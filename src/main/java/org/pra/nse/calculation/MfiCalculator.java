package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.MfiData;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.calc.MfiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.repository.CalcMfiRepository;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.pra.nse.calculation.CalcCons.MFI_DATA_FILE_PREFIX;
import static org.pra.nse.calculation.CalcCons.MFI_CSV_HEADER;

@Component
public class MfiCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MfiCalculator.class);

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final MfiCalculationDao dao;
    private final CalcMfiRepository repository;
    private final DataManager dataManager;

    public MfiCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                         MfiCalculationDao mfiCalculationDao, CalcMfiRepository calcMfiRepository, DataManager dataManager) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = mfiCalculationDao;
        this.repository = calcMfiRepository;
        this.dataManager = dataManager;
    }

    public void calculateAndSave(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

        String fileName = MFI_DATA_FILE_PREFIX + "-" + forDate.toString() + ApCo.DATA_FILE_EXT;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.COMPUTE_DIR_NAME +File.separator+ fileName;

        LOGGER.info("{} | for:{}", MFI_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", MFI_DATA_FILE_PREFIX, toDir);
            return;
        }

        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, 10);

        // calculate mfi for each s symbol
        List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (symbol, list)  -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, pm:{}, atp:{}, vol:{}, del:{}, H:{}, L:{}, C:{},L:{}",
//                                dto.getTradeDate(), dto.getAtpChgPrcnt(), dto.getAtp(), dto.getVolume(), dto.getDelivery(), dto.getHigh(), dto.getLow(), dto.getClose(), dto.getLast());
                        if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                            return dto.getAtp().multiply(dto.getVolume());
                        } else {
                            return dto.getAtp().multiply(dto.getVolume()).multiply(new BigDecimal(-1));
                        }
                    },
                    (dto, mfi) -> dto.setVolumeAtpMfi10(mfi));
            calculate(forDate, symbol, list,
                    dto -> {
                        if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                            return dto.getAtp().multiply(dto.getDelivery());
                        } else {
                            return dto.getAtp().multiply(dto.getDelivery()).multiply(new BigDecimal(-1));
                        }
                    },
                    (dto, mfi) -> {
                        dto.setDeliveryAtpMfi10(mfi);
                        dtos_ToBeSaved.add(dto);
                    }
            );
        });

        //
        saveToCsv(forDate, dtos_ToBeSaved);
        saveToDb(forDate, dtos_ToBeSaved);
    }

    public void calculate(LocalDate forDate,
                                String symbol,
                                List<DeliverySpikeDto> deliverySpikeDtoList,
                                Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
        // calculate mfi for each s symbol
        //LOGGER.info("mfi | for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;

        short upCtr = 0;
        BigDecimal up = BigDecimal.ZERO;
        short dnCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;

        DeliverySpikeDto latestDto = null;

//        if("EXIDEIND".equals(symbol)) {
//            LOGGER.info("");
//        }
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal rsiColumn = functionSupplier.apply(dsDto);
            if(rsiColumn==null) {
                rsiColumn = BigDecimal.ZERO;
            }
            if(rsiColumn.compareTo(zero) > 0)  {
                //up = up.add(dsDto.getTdycloseMinusYesclose());
                up = up.add(rsiColumn);
                upCtr++;
            } else {
                //dn = dn.add(dsDto.getTdycloseMinusYesclose());
                dn = dn.add(rsiColumn.abs());
                dnCtr++;
            }
        }

        if(upCtr == 0 || dnCtr == 0) {
            LOGGER.info("mfi | for symbol = {}, upCtr = {}, dnCtr = {}", symbol, upCtr, dnCtr);
        }

        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());

        BigDecimal moneyFlowRatio;
        moneyFlowRatio = up.divide(dnCtr == 0 ? BigDecimal.ONE : dn, 2, RoundingMode.HALF_UP);
        if(upCtr > 0 && dnCtr > 0) {
            moneyFlowRatio = up.divide(dn, 2, RoundingMode.HALF_UP);
        } else if(upCtr > 0 && dnCtr == 0) {
            moneyFlowRatio = up;
        } else if(upCtr == 0 && dnCtr > 0) {
            moneyFlowRatio = BigDecimal.ZERO;
        } else {
            moneyFlowRatio = BigDecimal.ZERO;
        }
        //mfi = 100 - (100 / (1 + moneyFlowRatio));
        //------------------------------------------
        //(1 + rs)
        BigDecimal mfi = moneyFlowRatio.add(BigDecimal.ONE);
        //(100 / (1 + rs)
        BigDecimal hundred = new BigDecimal(100);
        mfi = hundred.divide(mfi, 2, RoundingMode.HALF_UP);
        //100 - (100 / (1 + rs))
        mfi = hundred.subtract(mfi);
        //===========================================

        //if(latestDto!=null) latestDto.setTdyCloseRsi10Ema(rsi);
        if(latestDto!=null) biConsumer.accept(latestDto, mfi);
        else LOGGER.warn("skipping mfi, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    public void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }

    private void saveToCsv(LocalDate forDate, List<DeliverySpikeDto> dtoHavingMfi) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtoHavingMfi.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("{} | saving of csv skipped, discrepancy in the data", MFI_DATA_FILE_PREFIX);
        }
        if(forDateSet.size() != 1 && forDate.compareTo(dtoHavingMfi.get(0).getTradeDate())  != 0) {
            LOGGER.info("mfi | csv skipped, discrepancy in the data");
            return;
        }

        String fileName = MFI_DATA_FILE_PREFIX + "-" + forDate + ApCo.DATA_FILE_EXT;
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
        File file = new File(toPath);

        MfiData.saveOverWrite(MFI_CSV_HEADER, dtoHavingMfi, toPath, dto -> dto.toString());
    }

    private void saveToDb(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtos.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1 && forDate.compareTo(dtos.get(0).getTradeDate()) != 0) {
            LOGGER.info("{} | upload skipped, discrepancy in the data", MFI_DATA_FILE_PREFIX);
            return;
        }

        if(dao.dataCount(forDate) > 0) {
            LOGGER.info("{} | upload skipped, already uploaded", MFI_DATA_FILE_PREFIX);
            return;
        }

        CalcMfiTab tab = new CalcMfiTab();
        dtos.forEach( dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());

            tab.setVolumeAtpMfi10(dto.getVolumeAtpMfi10());
            tab.setDeliveryAtpMfi10(dto.getDeliveryAtpMfi10());

            repository.save(tab);
        });
    }

}

package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.*;
import org.pra.nse.service.DataService;
import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.NumberUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class RsiCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsiCalculator.class);

    private final String calc_name = CalcCons.RSI_FILE_PREFIX;
    private final String csv_header = CalcCons.RSI_CSV_HEADER;

    private final String computeFolderName = ApCo.RSI_DIR_NAME;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final RsiCalculationDao dao;
    private final CalcRsiRepository repository;
    private final DataService dataService;

    RsiCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                  RsiCalculationDao rsiCalculationDao, CalcRsiRepository calcRsiRepository, DataService dataService) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = rsiCalculationDao;
        this.repository = calcRsiRepository;
        this.dataService = dataService;
    }

    public List<RsiBean> calculateAndReturn(LocalDate forDate) {
        return calculateAndReturn(forDate, null);
    }
    public List<RsiBean> calculateAndReturn(LocalDate forDate, String forSymbol) {
        Map<String, RsiBean> beansMap = prepareData(forDate, forSymbol);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<RsiBean> rsiBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            rsiBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            return rsiBeanList;
        } else {
            return Collections.emptyList();
        }
    }

    public void calculateAndSave(LocalDate forDate) {
        String computeFilePath = getComputeOutputPath(forDate);
        LOGGER.info("{} | for:{}", calc_name, forDate.toString());
        if(nseFileUtils.isFileExist(computeFilePath)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", calc_name, computeFilePath);
            return;
        }

        Map<String, RsiBean> beansMap = prepareFullData(forDate);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<RsiBean> rsiBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            rsiBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            saveToCsv(forDate, rsiBeanList);
            //saveToDb(forDate, rsiBeanList);
        }
    }

    private Map<String, RsiBean> prepareFullData(LocalDate forDate) {
        return prepareData(forDate, null);
    }
    private Map<String, RsiBean> prepareData(LocalDate forDate, String forSymbol) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return Collections.emptyMap();

        LOGGER.info("{} calculating for 20 days", calc_name);
        Map<String, List<DeliverySpikeDto>> symbolMap;
        symbolMap = dataService.getRawDataBySymbol(forDate, 20, forSymbol);

        Map<String, RsiBean> beansMap = new HashMap<>();
        symbolMap.values().forEach( list -> {
            list.forEach( dto -> {
                if (dto.getTradeDate().compareTo(forDate) == 0) {
                    RsiBean bean = new RsiBean();
                    bean.setSymbol(dto.getSymbol());
                    bean.setTradeDate(dto.getTradeDate());
                    beansMap.put(dto.getSymbol(), bean);
                }
            });
        });

        loopIt(forDate, symbolMap,
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setAtpRsi20(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setCloseRsi20(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setLastRsi20(rsi)
        );

        LOGGER.info("{} calculating for 10 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 10, forSymbol);
        loopIt(forDate, symbolMap,
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setAtpRsi10(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setCloseRsi10(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setLastRsi10(rsi)
        );

        LOGGER.info("{} calculating for 05 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 5, forSymbol);
        loopIt(forDate, symbolMap,
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setAtpRsi05(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setCloseRsi05(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setLastRsi05(rsi)
        );

        //
        return beansMap;
    }

    private void loopIt(LocalDate forDate,
                        Map<String, List<DeliverySpikeDto>> symbolDtoMap,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerAtp,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerClose,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerLast) {
        //List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolDtoMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        LOGGER.debug("calc+:{}", dto.getTdyatpMinusYesatp());
                        return dto.getTdyatpMinusYesatp();
                    },
                    (dto, calculatedValue) -> biConsumerAtp.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getTdycloseMinusYesclose();
                    },
                    (dto, calculatedValue) -> biConsumerClose.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getTdylastMinusYeslast();
                    },
                    (dto, calculatedValue) -> biConsumerLast.accept(dto, calculatedValue)
            );
        });
        //return dtos_ToBeSaved;
    }

    public void calculate(LocalDate forDate, String symbol,
                            List<DeliverySpikeDto> spikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
//        if(spikeDtoList.size() != 10) {
//            LOGGER.warn("size of the dto list is not 10, it is {}, for {}", spikeDtoList.size(), spikeDtoList.get(0).getSymbol());
//        }

        //LOGGER.info("for symbol = {}", symbol);
        BigDecimal up = BigDecimal.ZERO;
        short upCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;
        short dnCtr = 0;
        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:spikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal rawPriceStrength = functionSupplier.apply(dsDto);
            if(rawPriceStrength == null || rawPriceStrength.compareTo(BigDecimal.ZERO) == 0)  {
                rawPriceStrength = BigDecimal.ZERO;
            } else if(rawPriceStrength.compareTo(BigDecimal.ZERO) == 1)  {
                upCtr++;
                up = up.add(rawPriceStrength);
                LOGGER.debug("up, rawPriceStrength {}, up {}, upCtr {}", rawPriceStrength, up, upCtr);
            } else if(rawPriceStrength.compareTo(BigDecimal.ZERO) == -1) {
                dnCtr++;
                dn = dn.add(rawPriceStrength);
                LOGGER.debug("dn, rawPriceStrength {}, dn {}, dnCtr {}", rawPriceStrength, dn, dnCtr);
            } else {
                LOGGER.error("rsi | {}, UNKNOWN CONDITION", symbol);
            }
        }
        LOGGER.debug("rsi | forSymbol = {}, forDate = {}, upCtr = {}, dnCtr = {}", symbol, forDate, upCtr, dnCtr);
//        if(upCtr == 0 || dnCtr == 0) {
//            LOGGER.warn("rsi | forSymbol = {}, forDate = {}, upCtr = {}, dnCtr = {}", symbol, forDate, upCtr, dnCtr);
//        }
        if(upCtr == 3 && dnCtr == 0) {
            LOGGER.info("rsi+ | forSymbol = {}, forDate = {}, upCtr = {}, dnCtr = {}", symbol, forDate, upCtr, dnCtr);
        }
        if(upCtr == 0 && dnCtr == 3) {
            LOGGER.info("rsi- | forSymbol = {}, forDate = {}, upCtr = {}, dnCtr = {}", symbol, forDate, upCtr, dnCtr);
        }

        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        //up = up.divide(upCtr == 0 ? BigDecimal.ONE : new BigDecimal(upCtr), 2, RoundingMode.HALF_UP);
        BigDecimal upAvg = NumberUtils.divide(up, new BigDecimal(upCtr));
        //dn = dn.divide(dnCtr == 0 ? BigDecimal.ONE : new BigDecimal(dnCtr), 2, RoundingMode.HALF_UP);
        BigDecimal dnAvg = NumberUtils.divide(dn, new BigDecimal(dnCtr));

        BigDecimal rs = BigDecimal.ZERO;
        if(up.compareTo(BigDecimal.ZERO) == 0 && dn.abs().compareTo(BigDecimal.ZERO) == 0) {
            LOGGER.warn("rsi | {}, Up and Dn both are Zero", symbol);
            rs = BigDecimal.ZERO;
        } else if (up.compareTo(BigDecimal.ZERO) == 1 && dn.abs().compareTo(BigDecimal.ZERO) == 0) {
            LOGGER.debug("rsi | {}, all closing are Up ({})", symbol, upAvg);
            //rs = up.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
            rs = upAvg;
        } else if (up.compareTo(BigDecimal.ZERO) == 0 && dn.abs().compareTo(BigDecimal.ZERO) == 1) {
            LOGGER.debug("rsi | {}, all closing are Dn ({})", symbol, dnAvg);
            //rs = up.divide(dn.abs(), 2, RoundingMode.HALF_UP);
            rs = BigDecimal.ZERO;
        } else {
            LOGGER.debug("rsi | {}, Up and Dn both are non-Zero  ({})  ({})", symbol, upAvg, dnAvg);
            //rs = up.divide(dn.abs(), 2, RoundingMode.HALF_UP);
            rs = NumberUtils.divide(upAvg, dnAvg.abs());
        }
        LOGGER.debug("rsi | {}, (rs) ={}, upAvg = {}, dnAvg = {}", symbol, rs, upAvg, dnAvg);

        //rsi = 100 - (100 / (1 + rs));
        //------------------------------------------
        //(1 + rs)
        BigDecimal rsi = rs.add(BigDecimal.ONE);
        LOGGER.debug("rsi | {}, (1 + rs) = {}", symbol, rsi);
        //(100 / (1 + rs)
        rsi = NumberUtils.divide(NumberUtils.HUNDRED, rsi);
        LOGGER.debug("rsi | {}, (100 / (1 + rs) = {}", symbol, rsi);
        //100 - (100 / (1 + rs))
        rsi = NumberUtils.HUNDRED.subtract(rsi);
        LOGGER.debug("rsi | {}, 100 - (100 / (1 + rs)) = {}", symbol, rsi);
        //===========================================

        if(latestDto != null) biConsumer.accept(latestDto, rsi);
        else LOGGER.warn("skipping rsi, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    private void saveToCsv(LocalDate forDate, List<RsiBean> dtos) {
        String computeToFilePath = getComputeOutputPath(forDate);
        RsiCao.saveOverWrite(csv_header, dtos, computeToFilePath, dto -> dto.toCsvString());
        LOGGER.info("{} | saved on disk ({})", calc_name, computeToFilePath);
    }

    private void saveToDb(LocalDate forDate, List<RsiBean> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcRsiTab tab = new CalcRsiTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setCloseRsi10Sma(dto.getCloseRsi10());
                tab.setLastRsi10Sma(dto.getLastRsi10());
                tab.setAtpRsi10Sma(dto.getAtpRsi10());

                tab.setCloseRsi20Sma(dto.getCloseRsi20());
                tab.setLastRsi20Sma(dto.getLastRsi20());
                tab.setAtpRsi20Sma(dto.getAtpRsi20());

                repository.save(tab);
            });
            LOGGER.info("{} | uploaded", calc_name);
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", calc_name);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", calc_name, dataCtr, dtos.size());
        }
    }

    private String getComputeOutputPath(LocalDate forDate) {
        String computeFileName = calc_name + forDate + ApCo.DATA_FILE_EXT;
        String computePath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + computeFileName;
        return computePath;
    }

}

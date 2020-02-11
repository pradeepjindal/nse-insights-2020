package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.AvgBean;
import org.pra.nse.csv.data.AvgCao;
import org.pra.nse.csv.data.CalcBean;
import org.pra.nse.service.DataService;
import org.pra.nse.db.dao.calc.AvgCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.repository.CalcAvgRepository;
import org.pra.nse.service.DateService;
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
public class AvgCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCalculator.class);

    private final String calc_name = CalcCons.AVG_FILE_PREFIX;
    private final String csv_header = CalcCons.AVG_CSV_HEADER;

    private final String computeFolderName = ApCo.AVG_DIR_NAME;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final AvgCalculationDao dao;
    private final CalcAvgRepository repository;
    private final DataService dataService;
    private final DateService dateService;

    public AvgCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                         AvgCalculationDao avgCalculationDao, CalcAvgRepository calcAvgRepository, DataService dataService,
                         DateService dateService) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = avgCalculationDao;
        this.repository = calcAvgRepository;
        this.dataService = dataService;
        this.dateService = dateService;
    }

    public List<AvgBean> calculateAndReturn(LocalDate forDate) {
        Map<String, AvgBean> beansMap = prepareData(forDate);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<AvgBean> avgBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            avgBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            return avgBeanList;
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

        Map<String, AvgBean> beansMap = prepareData(forDate);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<AvgBean> avgBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            avgBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            saveToCsv(forDate, avgBeanList);
            //saveToDb(forDate, avgBeanList);
        }
    }

    private Map<String, AvgBean> prepareData(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return Collections.emptyMap();

        Map<String, List<DeliverySpikeDto>> symbolMap;
        Map<String, AvgBean> beansMap = new HashMap<>();

        // prepare beans
        symbolMap = dataService.getRawDataBySymbol(forDate, 1);
        symbolMap.values().forEach( list -> {
            list.forEach( dto -> {
                if (dto.getTradeDate().compareTo(forDate) == 0) {
                    AvgBean bean = new AvgBean();
                    bean.setSymbol(dto.getSymbol());
                    bean.setTradeDate(dto.getTradeDate());
                    beansMap.put(dto.getSymbol(), bean);
                }
            });
        });

        LOGGER.info("{} calculating for 20 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 20);
        loopIt(forDate, symbolMap,
                (dto, avg) -> beansMap.get(dto.getSymbol()).setAtpAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setVolAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setDelAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setFoiAvg20(avg)
        );

        LOGGER.info("{} calculating for 10 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 10);
        loopIt(forDate, symbolMap,
                (dto, avg) -> beansMap.get(dto.getSymbol()).setAtpAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setVolAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setDelAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setFoiAvg10(avg)
        );

        LOGGER.info("{} calculating for 05 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 5);
        loopIt(forDate, symbolMap,
                (dto, avg) -> beansMap.get(dto.getSymbol()).setAtpAvg05(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setVolAvg05(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setDelAvg05(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setFoiAvg05(avg)
        );

        return beansMap;
    }

    private void loopIt(LocalDate forDate,
                        Map<String, List<DeliverySpikeDto>> symbolDtosMap,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerAtp,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerVol,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerDel,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerFoi) {
        //List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolDtosMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        return dto.getAtp();
                    },
                    (dto, calculatedValue) -> biConsumerAtp.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getVolume();
                    },
                    (dto, calculatedValue) -> biConsumerVol.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getDelivery();
                    },
                    (dto, calculatedValue) -> biConsumerDel.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getOi();
                    },
                    (dto, calculatedValue) -> biConsumerFoi.accept(dto, calculatedValue)
            );
        });
        //return dtos_ToBeSaved;
    }

    private void calculate(LocalDate forDate, String symbol,
                            List<DeliverySpikeDto> spikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto, BigDecimal> biConsumer) {
        // calculate avg for each symbol
        //LOGGER.info("avg | for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;

        short ctr = 0;
        BigDecimal numberOfTrades = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:spikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal indicatorColumn = functionSupplier.apply(dsDto);
            if(indicatorColumn == null || indicatorColumn.compareTo(BigDecimal.ZERO) == 0) {
                indicatorColumn = BigDecimal.ZERO;
            } else {
                ctr++;
                sum = sum.add(indicatorColumn);
            }
        }
        if(ctr != spikeDtoList.size()) {
            LOGGER.warn("avg | for symbol = {}, ctr mismatch {}", symbol, ctr);
        }
        if(ctr == 0) {
            LOGGER.info("avg | for symbol = {}, ctr = {}", symbol, ctr);
        }

        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        BigDecimal avg = NumberUtils.divide(sum, new BigDecimal(ctr));
        //===========================================

        if(latestDto != null) biConsumer.accept(latestDto, avg);
        else LOGGER.warn("skipping avg, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, avg = {}", symbol, avg);
    }

    private void saveToCsv(LocalDate forDate, List<AvgBean> beans) {
        String computeToFilePath = getComputeOutputPath(forDate);
        AvgCao.saveOverWrite(csv_header, beans, computeToFilePath, bean -> bean.toCsvString());
        LOGGER.info("{} | saved on disk ({})", calc_name, computeToFilePath);
    }

    private void saveToDb(LocalDate forDate, List<AvgBean> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcAvgTab tab = new CalcAvgTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setAtpAvg10Sma(dto.getAtpAvg10());
                tab.setVolAvg10Sma(dto.getVolAvg10());
                tab.setDelAvg10Sma(dto.getDelAvg10());
                tab.setOiAvg10Sma(dto.getFoiAvg10());

                tab.setAtpAvg20Sma(dto.getAtpAvg20());
                tab.setVolAvg20Sma(dto.getVolAvg20());
                tab.setDelAvg20Sma(dto.getDelAvg20());
                tab.setOiAvg20Sma(dto.getFoiAvg20());

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

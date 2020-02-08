package org.pra.nse.service;

import org.pra.nse.Manager;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.db.dao.nse.OiDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.dto.OiDto;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcAvgRepository;
import org.pra.nse.db.repository.CalcMfiRepository;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.report.ReportHelper;
import org.pra.nse.util.NumberUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class DataService implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    private final PraFileUtils praFileUtils;
    private final NseReportsDao nseReportsDao;
    private final OiDao oiDao;
    private final DateService dateService;

    private final CalcRsiRepository calcRsiRepository;
    private final CalcMfiRepository calcMfiRepository;
    private final CalcAvgRepository calcAvgRepository;

    private final NavigableMap<Integer, LocalDate>  tradeDates_NavigableMap = new TreeMap<>();
    private final List<LocalDate>                   tradeDates_Desc_LinkedList = new LinkedList<>();
    private final Map<LocalDate, LocalDate>         nextDateMap = new TreeMap<>();
    private final Map<LocalDate, LocalDate>         backDateMap = new TreeMap<>();

    private List<DeliverySpikeDto> dbResults = null;

    private LocalDate       latestDbDate = null;
    private List<LocalDate> latest10Dates = null;
    private List<LocalDate> latest20Dates = null;
    private boolean isDataInRawState = true;

    public DataService(PraFileUtils praFileUtils, NseReportsDao nseReportsDao, OiDao oiDao,
                       DateService dateService,
                       CalcRsiRepository calcRsiRepository, CalcMfiRepository calcMfiRepository, CalcAvgRepository calcAvgRepository) {
        this.praFileUtils = praFileUtils;
        this.nseReportsDao = nseReportsDao;
        this.oiDao = oiDao;
        this.dateService = dateService;
        this.calcRsiRepository = calcRsiRepository;
        this.calcMfiRepository = calcMfiRepository;
        this.calcAvgRepository = calcAvgRepository;
    }


    @Override
    public void execute() {
        LOGGER.info("Data Manger - Shop is open..........");
    }

//    public LocalDate getMinDate(LocalDate forDate, int forMinusDays) {
//        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
//        if(dbResults == null || latestNseDate.isAfter(latestDbDate)) {
//            bootUpData();
//            fillTheCalcFields();
//            fillTheOi();
//            fillTheNext();
//            fillTheIndicators();
//        }
//        if(forDate.isAfter(latestDbDate))
//            return null;
//        else
//            return minDate(forDate, forMinusDays);
//    }

    public Map<String, List<DeliverySpikeDto>> getRawDataBySymbol(LocalDate forDate, int forMinusDays) {
        return getRawDataBySymbol(forDate, forMinusDays, null);
    }
    public Map<String, List<DeliverySpikeDto>> getRawDataBySymbol(LocalDate forDate, int forMinusDays, String forSymbol) {
        Predicate<DeliverySpikeDto> predicate = initializeRawData(forDate, forMinusDays, forSymbol);
        return predicate == null ? Collections.EMPTY_MAP : prepareDataBySymbol(predicate);
    }

    public Map<String, List<DeliverySpikeDto>> getRichDataBySymbol(LocalDate forDate, int forMinusDays) {
        return getRichDataBySymbol(forDate, forMinusDays, null);
    }
    public Map<String, List<DeliverySpikeDto>> getRichDataBySymbol(LocalDate forDate, int forMinusDays, String forSymbol) {
        Predicate<DeliverySpikeDto> predicate =  initializeData(forDate, forMinusDays, forSymbol);
        return predicate == null ? Collections.EMPTY_MAP : prepareDataBySymbol(predicate);
    }

    public Map<LocalDate, Map<String, DeliverySpikeDto>> getRichDataByTradeDateAndSymbol(LocalDate forDate, int forMinusDays) {
        return getRichDataByTradeDateAndSymbol(forDate, forMinusDays, null);
    }
    public Map<LocalDate, Map<String, DeliverySpikeDto>> getRichDataByTradeDateAndSymbol(LocalDate forDate, int forMinusDays, String forSymbol) {
        Predicate<DeliverySpikeDto> predicate =  initializeData(forDate, forMinusDays, forSymbol);
        return predicate == null ? Collections.EMPTY_MAP : prepareDataByTradeDateAndSymbol(predicate);
    }

    private Predicate<DeliverySpikeDto> initializeRawData(LocalDate forDate, int forMinusDays, String forSymbol) {
        if(!dateService.validateTradeDate(forDate)) return null;
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(dbResults == null || latestNseDate.isAfter(latestDbDate)) {
            bootUpData();
//            fillTheCalcFields();
//            fillTheOi();
//            fillTheNext();
//            fillTheIndicators();
        }
        if(forDate.isAfter(latestDbDate))
            return null;
        else
            return predicateIt(forDate, forMinusDays, forSymbol);
    }

    private Predicate<DeliverySpikeDto> initializeData(LocalDate forDate, int forMinusDays, String forSymbol) {
        if(!dateService.validateTradeDate(forDate)) return null;
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(dbResults == null || latestNseDate.isAfter(latestDbDate)) {
            bootUpData();
            isDataInRawState = true;
        }
        if (isDataInRawState) {
            fillTheCalcFields();
            fillTheOi();
            fillTheNext();
            fillTheIndicators();
            isDataInRawState = false;
        }
        if(forDate.isAfter(latestDbDate))
            return null;
        else
            return predicateIt(forDate, forMinusDays, forSymbol);
    }

    private Predicate<DeliverySpikeDto> predicateIt(LocalDate forDate, int forMinusDays, String forSymbol) {
        //TODO what if minDate is null
        LocalDate minDate = minDate(forDate, forMinusDays);
        Predicate<DeliverySpikeDto> predicate = null;
        if (forSymbol == null) {
            predicate = dto -> filterDate(dto, minDate, forDate);
        } else {
            predicate = dto -> filterDateAndSymbol(dto, minDate, forDate, forSymbol);
        }
        return predicate;
    }


    private void bootUpData() {
        dbResults = nseReportsDao.getDeliverySpike();

        NavigableMap<LocalDate, LocalDate> map = new TreeMap<>();
        dbResults.forEach( row-> {
            map.put(row.getTradeDate(), row.getTradeDate());
        });

        AtomicInteger dateCount = new AtomicInteger();
        tradeDates_NavigableMap.clear();
        map.descendingKeySet().stream().forEach( key -> tradeDates_NavigableMap.put(dateCount.incrementAndGet(), key));
        //tradeDates_NavigableMap.descendingMap();

        tradeDates_Desc_LinkedList.clear();
        map.descendingKeySet().stream().forEach( key -> tradeDates_Desc_LinkedList.add(key));
        //tradeDates_SortedLinkedList.reverse();

        initializeTradeDates();
        initializeBackTradeDates();
        initializeNextTradeDates();

        dbResults.forEach( row-> {
            row.setBackDate(backDateMap.get(row.getTradeDate()));
            row.setNextDate(nextDateMap.get(row.getTradeDate()));
        });
    }

    private void initializeTradeDates() {
        Set<LocalDate> tradeDates = new HashSet<>();
        dbResults.forEach( row-> {
            tradeDates.add(row.getTradeDate());
        });
        List<LocalDate> list = tradeDates.stream().collect(Collectors.toList());
        Collections.sort(list, Collections.reverseOrder());

        latestDbDate = list.get(0);
        latest10Dates = list.stream().limit(10).collect(Collectors.toList());
        latest20Dates = list.stream().limit(20).collect(Collectors.toList());
    }

    private void initializeBackTradeDates() {
        for(int i = 0; i < tradeDates_Desc_LinkedList.size() - 1; i++) {
            //LOGGER.info("tdy: {}, nxt:{}", i+1, i);
            backDateMap.put(tradeDates_Desc_LinkedList.get(i), tradeDates_Desc_LinkedList.get(i+1));
            //LOGGER.info("tdy: {}, nxt:{}", tradeDates_Desc_LinkedList.get(i+1), tradeDates_Desc_LinkedList.get(i));
        }
    }
    private void initializeNextTradeDates() {
        for(int i = 0; i < tradeDates_Desc_LinkedList.size() - 1; i++) {
            //LOGGER.info("tdy: {}, nxt:{}", i+1, i);
            nextDateMap.put(tradeDates_Desc_LinkedList.get(i+1), tradeDates_Desc_LinkedList.get(i));
            //LOGGER.info("tdy: {}, nxt:{}", tradeDates_Desc_LinkedList.get(i+1), tradeDates_Desc_LinkedList.get(i));
        }
    }

    private LocalDate minDate(LocalDate forDate, int forMinusDays) {
        int fromIndex = tradeDates_Desc_LinkedList.indexOf(forDate);
        int toIndexDesc = fromIndex + forMinusDays -1;
        //LocalDate maxDate = tradeDates_Desc_LinkedList.get(fromIndex);
        LocalDate minDate = tradeDates_Desc_LinkedList.get(toIndexDesc);
        LOGGER.info("forDate:{}, minusDays:{}, minDate:{}, maxDate:{}", forDate, forMinusDays, minDate, forDate);
        return minDate;
    }

    private boolean filterDateAndSymbol(DeliverySpikeDto dto, LocalDate minDate, LocalDate maxDate, String symbol) {
        //return filterDate(dto, minDate, maxDate) && symbol.toUpperCase().equals(dto.getSymbol());
        return filterDate(dto, minDate, maxDate) && filterSymbol(dto, symbol);
    }
    private boolean filterDate(DeliverySpikeDto dto, LocalDate minDate, LocalDate maxDate) {
        return dto.getTradeDate().isAfter(minDate.minusDays(1)) && dto.getTradeDate().isBefore(maxDate.plusDays(1));
    }
    private boolean filterSymbol(DeliverySpikeDto dto, String symbol) {
        return symbol.toUpperCase().equals(dto.getSymbol());
    }

    private void fillTheCalcFields() {
        LOGGER.info("DataManager - fillTheCalcFields");
        BigDecimal TWO = new BigDecimal(2);
        BigDecimal FOUR = new BigDecimal(4);
        BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal onePercent = null;

        BigDecimal ohlcSum = null;
        BigDecimal diff = null;
        BigDecimal highLowDiffByHalf = null;
        BigDecimal biggerValue = null;
        BigDecimal smallerValue = null;
        for(DeliverySpikeDto row:dbResults) {
            //ohlc
            ohlcSum = row.getOpen().add(row.getHigh()).add(row.getLow()).add(row.getClose());
            row.setOhlc(NumberUtils.divide(ohlcSum, FOUR));
            //hlm
            diff = row.getHigh().subtract(row.getLow());
            highLowDiffByHalf = NumberUtils.divide(diff, TWO);
            row.setHighLowMid(row.getLow().add(highLowDiffByHalf));
            //hlp
            onePercent = NumberUtils.onePercent(row.getOpen());
            row.setHighLowPct(NumberUtils.divide(diff, onePercent));
            //closeToLastPct

            if(row.getLast().compareTo(row.getClose()) == 1) {
                biggerValue = row.getLast();
                smallerValue = row.getClose();
                    onePercent = NumberUtils.onePercent(smallerValue);
                    diff = biggerValue.subtract(smallerValue);
                    row.setCloseToLastPercent(NumberUtils.divide(diff, onePercent));
            } else if (row.getClose().compareTo(row.getLast()) == 1) {
                biggerValue = row.getLast();
                smallerValue = row.getClose();
                    onePercent = NumberUtils.onePercent(smallerValue);
                    diff = biggerValue.subtract(smallerValue);
                    row.setCloseToLastPercent(NumberUtils.divide(diff, onePercent));
            } else {
                row.setCloseToLastPercent(BigDecimal.ZERO);
            }

        }
    }

    private void fillTheOi() {
        LOGGER.info("DataManager - fillTheOi");
        //LocalDate minDate = minDate(latestDbDate, 20);
        Predicate<DeliverySpikeDto> predicate = dto -> true;
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);

        List<OiDto> dbResults = oiDao.getOiAll();
        Map<String, Map<LocalDate, OiDto>> oiMap = new HashMap<>();

//        Map<String, Map<LocalDate, OiDto>> localMap = new HashMap<>();
//        dbResults.forEach( filteredRow-> {
//            if(localMap.containsKey(filteredRow.getSymbol())) {
//                if(localMap.get(filteredRow.getSymbol()).containsKey(filteredRow.getTradeDate())) {
//                    LOGGER.warn("tradeDate-symbol | matched symbol {} tradeDate {}", filteredRow.getSymbol(), filteredRow.getTradeDate());
//                } else {
//                    localMap.get(filteredRow.getSymbol()).put(filteredRow.getTradeDate(), filteredRow);
//                }
//            } else {
//                LOGGER.warn("oi | new entry. symbol {} tradeDate {}", filteredRow.getSymbol(), filteredRow.getTradeDate());
//                Map<LocalDate, OiDto> map = new HashMap<>();
//                map.put(filteredRow.getTradeDate(), filteredRow);
//                localMap.put(filteredRow.getSymbol(), map);
//                //LOGGER.info("tradeDate-symbol | tradeDate {}", filteredRow.getTradeDate());
//            }
//        });

        dbResults.forEach( row -> {
            try {
                tradeDateAndSymbolMap.get(row.getTradeDate()).get(row.getSymbol()).setOi(row.getSumOi());
            } catch (Exception e) {
                //LOGGER.warn("oi not found, {} for {}", row.getSymbol(), row.getTradeDate());
            }
        });
    }

    private void fillTheNext() {
        LOGGER.info("DataManager - fillTheNext");
        Predicate<DeliverySpikeDto> predicate = dto -> true;
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);
        for(DeliverySpikeDto dto:dbResults) {
            LocalDate nextDate = nextDateMap.get(dto.getTradeDate());
            //if(nextDate.compareTo(latestDbDate) == 1) continue;
            if(nextDate == null) continue;
            DeliverySpikeDto nextDto = tradeDateAndSymbolMap.get(nextDate).get(dto.getSymbol());
            if (nextDto == null) {
                LOGGER.warn("{} - no next data for: {} (may be symbol has phased out of fno)", dto.getSymbol(), nextDate);
            } else {
                dto.setNxtCloseToOpenPercent(nextDto.getCloseToOpenPercent());
                dto.setNxtOptoHighPrcnt(nextDto.getOthighPrcnt());
                dto.setNxtOptoLowPrcnt(nextDto.getOtlowPrcnt());
                dto.setNxtOptoAtpPrcnt(nextDto.getOtatpPrcnt());
            }
        }
    }

    private void fillTheNextOld() {
        LOGGER.info("DataManager - fillTheNext");
        LocalDate minDate = minDate(latestDbDate, 20);
        Predicate<DeliverySpikeDto> predicate = dto -> filterDate(dto, minDate, latestDbDate);
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);
        //TODO use tradeDateAndSymbolMap instead of dbResults BUT dbResults keep the order while map not
        long ctr = dbResults.stream()
                .filter( row -> row.getTradeDate().isAfter(tradeDates_Desc_LinkedList.get(20))
                && row.getTradeDate().isBefore(tradeDates_Desc_LinkedList.get(0)))
                .map( filteredRow -> {
                    LocalDate nextDate = nextDateMap.get(filteredRow.getTradeDate());
                    DeliverySpikeDto nextDto = tradeDateAndSymbolMap.get(nextDate).get(filteredRow.getSymbol());
                    if (nextDto == null) {
                        LOGGER.warn("{} - no next data for: {} (may be symbol has phased out of fno)", filteredRow.getSymbol(), nextDate);
                    } else {
                        filteredRow.setNxtCloseToOpenPercent(nextDto.getCloseToOpenPercent());
                        filteredRow.setNxtOptoHighPrcnt(nextDto.getOthighPrcnt());
                        filteredRow.setNxtOptoLowPrcnt(nextDto.getOtlowPrcnt());
                        filteredRow.setNxtOptoAtpPrcnt(nextDto.getOtatpPrcnt());
                    }
                    return true;
                }).count();
    }

//    private void fillTheIndicators() {
//        LocalDate minDate = minDate(latestDbDate, 21);
//        Predicate<DeliverySpikeDto> predicate = dto -> filterDate(dto, minDate, latestDbDate);
//        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);
//
//        //load old Rsi
//        List<CalcRsiTab> oldRsiList = calcRsiRepository.findAll();
//        ReportHelper.enrichRsi(oldRsiList, tradeDateAndSymbolMap);
//
//        //load old Mfi
//        List<CalcMfiTab> oldMfiList = calcMfiRepository.findAll();
//        ReportHelper.enrichMfi(oldMfiList, tradeDateAndSymbolMap);
//
//        BigDecimal HUNDRED = new BigDecimal(100);
////        LocalDate minDate = minDate(latestDbDate, 20);
////        Predicate<DeliverySpikeDto> predicate = dto -> filterDate(dto, minDate, latestDbDate);
////        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);
//        //TODO use tradeDateAndSymbolMap instead of dbResults BUT dbResults keep the order while map not
//        long ctr = dbResults.stream()
//                .filter( row -> row.getTradeDate().isAfter(tradeDates_Desc_LinkedList.get(20)))
//                .map( filteredRow -> {
//                    LocalDate backDate = backDateMap.get(filteredRow.getTradeDate());
//                    DeliverySpikeDto backDto = null;
//                    if(tradeDateAndSymbolMap.containsKey(backDate))
//                        backDto = tradeDateAndSymbolMap.get(backDate).get(filteredRow.getSymbol());
//                    if (backDto == null) {
//                        LOGGER.warn("{} - backDto is null for: {}", filteredRow.getSymbol(), backDate);
//                    } else if (backDto.getDelAtpMfi() == null) {
//                        LOGGER.warn("{} - DelAtpMfi is null for: {}", filteredRow.getSymbol(), backDate);
//                    } else if (backDto.getAtpRsi() == null) {
//                        LOGGER.warn("{} - AtpRsi is null for: {}", filteredRow.getSymbol(), backDate);
//                    } else {
//                        filteredRow.setDelAtpMfiChg(
//                                filteredRow.getDelAtpMfi().divide(
//                                        backDto.getDelAtpMfi().divide(HUNDRED, 2, RoundingMode.HALF_UP),
//                                        2, RoundingMode.HALF_UP
//                                )
//                        );
//                        filteredRow.setAtpRsiChg(
//                                filteredRow.getAtpRsi().divide(
//                                        backDto.getAtpRsi().divide(HUNDRED, 2, RoundingMode.HALF_UP),
//                                        2, RoundingMode.HALF_UP
//                                )
//                        );
//                    }
//                    return true;
//                }).count();
//    }
    private void fillTheIndicators() {
        LOGGER.info("DataManager - fillTheIndicators");
        LocalDate minDate = minDate(latestDbDate, 21);
        Predicate<DeliverySpikeDto> predicate = dto -> filterDate(dto, minDate, latestDbDate);
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);

        //load old Rsi
        List<CalcRsiTab> oldRsiList = calcRsiRepository.findAll();
        ReportHelper.enrichRsi(oldRsiList, tradeDateAndSymbolMap);

        //load old Mfi
        List<CalcMfiTab> oldMfiList = calcMfiRepository.findAll();
        ReportHelper.enrichMfi(oldMfiList, tradeDateAndSymbolMap);

        LocalDate backDate = null;
        DeliverySpikeDto backDto = null;
        BigDecimal onePercent = null;
        BigDecimal diff = null;
        BigDecimal chg = null;
        for(DeliverySpikeDto dto:dbResults) {
            if(dto.getTradeDate().isAfter(tradeDates_Desc_LinkedList.get(20))) {
                backDto = null;
                backDate = backDateMap.get(dto.getTradeDate());
                if(tradeDateAndSymbolMap.containsKey(backDate))
                    backDto = tradeDateAndSymbolMap.get(backDate).get(dto.getSymbol());
                if (backDto == null) {
                    LOGGER.warn("{} - backDto is null for: {}", dto.getSymbol(), backDate);
                } else if (backDto.getDelAtpMfi() == null) {
                    LOGGER.warn("{} - DelAtpMfi is null for: {}", dto.getSymbol(), backDate);
                } else if (backDto.getAtpRsi() == null) {
                    LOGGER.warn("{} - AtpRsi is null for: {}", dto.getSymbol(), backDate);
                } else {
                    onePercent = backDto.getDelAtpMfi().divide(NumberUtils.HUNDRED, 2, RoundingMode.HALF_UP);
                    diff = dto.getDelAtpMfi().divide(onePercent, 2, RoundingMode.HALF_UP);
                    chg = diff.subtract(NumberUtils.HUNDRED);
                    dto.setDelAtpMfiChg(chg);

                    onePercent = backDto.getAtpRsi().divide(NumberUtils.HUNDRED, 2, RoundingMode.HALF_UP);
                    diff = dto.getAtpRsi().divide(onePercent, 2, RoundingMode.HALF_UP);
                    chg = diff.subtract(NumberUtils.HUNDRED);
                    dto.setAtpRsiChg(chg);
                }
            }
        }
    }

    private Map<String, List<DeliverySpikeDto>> prepareDataBySymbol(Predicate<DeliverySpikeDto> filterPredicate) {
        // aggregate trade by symbols
        // symbol wise trade list
        Map<String, List<DeliverySpikeDto>> localMap = new HashMap<>();
        long rowCount = dbResults.stream()
                .filter( filterPredicate )
                //.filter( passedRow -> "ACC".toUpperCase().equals(passedRow.getSymbol()))
                .map( filteredRow -> {
                    if(localMap.containsKey(filteredRow.getSymbol())) {
                        localMap.get(filteredRow.getSymbol()).add(filteredRow);
                    } else {
                        List<DeliverySpikeDto> list = new ArrayList<>();
                        list.add(filteredRow);
                        localMap.put(filteredRow.getSymbol(), list);
                    }
                    return true;
                })
                .count();
        return localMap;
    }
    private Map<LocalDate, Map<String, DeliverySpikeDto>> prepareDataByTradeDateAndSymbol(Predicate<DeliverySpikeDto> filterPredicate) {
        // aggregate trade by symbols
        // tradeDateAndSymbolWise_DoubleMap
        Map<LocalDate, Map<String, DeliverySpikeDto>> localMap = new HashMap<>();
        long rowCount = dbResults.stream()
                .filter( filterPredicate )
                .map( filteredRow -> {
                    if(localMap.containsKey(filteredRow.getTradeDate())) {
                        if(localMap.get(filteredRow.getTradeDate()).containsKey(filteredRow.getSymbol())) {
                            LOGGER.warn("tradeDate-symbol | matched tradeDate {} symbol {}", filteredRow.getTradeDate(), filteredRow.getSymbol());
                        } else {
                            localMap.get(filteredRow.getTradeDate()).put(filteredRow.getSymbol(), filteredRow);
                        }
                    } else {
                        Map<String, DeliverySpikeDto> map = new HashMap<>();
                        map.put(filteredRow.getSymbol(), filteredRow);
                        localMap.put(filteredRow.getTradeDate(), map);
                        //LOGGER.info("tradeDate-symbol | tradeDate {}", filteredRow.getTradeDate());
                    }
                    return true;
                })
                .count();
        return localMap;
    }

}

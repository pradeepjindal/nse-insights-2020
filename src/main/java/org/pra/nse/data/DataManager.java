package org.pra.nse.data;

import org.pra.nse.Manager;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.db.dao.nse.OiDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.dto.OiDto;
import org.pra.nse.service.TradeDateService;
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
public class DataManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    private final PraFileUtils praFileUtils;
    private final NseReportsDao nseReportsDao;
    private final OiDao oiDao;
    private final TradeDateService tradeDateService;

    private final NavigableMap<Integer, LocalDate>  tradeDates_NavigableMap = new TreeMap<>();
    private final List<LocalDate>                   tradeDates_Desc_LinkedList = new LinkedList<>();
    private final Map<LocalDate, LocalDate>         nextDateMap = new TreeMap<>();

    private List<DeliverySpikeDto> dbResults = null;

    private LocalDate       latestDbDate = null;
    private List<LocalDate> latest10Dates = null;
    private List<LocalDate> latest20Dates = null;


    public DataManager(PraFileUtils praFileUtils, NseReportsDao nseReportsDao, OiDao oiDao,
                       TradeDateService tradeDateService) {
        this.praFileUtils = praFileUtils;
        this.nseReportsDao = nseReportsDao;
        this.oiDao = oiDao;
        this.tradeDateService = tradeDateService;
    }


    @Override
    public void execute() {
        LOGGER.info("Data Manger - Shop is open..........");
    }

    public LocalDate getMinDate(LocalDate forDate, int forMinusDays) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(dbResults == null || latestNseDate.isAfter(latestDbDate)) {
            bootUpData();
            fillTheOhlc();
            fillTheOi();
            fillTheNext();
        }
        if(forDate.isAfter(latestDbDate))
            return null;
        else
            return minDate(forDate, forMinusDays);
    }



    public Map<String, List<DeliverySpikeDto>> getDataBySymbol(LocalDate forDate, int forMinusDays) {
        return getDataBySymbol(forDate, forMinusDays, null);
    }
    public Map<String, List<DeliverySpikeDto>> getDataBySymbol(LocalDate forDate, int forMinusDays, String forSymbol) {
        Predicate<DeliverySpikeDto> predicate =  initializeData(forDate, forMinusDays, forSymbol);
        return predicate == null ? Collections.EMPTY_MAP : prepareDataBySymbol(predicate);
    }

    public Map<LocalDate, Map<String, DeliverySpikeDto>> getDataByTradeDateAndSymbol(LocalDate forDate, int forMinusDays) {
        return getDataByTradeDateAndSymbol(forDate, forMinusDays, null);
    }
    public Map<LocalDate, Map<String, DeliverySpikeDto>> getDataByTradeDateAndSymbol(LocalDate forDate, int forMinusDays, String forSymbol) {
        Predicate<DeliverySpikeDto> predicate =  initializeData(forDate, forMinusDays, forSymbol);
        return predicate == null ? Collections.EMPTY_MAP : prepareDataByTradeDateAndSymbol(predicate);
    }

    private Predicate<DeliverySpikeDto> initializeData(LocalDate forDate, int forMinusDays, String forSymbol) {
        if(!tradeDateService.validateTradeDate(forDate)) return null;
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(dbResults == null || latestNseDate.isAfter(latestDbDate)) {
            bootUpData();
            fillTheOhlc();
            fillTheOi();
            fillTheNext();
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
        initializeNextTradeDates();
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

    private void fillTheOhlc() {
        BigDecimal four = new BigDecimal(4);
        dbResults.forEach( row-> {
            BigDecimal ohlc = row.getOpen().add(row.getHigh()).add(row.getLow()).add(row.getClose());
            row.setOhlc(ohlc.divide(four, 2, RoundingMode.HALF_UP));
        });
    }

    private void fillTheOi() {
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
        LocalDate minDate = minDate(latestDbDate, 20);
        Predicate<DeliverySpikeDto> predicate = dto -> filterDate(dto, minDate, latestDbDate);
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolMap = prepareDataByTradeDateAndSymbol(predicate);
        //TODO use tradeDateAndSymbolMap instead of dbResults BUT dbResults keep the order while map not
        long ctr = dbResults.stream().filter( row -> row.getTradeDate().isAfter(tradeDates_Desc_LinkedList.get(20))
                && row.getTradeDate().isBefore(tradeDates_Desc_LinkedList.get(0))
        )
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

    private void fillTheGrowth() {

    }

}

package org.pra.nse.calculation;

import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.dto.OiDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CalculationHelper {
    //    <R> Stream<R> map(Function<? super T, ? extends R> mapper);
//    public static <R> Map<> transformBySymbol(List<? extends Dto> dtos, Predicate<? extends Dto> filterPredicate) {
//        // aggregate trade by symbols
//        // symbol wise trade list
//        Map<String, List<OiDto>> localMap = new HashMap<>();
//        long rowCount = dtos.stream()
//                //.filter( filterPredicate )
//                //.filter( passedRow -> "ACC".toUpperCase().equals(passedRow.getSymbol()))
//                .map( filteredRow -> {
//                    if(localMap.containsKey(filteredRow.getSymbol())) {
//                        localMap.get(filteredRow.getSymbol()).add(filteredRow);
//                    } else {
//                        List<OiDto> list = new ArrayList<>();
//                        list.add(filteredRow);
//                        localMap.put(filteredRow.getSymbol(), list);
//                    }
//                    return true;
//                })
//                .count();
//        return localMap;
//    }

    public static Map<String, List<OiDto>> transformBySymbol(List<OiDto> dtos, Predicate<OiDto> filterPredicate) {
        // aggregate trade by symbols
        // symbol wise trade list
        Map<String, List<OiDto>> localMap = new HashMap<>();
        long rowCount = dtos.stream()
                //.filter( filterPredicate )
                //.filter( passedRow -> "ACC".toUpperCase().equals(passedRow.getSymbol()))
                .map( filteredRow -> {
                    if(localMap.containsKey(filteredRow.getSymbol())) {
                        localMap.get(filteredRow.getSymbol()).add(filteredRow);
                    } else {
                        List<OiDto> list = new ArrayList<>();
                        list.add(filteredRow);
                        localMap.put(filteredRow.getSymbol(), list);
                    }
                    return true;
                })
                .count();
        return localMap;
    }
//    private Map<LocalDate, Map<String, DeliverySpikeDto>> transformByTradeDateAndSymbol(Predicate<DeliverySpikeDto> filterPredicate) {
//        // aggregate trade by symbols
//        // tradeDateAndSymbolWise_DoubleMap
//        Map<LocalDate, Map<String, DeliverySpikeDto>> localMap = new HashMap<>();
//        long rowCount = dbResults.stream()
//                .filter( filterPredicate )
//                .map( filteredRow -> {
//                    if(localMap.containsKey(filteredRow.getTradeDate())) {
//                        if(localMap.get(filteredRow.getTradeDate()).containsKey(filteredRow.getSymbol())) {
//                            LOGGER.warn("tradeDate-symbol | matched tradeDate {} symbol {}", filteredRow.getTradeDate(), filteredRow.getSymbol());
//                        } else {
//                            localMap.get(filteredRow.getTradeDate()).put(filteredRow.getSymbol(), filteredRow);
//                        }
//                    } else {
//                        Map<String, DeliverySpikeDto> map = new HashMap<>();
//                        map.put(filteredRow.getSymbol(), filteredRow);
//                        localMap.put(filteredRow.getTradeDate(), map);
//                        //LOGGER.info("tradeDate-symbol | tradeDate {}", filteredRow.getTradeDate());
//                    }
//                    return true;
//                })
//                .count();
//        return localMap;
//    }

    public static void calculateBells2(DeliverySpikeDto dto) {
        String signal;
        BigDecimal hundred = new BigDecimal(100);

        BigDecimal minPlusThreshHold = new BigDecimal(0.25f);
        BigDecimal plusThreshHold = new BigDecimal(0.55f);
        BigDecimal veryPlusThreshHold = new BigDecimal(0.95f);

        BigDecimal minMinusThreshHold = new BigDecimal(-0.25f);
        BigDecimal minusThreshHold = new BigDecimal(-0.55f);
        BigDecimal veryMinusThreshHold = new BigDecimal(-0.95f);

        if(dto.getLast().compareTo(dto.getClose()) == 1) {
            BigDecimal percent = dto.getClose().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getLast().subtract(dto.getClose());
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.20f)) > 0 ? "bullish" : "";
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.45f)) > 0 ? "Bullish" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.75f)) > 0 ? "BULLISH" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.95f)) > 0 ? "BULLISHhh" : signal;
            dto.setClosingBell(signal);
        } else {
            BigDecimal percent = dto.getLast().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getClose().subtract(dto.getLast()).multiply(new BigDecimal(-1));
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.20f)) < 0 ? "bearish" : "";
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.45f)) < 0 ? "Bearish" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.75f)) < 0 ? "BEARISH" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.95f)) < 0 ? "BEARISHhh" : signal;
            dto.setClosingBell(signal);
        }

        if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
            signal = dto.getCloseToOpenPercent().compareTo(minPlusThreshHold) > 0 ? "gapup" : "";
            signal = dto.getCloseToOpenPercent().compareTo(plusThreshHold) > 0 ? "GapUp" : signal;
            signal = dto.getCloseToOpenPercent().compareTo(veryPlusThreshHold) > 0 ? "GAPUP" : signal;
            dto.setOpeningBell(signal);
        } else {
            signal = dto.getCloseToOpenPercent().compareTo(minMinusThreshHold) < 0 ? "gapdown" : "";
            signal = dto.getCloseToOpenPercent().compareTo(minusThreshHold) < 0 ? "GapDown" : signal;
            signal = dto.getCloseToOpenPercent().compareTo(veryMinusThreshHold) < 0 ? "GAPDOWN" : signal;
            dto.setOpeningBell(signal);
        }
    }




}

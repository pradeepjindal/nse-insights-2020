package org.pra.nse.report;

import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.model.CalcRsiTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class ReportHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportHelper.class);

    public static void enrichRsi(List<CalcRsiTab> oldRsiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setCloseRsi(oldRsi.getCloseRsi10Sma());
                    tdyDto.setLastRsi(oldRsi.getLastRsi10Sma());
                    tdyDto.setAtpRsi(oldRsi.getAtpRsi10Sma());
                    // calculating Bells
                    //calculateBells2(tdyDto);
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichRsi05(List<CalcRsiTab> oldRsiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setCloseRsi(oldRsi.getCloseRsi05Sma());
                    tdyDto.setLastRsi(oldRsi.getLastRsi05Sma());
                    tdyDto.setAtpRsi(oldRsi.getAtpRsi05Sma());
                    // calculating Bells
                    //calculateBells2(tdyDto);
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichRsi10(List<CalcRsiTab> oldRsiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setCloseRsi(oldRsi.getCloseRsi10Sma());
                    tdyDto.setLastRsi(oldRsi.getLastRsi10Sma());
                    tdyDto.setAtpRsi(oldRsi.getAtpRsi10Sma());
                    // calculating Bells
                    //calculateBells2(tdyDto);
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichRsi20(List<CalcRsiTab> oldRsiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setCloseRsi(oldRsi.getCloseRsi20Sma());
                    tdyDto.setLastRsi(oldRsi.getLastRsi20Sma());
                    tdyDto.setAtpRsi(oldRsi.getAtpRsi20Sma());
                    // calculating Bells
                    //calculateBells2(tdyDto);
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }

    public static void enrichMfi(List<CalcMfiTab> oldMfiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldMfiList.forEach( oldMfi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldMfi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).containsKey(oldMfi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).get(oldMfi.getSymbol());
                    tdyDto.setVolAtpMfi(oldMfi.getVolAtpMfi10Sma());
                    tdyDto.setDelAtpMfi(oldMfi.getDelAtpMfi10Sma());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichMfi05(List<CalcMfiTab> oldMfiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldMfiList.forEach( oldMfi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldMfi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).containsKey(oldMfi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).get(oldMfi.getSymbol());
                    tdyDto.setVolAtpMfi(oldMfi.getVolAtpMfi05Sma());
                    tdyDto.setDelAtpMfi(oldMfi.getDelAtpMfi05Sma());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichMfi10(List<CalcMfiTab> oldMfiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldMfiList.forEach( oldMfi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldMfi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).containsKey(oldMfi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).get(oldMfi.getSymbol());
                    tdyDto.setVolAtpMfi(oldMfi.getVolAtpMfi10Sma());
                    tdyDto.setDelAtpMfi(oldMfi.getDelAtpMfi10Sma());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }
    public static void enrichMfi20(List<CalcMfiTab> oldMfiList, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        oldMfiList.forEach( oldMfi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldMfi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).containsKey(oldMfi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).get(oldMfi.getSymbol());
                    tdyDto.setVolAtpMfi(oldMfi.getVolAtpMfi20Sma());
                    tdyDto.setDelAtpMfi(oldMfi.getDelAtpMfi20Sma());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }

    public static void enrichGrowth05(Map<String, CalcAvgTab> calcAvgMap, Map<String, List<DeliverySpikeDto>> symbolMap) {
        BigDecimal hundred = new BigDecimal(100);
        Map.Entry<String, LocalDate> previousDate = new AbstractMap.SimpleEntry<>("tradeDate", LocalDate.now());
        Map.Entry<String, BigDecimal> sumDelivery = new AbstractMap.SimpleEntry<>("sumDelivery", BigDecimal.ZERO);
        symbolMap.entrySet().forEach( entry -> {
            previousDate.setValue(null);
            DeliverySpikeDto firstDto = entry.getValue().get(0);
            BigDecimal atpFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getAtpAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal volFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getVolAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal delFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getDelAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal foiFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getOiAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            //
            sumDelivery.setValue(BigDecimal.ZERO);
            BigDecimal totalExpectedDelivery = calcAvgMap.get(firstDto.getSymbol()).getDelAvg05Sma().multiply(new BigDecimal(entry.getValue().size()));
            BigDecimal onePercentOfExpectedDelivery = totalExpectedDelivery.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            entry.getValue().forEach( dto -> {
                BigDecimal atpDynOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal volDynOnePercent = calcAvgMap.get(dto.getSymbol()).getVolAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal delDynOnePercent = calcAvgMap.get(dto.getSymbol()).getDelAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal foiDynOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg05Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                //
                sumDelivery.setValue(sumDelivery.getValue().add(dto.getDelivery()));

                if(previousDate.getValue() == null || previousDate.getValue().isBefore(dto.getTradeDate())) {
                    previousDate.setValue(dto.getTradeDate());
                    dto.setDelAccumulation(sumDelivery.getValue().divide(onePercentOfExpectedDelivery, 2, RoundingMode.HALF_UP));
                    // fix
                    BigDecimal atpFixGrowth = dto.getAtp().divide(atpFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpFixGrowth(atpFixGrowth);
                    BigDecimal volFixGrowth = dto.getVolume().divide(volFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolFixGrowth(volFixGrowth);
                    BigDecimal delFixGrowth = dto.getDelivery().divide(delFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelFixGrowth(delFixGrowth);
                    BigDecimal foiFixGrowth = dto.getOi().divide(foiFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiFixGrowth(foiFixGrowth);
                    // dyn
                    BigDecimal atpDynGrowth = dto.getAtp().divide(atpDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpDynGrowth(atpDynGrowth);
                    BigDecimal volDynGrowth = dto.getVolume().divide(volDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolDynGrowth(volDynGrowth);
                    BigDecimal delDynGrowth = dto.getDelivery().divide(delDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelDynGrowth(delDynGrowth);
                    BigDecimal foiDynGrowth = dto.getOi().divide(foiDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiDynGrowth(foiDynGrowth);
                } else {
                    LOGGER.warn("enrichCalc | unknown condition - previousDate:{}, currentDate:{}", previousDate.getValue(), dto.getTradeDate());
                }
            });
        });
    }
    public static void enrichGrowth10(Map<String, CalcAvgTab> calcAvgMap, Map<String, List<DeliverySpikeDto>> symbolMap) {
        BigDecimal hundred = new BigDecimal(100);
        Map.Entry<String, LocalDate> previousDate = new AbstractMap.SimpleEntry<>("tradeDate", LocalDate.now());
        Map.Entry<String, BigDecimal> sumDelivery = new AbstractMap.SimpleEntry<>("sumDelivery", BigDecimal.ZERO);
        symbolMap.entrySet().forEach( entry -> {
            previousDate.setValue(null);
            DeliverySpikeDto firstDto = entry.getValue().get(0);
            BigDecimal atpFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getAtpAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal volFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getVolAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal delFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getDelAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal foiFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getOiAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            //
            sumDelivery.setValue(BigDecimal.ZERO);
            BigDecimal totalExpectedDelivery = calcAvgMap.get(firstDto.getSymbol()).getDelAvg10Sma().multiply(new BigDecimal(entry.getValue().size()));
            BigDecimal onePercentOfExpectedDelivery = totalExpectedDelivery.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            entry.getValue().forEach( dto -> {
                BigDecimal atpDynOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal volDynOnePercent = calcAvgMap.get(dto.getSymbol()).getVolAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal delDynOnePercent = calcAvgMap.get(dto.getSymbol()).getDelAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal foiDynOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg10Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                //
                sumDelivery.setValue(sumDelivery.getValue().add(dto.getDelivery()));

                if(previousDate.getValue() == null || previousDate.getValue().isBefore(dto.getTradeDate())) {
                    previousDate.setValue(dto.getTradeDate());
                    dto.setDelAccumulation(sumDelivery.getValue().divide(onePercentOfExpectedDelivery, 2, RoundingMode.HALF_UP));
                    // fix
                    BigDecimal atpFixGrowth = dto.getAtp().divide(atpFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpFixGrowth(atpFixGrowth);
                    BigDecimal volFixGrowth = dto.getVolume().divide(volFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolFixGrowth(volFixGrowth);
                    BigDecimal delFixGrowth = dto.getDelivery().divide(delFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelFixGrowth(delFixGrowth);
                    BigDecimal foiFixGrowth = dto.getOi().divide(foiFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiFixGrowth(foiFixGrowth);
                    // dyn
                    BigDecimal atpDynGrowth = dto.getAtp().divide(atpDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpDynGrowth(atpDynGrowth);
                    BigDecimal volDynGrowth = dto.getVolume().divide(volDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolDynGrowth(volDynGrowth);
                    BigDecimal delDynGrowth = dto.getDelivery().divide(delDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelDynGrowth(delDynGrowth);
                    BigDecimal foiDynGrowth = dto.getOi().divide(foiDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiDynGrowth(foiDynGrowth);
                } else {
                    LOGGER.warn("enrichCalc | unknown condition - previousDate:{}, currentDate:{}", previousDate.getValue(), dto.getTradeDate());
                }
            });
        });
    }
    public static void enrichGrowth20(Map<String, CalcAvgTab> calcAvgMap, Map<String, List<DeliverySpikeDto>> symbolMap) {
        BigDecimal hundred = new BigDecimal(100);
        Map.Entry<String, LocalDate> previousDate = new AbstractMap.SimpleEntry<>("tradeDate", LocalDate.now());
        Map.Entry<String, BigDecimal> sumDelivery = new AbstractMap.SimpleEntry<>("sumDelivery", BigDecimal.ZERO);
        symbolMap.entrySet().forEach( entry -> {
            previousDate.setValue(null);
            DeliverySpikeDto firstDto = entry.getValue().get(0);
            BigDecimal atpFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getAtpAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal volFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getVolAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal delFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getDelAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal foiFixOnePercent = calcAvgMap.get(firstDto.getSymbol()).getOiAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
            //
            sumDelivery.setValue(BigDecimal.ZERO);
            BigDecimal totalExpectedDelivery = calcAvgMap.get(firstDto.getSymbol()).getDelAvg20Sma().multiply(new BigDecimal(entry.getValue().size()));
            BigDecimal onePercentOfExpectedDelivery = totalExpectedDelivery.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            entry.getValue().forEach( dto -> {
                BigDecimal atpDynOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal volDynOnePercent = calcAvgMap.get(dto.getSymbol()).getVolAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal delDynOnePercent = calcAvgMap.get(dto.getSymbol()).getDelAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal foiDynOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg20Sma().divide(hundred, 2, RoundingMode.HALF_UP);
                //
                sumDelivery.setValue(sumDelivery.getValue().add(dto.getDelivery()));

                if(previousDate.getValue() == null || previousDate.getValue().isBefore(dto.getTradeDate())) {
                    previousDate.setValue(dto.getTradeDate());
                    dto.setDelAccumulation(sumDelivery.getValue().divide(onePercentOfExpectedDelivery, 2, RoundingMode.HALF_UP));
                    // fix
                    BigDecimal atpFixGrowth = dto.getAtp().divide(atpFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpFixGrowth(atpFixGrowth);
                    BigDecimal volFixGrowth = dto.getVolume().divide(volFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolFixGrowth(volFixGrowth);
                    BigDecimal delFixGrowth = dto.getDelivery().divide(delFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelFixGrowth(delFixGrowth);
                    BigDecimal foiFixGrowth = dto.getOi().divide(foiFixOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiFixGrowth(foiFixGrowth);
                    // dyn
                    BigDecimal atpDynGrowth = dto.getAtp().divide(atpDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpDynGrowth(atpDynGrowth);
                    BigDecimal volDynGrowth = dto.getVolume().divide(volDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolDynGrowth(volDynGrowth);
                    BigDecimal delDynGrowth = dto.getDelivery().divide(delDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDelDynGrowth(delDynGrowth);
                    BigDecimal foiDynGrowth = dto.getOi().divide(foiDynOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setFoiDynGrowth(foiDynGrowth);
                } else {
                    LOGGER.warn("enrichCalc | unknown condition - previousDate:{}, currentDate:{}", previousDate.getValue(), dto.getTradeDate());
                }
            });
        });
    }

    private void calculateBellsMethodOne(DeliverySpikeDto dto) {
        String signal;
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal minPlusThreshHold = new BigDecimal(0.20f);
        BigDecimal minMinusThreshHold = new BigDecimal(-0.20f);

        if(dto.getLast().compareTo(dto.getClose()) == 1) {
            BigDecimal percent = dto.getClose().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getLast().subtract(dto.getClose());
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minPlusThreshHold) > 0 ? "bullish" : "";
            dto.setClosingBell(signal);
        } else {
            BigDecimal percent = dto.getLast().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getClose().subtract(dto.getLast()).multiply(new BigDecimal(-1));
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minMinusThreshHold) < 0 ? "bearish" : "";
            dto.setClosingBell(signal);
        }

        if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
            signal = dto.getCloseToOpenPercent().compareTo(minPlusThreshHold) > 0 ? "gapUp" : "";
            dto.setOpeningBell(signal);
        } else {
            signal = dto.getCloseToOpenPercent().compareTo(minMinusThreshHold) < 0 ? "gapDown" : "";
            dto.setOpeningBell(signal);
        }
    }

    private void calculateBellsMethodTwo(DeliverySpikeDto dto) {
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

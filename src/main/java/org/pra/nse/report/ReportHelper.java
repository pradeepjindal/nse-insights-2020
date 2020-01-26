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
                    tdyDto.setTdyCloseRsi10Ema(oldRsi.getCloseRsi10Ema());
                    tdyDto.setTdyLastRsi10Ema(oldRsi.getLastRsi10Ema());
                    tdyDto.setTdyAtpRsi10Ema(oldRsi.getAtpRsi10Ema());
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
                    tdyDto.setVolumeAtpMfi10(oldMfi.getVolumeAtpMfi10());
                    tdyDto.setDeliveryAtpMfi10(oldMfi.getDeliveryAtpMfi10());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
    }

    public static void enrichCalc(Map<String, CalcAvgTab> calcAvgMap, Map<String, List<DeliverySpikeDto>> symbolMap) {
        BigDecimal hundred = new BigDecimal(100);
        Map.Entry<String, LocalDate> previousDate = new AbstractMap.SimpleEntry<>("tradeDate", LocalDate.now());
        symbolMap.entrySet().forEach( entry -> {
            previousDate.setValue(null);
            entry.getValue().forEach( dto -> {
                BigDecimal atpOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal volumeOnePercent = calcAvgMap.get(dto.getSymbol()).getVolumeAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal deliveryOnePercent = calcAvgMap.get(dto.getSymbol()).getDeliveryAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal oiOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                if(previousDate.getValue() == null || previousDate.getValue().isBefore(dto.getTradeDate())) {
                    previousDate.setValue(dto.getTradeDate());
                    BigDecimal atpGrowth = dto.getAtp().divide(atpOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setAtpGrowth10(atpGrowth);
                    BigDecimal volumeGrowth = dto.getVolume().divide(volumeOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setVolumeGrowth10(volumeGrowth);
                    BigDecimal deliveryGrowth = dto.getDelivery().divide(deliveryOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setDeliveryGrowth10(deliveryGrowth);
                    BigDecimal oiGrowth = dto.getOi().divide(oiOnePercent, 2, RoundingMode.HALF_UP);
                    dto.setOiGrowth10(oiGrowth);
                } else {
                    LOGGER.warn("unknown condition - previousDate:{}, currentDate:{}", previousDate.getValue(), dto.getTradeDate());
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

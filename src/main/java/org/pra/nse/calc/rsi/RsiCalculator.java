package org.pra.nse.calc.rsi;

import org.pra.nse.db.dto.DeliverySpikeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RsiCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsiCalculator.class);

    public static void calculateSma(List<LocalDate> latestTenDates,
                                String symbol,
                                List<DeliverySpikeDto> deliverySpikeDtoList,
                                Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
        // calculate rsi for each s symbol
        //LOGGER.info("for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal up = BigDecimal.ZERO;
        short upCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;
        short dnCtr = 0;
        DeliverySpikeDto latestDto = null;
        LocalDate latestDate = latestTenDates.get(0);
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(latestDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal rsiColumn = functionSupplier.apply(dsDto);
            if(rsiColumn.compareTo(zero) > 0)  {
                //up = up.add(dsDto.getTdycloseMinusYesclose());
                up = up.add(rsiColumn);
                upCtr++;
            }
            else {
                //dn = dn.add(dsDto.getTdycloseMinusYesclose());
                dn = dn.add(rsiColumn);
                dnCtr++;
            }
        }
        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        up = up.divide(upCtr == 0 ? BigDecimal.ONE : new BigDecimal(upCtr), 2, RoundingMode.HALF_UP);
        dn = dn.divide(dnCtr == 0 ? BigDecimal.ONE : new BigDecimal(dnCtr), 2, RoundingMode.HALF_UP);

        BigDecimal rs = BigDecimal.ZERO;
        if(dn.abs().compareTo(BigDecimal.ZERO) == 0) {
            LOGGER.warn("rsi | {}, all closing are up", symbol);
            rs = up.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
        } else {
            rs = up.divide(dn.abs(), 2, RoundingMode.HALF_UP);
        }


        //rsi = 100 - (100 / (1 + rs));
        //------------------------------------------
        //(1 + rs)
        BigDecimal rsi = rs.add(BigDecimal.ONE);
        //(100 / (1 + rs)
        BigDecimal hundred = new BigDecimal(100);
        rsi = hundred.divide(rsi, 2, RoundingMode.HALF_UP);
        //100 - (100 / (1 + rs))
        rsi = hundred.subtract(rsi);
        //===========================================

        //if(latestDto!=null) latestDto.setTdyCloseRsi10Ema(rsi);
        if(latestDto!=null) biConsumer.accept(latestDto, rsi);
        else LOGGER.warn("skipping rsi, latestDate is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    public static void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }

}

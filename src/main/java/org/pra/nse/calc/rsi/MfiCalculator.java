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

public class MfiCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MfiCalculator.class);

    public static void calculate(List<LocalDate> latestTenDates,
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
        LocalDate latestDate = latestTenDates.get(0);
//        if("EXIDEIND".equals(symbol)) {
//            LOGGER.info("");
//        }
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(latestDate)  == 0) {
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
        else LOGGER.warn("skipping mfi, latestDate is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    public static void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }

}

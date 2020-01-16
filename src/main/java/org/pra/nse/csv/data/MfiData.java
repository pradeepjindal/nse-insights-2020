package org.pra.nse.csv.data;

import org.pra.nse.ApCo;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class MfiData {
    private static final Logger LOGGER = LoggerFactory.getLogger(MfiData.class);


    public static void saveOverWrite(String csvHeaderString,
                                     List<DeliverySpikeDto> dtoHavingMfi,
                                     String toPath,
                                     Function<MfiBean, String> csvStringFunction) {
        //
        List<MfiBean> beans = new ArrayList<>();
        dtoHavingMfi.stream().forEach( dto -> {
            MfiBean bean = new MfiBean();
            bean.setSymbol(dto.getSymbol());
            //rsiBean.setTradeDate(DateUtils.toUtilDate(dto.getTradeDate()));
            bean.setTradeDate(dto.getTradeDate());
            bean.setVolumeAtpMfi10(dto.getVolumeAtpMfi10());
            bean.setDeliveryAtpMfi10(dto.getDeliveryAtpMfi10());
            beans.add(bean);
        });
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        //symbolMap.values().forEach( list -> list.forEach( dto -> csvLines.add(dto.toFullCsvString())));
        beans.forEach( dto -> csvLines.add( csvStringFunction.apply(dto) ));

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(csvHeaderString);
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException("mfi | could not create file: " + toPath);
        }
    }

    public static void saveAppend(List<DeliverySpikeDto> dtoHavingMfi) {

        //List<RsiEntity> rsiBeans0 = RsiData.load();
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + "mfi.csv";
        //
        List<RsiBean> beans = new ArrayList<>();
        dtoHavingMfi.stream().forEach( dto -> {
            RsiBean bean = new RsiBean();
            bean.setSymbol(dto.getSymbol());
            //rsiBean.setTradeDate(DateUtils.toUtilDate(dto.getTradeDate()));
            bean.setTradeDate(dto.getTradeDate());
            bean.setCloseRsi10Ema(dto.getTdyCloseRsi10Ema());
            bean.setLastRsi10Ema(dto.getTdyLastRsi10Ema());
            bean.setAtpRsi10Ema(dto.getTdyAtpRsi10Ema());
            beans.add(bean);
        });

        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        beans.forEach( bean -> csvLines.add(bean.toString()));

        // print csv lines
        File csvOutputFile = new File(toPath);
        //overwrite mode
        //try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
        //append mode
        try (FileWriter fw = new FileWriter(csvOutputFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println("symbol, trade_date, AtpMfi05, AtpMfi10, AtpMfi15, AtpMfi20");
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (IOException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException("mfi: Could not create file");
        }
    }

}

package org.pra.nse.csv.data;

import org.pra.nse.db.dto.DeliverySpikeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class AvgData {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgData.class);


    public static void saveOverWrite(String csvHeaderString,
                                     List<DeliverySpikeDto> dtos,
                                     String toPath,
                                     Function<AvgBean, String> csvStringFunction) {
        //
        List<AvgBean> beans = new ArrayList<>();
        dtos.stream().forEach( dto -> {
            AvgBean bean = new AvgBean();
            bean.setSymbol(dto.getSymbol());
            //rsiBean.setTradeDate(DateUtils.toUtilDate(dto.getTradeDate()));
            bean.setTradeDate(dto.getTradeDate());

            bean.setVolumeAvg10(dto.getVolumeAvg10());
            bean.setDeliveryAvg10(dto.getDeliveryAvg10());
            bean.setOiAvg10(dto.getOiAvg10());

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
            throw new RuntimeException("avg | could not create file: " + toPath);
        }
    }

}

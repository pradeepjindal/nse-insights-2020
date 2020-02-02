package org.pra.nse.csv.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * CAO Csv Access Object
 */
public class AvgCao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCao.class);


    public static void saveOverWrite(String csvHeaderString,
                                     List<AvgBean> dtos,
                                     String toPath,
                                     Function<AvgBean, String> csvStringFunction) {
        //
//        List<AvgBean> beans = new ArrayList<>();
//        dtos.stream().forEach( dto -> {
//            AvgBean bean = new AvgBean();
//            bean.setSymbol(dto.getSymbol());
//            //rsiBean.setTradeDate(DateUtils.toUtilDate(dto.getTradeDate()));
//            bean.setTradeDate(dto.getTradeDate());
//
//            bean.setAtpAvg10(dto.getAtpAvg10());
//            bean.setVolAvg10(dto.getVolAvg10());
//            bean.setDelAvg10(dto.getDelAvg10());
//            bean.setFoiAvg10(dto.getFoiAvg10());
//
//            beans.add(bean);
//        });
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        //symbolMap.values().forEach( list -> list.forEach( dto -> csvLines.add(dto.toFullCsvString())));
        dtos.forEach( dto -> csvLines.add( csvStringFunction.apply(dto) ));

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

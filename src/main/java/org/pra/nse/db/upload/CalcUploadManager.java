package org.pra.nse.db.upload;

import org.pra.nse.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CalcUploadManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcUploadManager.class);

    private final CalcAvgUploader calcAvgUploader;
    private final CalcRsiUploader calcRsiUploader;
    private final CalcMfiUploader calcMfiUploader;

    private final CalcAvgUploaderNew calcAvgUploaderNew;
    private final CalcMfiUploaderNew calcMfiUploaderNew;
    private final CalcRsiUploaderNew calcRsiUploaderNew;

    public CalcUploadManager(CalcAvgUploader calcAvgUploader, CalcRsiUploader calcRsiUploader, CalcMfiUploader calcMfiUploader,
                             CalcAvgUploaderNew calcAvgUploaderNew, CalcMfiUploaderNew calcMfiUploaderNew, CalcRsiUploaderNew calcRsiUploaderNew) {
        this.calcAvgUploader = calcAvgUploader;
        this.calcRsiUploader = calcRsiUploader;
        this.calcMfiUploader = calcMfiUploader;
        this.calcAvgUploaderNew = calcAvgUploaderNew;
        this.calcMfiUploaderNew = calcMfiUploaderNew;
        this.calcRsiUploaderNew = calcRsiUploaderNew;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ CALC - Upload Manager");

        calcAvgUploader.uploadFromLastDate();
        LOGGER.info("----------");
        calcRsiUploader.uploadFromLastDate();
        LOGGER.info("----------");
        calcMfiUploader.uploadFromLastDate();

//        calcAvgUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        calcRsiUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        calcMfiUploader.uploadFromDefaultDate();

        LOGGER.info("----------");
        calcAvgUploaderNew.uploadFromDate(LocalDate.of(2020,2,15));
        calcMfiUploaderNew.uploadFromDate(LocalDate.of(2020,2,15));
        calcRsiUploaderNew.uploadFromDate(LocalDate.of(2020,2,15));

        LOGGER.info("======================================== CALC - Upload Manager");
    }


}

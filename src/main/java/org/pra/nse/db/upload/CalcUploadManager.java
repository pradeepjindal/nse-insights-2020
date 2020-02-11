package org.pra.nse.db.upload;

import org.pra.nse.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalcUploadManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcUploadManager.class);

    private final CalcAvgUploader calcAvgUploader;
    private final CalcRsiUploader calcRsiUploader;
    private final CalcMfiUploader calcMfiUploader;

    public CalcUploadManager(CalcAvgUploader calcAvgUploader, CalcRsiUploader calcRsiUploader, CalcMfiUploader calcMfiUploader) {
        this.calcAvgUploader = calcAvgUploader;
        this.calcRsiUploader = calcRsiUploader;
        this.calcMfiUploader = calcMfiUploader;
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

        LOGGER.info("======================================== CALC - Upload Manager");
    }


}

package org.pra.nse.db.upload;

import org.pra.nse.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalcUploadManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcUploadManager.class);

    private final AvgCalcUploader avgCalcUploader;
    private final RsiCalcUploader rsiCalcUploader;
    private final MfiCalcUploader mfiCalcUploader;

    public CalcUploadManager(AvgCalcUploader avgCalcUploader, RsiCalcUploader rsiCalcUploader, MfiCalcUploader mfiCalcUploader) {
        this.avgCalcUploader = avgCalcUploader;
        this.rsiCalcUploader = rsiCalcUploader;
        this.mfiCalcUploader = mfiCalcUploader;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ CALC - Upload Manager");

        avgCalcUploader.uploadFromLastDate();
        LOGGER.info("----------");
        rsiCalcUploader.uploadFromLastDate();
        LOGGER.info("----------");
        mfiCalcUploader.uploadFromLastDate();

//        avgCalcUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        rsiCalcUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        mfiCalcUploader.uploadFromDefaultDate();

        LOGGER.info("======================================== CALC - Upload Manager");
    }


}

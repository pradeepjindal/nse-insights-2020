package org.pra.nse.db.upload;

import org.pra.nse.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NseUploadManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(NseUploadManager.class);

    private final CashMarketUploader cashMarketUploader;
    private final FutureMarketUploader futureMarketUploader;
    private final DeliveryMarketUploader deliveryMarketUploader;

    public NseUploadManager(CashMarketUploader cashMarketUploader,
                            FutureMarketUploader futureMarketUploader,
                            DeliveryMarketUploader deliveryMarketUploader) {
        this.cashMarketUploader = cashMarketUploader;
        this.futureMarketUploader = futureMarketUploader;
        this.deliveryMarketUploader = deliveryMarketUploader;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ NSE - Upload Manager");

        cashMarketUploader.uploadFromLastDate();
        LOGGER.info("----------");
        futureMarketUploader.uploadFromLastDate();
        LOGGER.info("----------");
        deliveryMarketUploader.uploadFromLastDate();

//        cashMarketUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        futureMarketUploader.uploadFromDefaultDate();
//        LOGGER.info("----------");
//        deliveryMarketUploader.uploadFromDefaultDate();

        LOGGER.info("======================================== NSE - Upload Manager");
    }


}
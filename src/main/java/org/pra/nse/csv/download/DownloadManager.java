package org.pra.nse.csv.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private final CmDownloader cmDownloader;
    private final FmDownloader fmDownloader;
    private final DmDownloader dmDownloader;
    private final BpDownloader bpDownloader;
    private final DbcDownloader dbcDownloader;

    public DownloadManager(CmDownloader cmDownloader,
                           FmDownloader fmDownloader,
                           DmDownloader dmDownloader,
                           BpDownloader bpDownloader,
                           DbcDownloader dbcDownloader) {
        this.cmDownloader = cmDownloader;
        this.fmDownloader = fmDownloader;
        this.dmDownloader = dmDownloader;
        this.bpDownloader = bpDownloader;
        this.dbcDownloader = dbcDownloader;
    }

    public void download() {
        LOGGER.info(".");
        LOGGER.info("____________________ Download Manager");

        cmDownloader.downloadFromLastDate();
        LOGGER.info("----------");
        fmDownloader.downloadFromLastDate();
        LOGGER.info("----------");
        dmDownloader.downloadFromLastDate();
        LOGGER.info("----------");
        bpDownloader.downloadFromLastDate();
        LOGGER.info("----------");
        dbcDownloader.downloadFromLastDate();

//        cmDownloader.downloadFromDefaultDate();
//        LOGGER.info("----------");
//        fmDownloader.downloadFromDefaultDate();
//        LOGGER.info("----------");
//        dmDownloader.downloadFromDefaultDate();
//        LOGGER.info("----------");
//        bpDownloader.downloadFromDefaultDate();

        LOGGER.info("======================================== Download Manager");
    }

}

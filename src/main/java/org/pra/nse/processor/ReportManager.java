package org.pra.nse.processor;

import org.pra.nse.ApCo;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ReportManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportManager.class);

    private final NseFileUtils nseFileUtils;

    private final PradeepProcessor pradeepProcessor;
    private final ManishProcessor manishProcessor;
    private final ManishProcessorB manishProcessorB;

    private final DeliverySpikeReport deliverySpikeReport;
    private final DeliverySpikeReportFull deliverySpikeReportFull;
    private final PastPresentFutureReport pastPresentFutureReport;

    public ReportManager(NseFileUtils nseFileUtils,
                         PradeepProcessor pradeepProcessor,
                         ManishProcessor manishProcessor,
                         ManishProcessorB manishProcessorB,
                         DeliverySpikeReport deliverySpikeReport,
                         DeliverySpikeReportFull deliverySpikeReportFull,
                         PastPresentFutureReport pastPresentFutureReport) {
        this.nseFileUtils = nseFileUtils;
        this.pradeepProcessor = pradeepProcessor;
        this.manishProcessor = manishProcessor;
        this.manishProcessorB = manishProcessorB;
        this.deliverySpikeReport = deliverySpikeReport;
        this.deliverySpikeReportFull = deliverySpikeReportFull;
        this.pastPresentFutureReport = pastPresentFutureReport;
    }

    public void report() {
        LOGGER.info(".");
        LOGGER.info("==================== Report Manager");

        nseFileUtils.getFilesToBeComputed(ApCo.DOWNLOAD_FROM_DATE, ()-> ApCo.PRADEEP_FILE_NAME)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", ApCo.PRADEEP_FILE_NAME, forDate.toString());
                    try {
                        pradeepProcessor.process(forDate);
                        //pradeepProcessorB.process(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("--------------------");
        nseFileUtils.getFilesToBeComputed(ApCo.DOWNLOAD_FROM_DATE, ()-> ApCo.MANISH_FILE_NAME)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", ApCo.MANISH_FILE_NAME, forDate.toString());
                    try {
                        manishProcessor.process(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("--------------------");
        nseFileUtils.getFilesToBeComputed(ApCo.DOWNLOAD_FROM_DATE, ()-> ApCo.MANISH_FILE_NAME_B)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", ApCo.MANISH_FILE_NAME_B, forDate.toString());
                    try {
                        manishProcessorB.process(ApCo.MANISH_FILE_NAME_B, forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("--------------------");
        deliverySpikeReport.process();

        LOGGER.info("--------------------");
        deliverySpikeReportFull.process();

        LOGGER.info("--------------------");
        //pastPresentFutureReport.process();
        nseFileUtils.getFilesToBeComputed(ApCo.COMPUTE_DIR_NAME, ApCo.DOWNLOAD_FROM_DATE, ()-> "mfi")
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", "mfi", forDate.toString());
                    try {
                        pastPresentFutureReport.process(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("==================== Report Manager");
    }

}

package org.pra.nse.report;

import org.pra.nse.ApCo;
import org.pra.nse.Manager;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * polymorphism is not letting me share the data in base class
 * inheritance letting me share the common code, constants but not the date from base
 * in any case i have to make 3 objects means 3 copies of data means data duplication
 * if i want to keep the single copy of data, it seems only composition is the way to go
 * what i mean to say, i can not do this:
 * > created the object maruti and filled the petrol
 * > run maruti a little, now i want this to be transformed into wagonR (it wont transform, that is another issue)
 * oops! i can not do that, i have to create a separate wagonR and fill the petrol again
 * there is not way i can use maruti petrol in wagonR while both are child of same FourWheeler class
 * only composition is a way to do so (by oop it can not be achieved)
 *
 * oop do polymorphism (via different instances)
 * composition can do transformation (can operate different instances on the same data)
 *
 * FourWheeler -> Maruti
 * FourWheeler -> WagonR
 *
 * Morpheus :: Maruti :: WagonR
 * Morpheus(Maruit)
 * Morpheus.fillPetrol
 * Morpheus.run
 * Morpheus.transform(WagonR)
 * Morpheus.run
 */

@Component
public class ReportManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportManager.class);

    private final NseFileUtils nseFileUtils;

    private final NseReportsDao nseReportsDao;

    private final DeliverySpikeReporter deliverySpikeReport;
    private final DeliverySpikeReporterFull deliverySpikeReportFull;
    private final PastPresentFutureReporter pastPresentFutureReport;

    public ReportManager(NseFileUtils nseFileUtils,
                         NseReportsDao nseReportsDao, DeliverySpikeReporter deliverySpikeReport,
                         DeliverySpikeReporterFull deliverySpikeReportFull,
                         PastPresentFutureReporter pastPresentFutureReport) {
        this.nseFileUtils = nseFileUtils;
        this.nseReportsDao = nseReportsDao;
        this.deliverySpikeReport = deliverySpikeReport;
        this.deliverySpikeReportFull = deliverySpikeReportFull;
        this.pastPresentFutureReport = pastPresentFutureReport;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ Report Manager");

//        LOGGER.info("--------------------");
//        deliverySpikeReport.reportFromLast();

        LOGGER.info("--------------------");
        nseFileUtils.getDatesToBeComputed(()-> ReportConstants.DSRF, ApCo.REPORTS_DIR_NAME_DSR, ApCo.REPORTS_FROM_DATE)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", ReportConstants.DSRF, forDate.toString());
                    try {
                        deliverySpikeReportFull.reportForDate(forDate, 10);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

//        LOGGER.info("--------------------");
//        nseFileUtils.getDatesToBeComputed(()-> ReportConstants.PPF_10, ApCo.REPORTS_DIR_NAME_TMP, ApCo.REPORTS_FROM_DATE)
//                .forEach( forDate -> {
//                    LOGGER.info(".");
//                    LOGGER.info("report-{} | for:{}", ReportConstants.PPF_10, forDate.toString());
//                    try {
//                        //pastPresentFutureReport.reportForDate(forDate, 10);
//                        pastPresentFutureReport.reportFromLast(10);
//                    } catch (Exception e) {
//                        LOGGER.error("ERROR: {}", e);
//                    }
//                });

        LOGGER.info("--------------------");
        //pastPresentFutureReport.process();
//        nseFileUtils.getDatesToBeComputed(()-> ReportConstants.PPF_20, ApCo.REPORTS_DIR_NAME_TMP, ApCo.REPORTS_FROM_DATE)
//                .forEach( forDate -> {
//                    LOGGER.info(".");
//                    LOGGER.info("report-{} | for:{}", ReportConstants.PPF_20, forDate.toString());
//                    try {
//                        pastPresentFutureReport.reportForDate(forDate, 20);
//                    } catch (Exception e) {
//                        LOGGER.error("ERROR: {}", e);
//                    }
//                });
        pastPresentFutureReport.reportFromLast(5);
        pastPresentFutureReport.reportFromLast(10);
        //pastPresentFutureReport.reportFromLast(15);
        pastPresentFutureReport.reportFromLast(20);

        LOGGER.info("======================================== Report Manager");
    }

}

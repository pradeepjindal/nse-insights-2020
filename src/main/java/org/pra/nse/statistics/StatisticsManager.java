package org.pra.nse.statistics;

import org.pra.nse.ApCo;
import org.pra.nse.Manager;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.report.DeliverySpikeReporter;
import org.pra.nse.report.DeliverySpikeReporterFull;
import org.pra.nse.report.PastPresentFutureReporter;
import org.pra.nse.report.ReportConstants;
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
public class StatisticsManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsManager.class);

    private final NseFileUtils nseFileUtils;

    private final NseReportsDao nseReportsDao;

    private final DeliverySpikeReporter deliverySpikeReport;
    private final DeliverySpikeReporterFull deliverySpikeReportFull;
    private final PastPresentFutureReporter pastPresentFutureReport;

    private final Statisian statisian;

    public StatisticsManager(NseFileUtils nseFileUtils,
                             NseReportsDao nseReportsDao, DeliverySpikeReporter deliverySpikeReport,
                             DeliverySpikeReporterFull deliverySpikeReportFull,
                             PastPresentFutureReporter pastPresentFutureReport, Statisian statisian) {
        this.nseFileUtils = nseFileUtils;
        this.nseReportsDao = nseReportsDao;
        this.deliverySpikeReport = deliverySpikeReport;
        this.deliverySpikeReportFull = deliverySpikeReportFull;
        this.pastPresentFutureReport = pastPresentFutureReport;
        this.statisian = statisian;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ Statistics Manager");

        statisian.stats(LocalDate.of(2020, 2, 5), 10);

        LOGGER.info("======================================== Statistics Manager");
    }

}

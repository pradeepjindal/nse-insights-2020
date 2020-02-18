package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.Manager;
import org.pra.nse.service.DataService;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.pra.nse.calculation.CalcCons.RSI_FILE_PREFIX;
import static org.pra.nse.calculation.CalcCons.MFI_FILE_PREFIX;
import static org.pra.nse.calculation.CalcCons.AVG_FILE_PREFIX;

@Component
public class CalculationManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationManager.class);

    private final NseFileUtils nseFileUtils;
    private final NseReportsDao nseReportsDao;

    private final DataService dataService;

    private final AvgCalculator avgCalculator;
    private final RsiCalculator rsiCalculator;
    private final MfiCalculator mfiCalculator;

    private final AvgCalculatorNew avgCalculatorNew;
    private final MfiCalculatorNew mfiCalculatorNew;
    private final RsiCalculatorNew rsiCalculatorNew;


    public CalculationManager(NseFileUtils nseFileUtils, NseReportsDao nseReportsDao,
                              DataService dataService,
                              AvgCalculator avgCalculator, RsiCalculator rsiCalculator, MfiCalculator mfiCalculator, AvgCalculatorNew avgCalculatorNew, MfiCalculatorNew mfiCalculatorNew, RsiCalculatorNew rsiCalculatorNew) {
        this.nseFileUtils = nseFileUtils;
        this.nseReportsDao = nseReportsDao;
        this.dataService = dataService;
        this.avgCalculator = avgCalculator;
        this.rsiCalculator = rsiCalculator;
        this.mfiCalculator = mfiCalculator;
        this.avgCalculatorNew = avgCalculatorNew;
        this.mfiCalculatorNew = mfiCalculatorNew;
        this.rsiCalculatorNew = rsiCalculatorNew;
    }

    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ Calculation Manager");

        //Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getLatest10DataBySymbol();

        nseFileUtils.getDatesToBeComputed(()-> AVG_FILE_PREFIX, ApCo.AVG_DIR_NAME, ApCo.CALC_FROM_DATE)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", AVG_FILE_PREFIX, forDate.toString());
                    try {
                        avgCalculator.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> MFI_FILE_PREFIX, ApCo.MFI_DIR_NAME, ApCo.CALC_FROM_DATE)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", MFI_FILE_PREFIX, forDate.toString());
                    try {
                        mfiCalculator.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> RSI_FILE_PREFIX, ApCo.RSI_DIR_NAME, ApCo.CALC_FROM_DATE)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", RSI_FILE_PREFIX, forDate.toString());
                    try {
                        rsiCalculator.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("======================================== Calculation Manager");
        //rsiCalculator.calculateAndReturn(LocalDate.of(2020,02,10));
        //rsiCalculator.calculateAndReturn(LocalDate.of(2020,02,10), "BAJAJ-AUTO");

        //mfiCalculatorNew.calculateAndReturn(LocalDate.of(2020,02,14));
        //mfiCalculatorNew.calculateAndSave(LocalDate.of(2020,2,14));

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> AVG_FILE_PREFIX, CalcCons.AVG_DIR_NAME_NEW, LocalDate.of(2020,2,15))
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", AVG_FILE_PREFIX, forDate.toString());
                    try {
                        avgCalculatorNew.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> MFI_FILE_PREFIX, CalcCons.MFI_DIR_NAME_NEW, LocalDate.of(2020,2,15))
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", MFI_FILE_PREFIX, forDate.toString());
                    try {
                        mfiCalculatorNew.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> RSI_FILE_PREFIX, CalcCons.RSI_DIR_NAME_NEW, LocalDate.of(2020,2,15))
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("calc-{} | for:{}", RSI_FILE_PREFIX, forDate.toString());
                    try {
                        rsiCalculatorNew.calculateAndSave(forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });


    }
}

package org.pra.nse.processor;

import org.pra.nse.ApCo;
import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ProcessManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessManager.class);

    private final NseFileUtils nseFileUtils;

    private final PradeepProcessor pradeepProcessor;
    private final ManishProcessor manishProcessor;
    private final ManishProcessorB manishProcessorB;

    public ProcessManager(NseFileUtils nseFileUtils,
                          PradeepProcessor pradeepProcessor,
                          ManishProcessor manishProcessor,
                          ManishProcessorB manishProcessorB) {
        this.nseFileUtils = nseFileUtils;
        this.pradeepProcessor = pradeepProcessor;
        this.manishProcessor = manishProcessor;
        this.manishProcessorB = manishProcessorB;
    }

    public void process() {
        LOGGER.info(".");
        LOGGER.info("____________________ Process Manager");

        //nseFileUtils.getDatesToBeComputed(()-> ApCo.PRADEEP_FILE_NAME), ApCo.DOWNLOAD_FROM_DATE)
        nseFileUtils.getDatesToBeComputed(()-> ApCo.PRADEEP_FILE_NAME)
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

//        LOGGER.info("----------");
//        nseFileUtils.getFilesToBeComputed(()-> ApCo.MANISH_FILE_NAME)
//                .forEach( forDate -> {
//                    LOGGER.info(".");
//                    LOGGER.info("report-{} | for:{}", ApCo.MANISH_FILE_NAME, forDate.toString());
//                    try {
//                        manishProcessor.process(forDate);
//                    } catch (Exception e) {
//                        LOGGER.error("ERROR: {}", e);
//                    }
//                });

        LOGGER.info("----------");
        nseFileUtils.getDatesToBeComputed(()-> ApCo.MANISH_FILE_NAME_B)
                .forEach( forDate -> {
                    LOGGER.info(".");
                    LOGGER.info("report-{} | for:{}", ApCo.MANISH_FILE_NAME_B, forDate.toString());
                    try {
                        manishProcessorB.process(ApCo.MANISH_FILE_NAME_B, forDate);
                    } catch (Exception e) {
                        LOGGER.error("ERROR: {}", e);
                    }
                });


        LOGGER.info("======================================== Process Manager");
    }

}

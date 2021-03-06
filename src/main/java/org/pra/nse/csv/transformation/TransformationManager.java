package org.pra.nse.csv.transformation;

import org.pra.nse.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransformationManager implements Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformationManager.class);

    private final CmTransformer cmTransformer;
    private final FmTransformer fmTransformer;
    private final DmTransformer dmTransformer;
    private final AbTransformer abTransformer;

    public TransformationManager(CmTransformer cmTransformer,
                                 FmTransformer fmTransformer,
                                 DmTransformer dmTransformer,
                                 AbTransformer abTransformer) {
        this.cmTransformer = cmTransformer;
        this.fmTransformer = fmTransformer;
        this.dmTransformer = dmTransformer;
        this.abTransformer = abTransformer;
    }


    @Override
    public void execute() {
        LOGGER.info(".");
        LOGGER.info("____________________ Transform Manager");

        cmTransformer.transformFromLastDate();
        LOGGER.info("----------");
        abTransformer.transformFromLastDate();
        LOGGER.info("----------");
        fmTransformer.transformFromLastDate();
        LOGGER.info("----------");
        dmTransformer.transformFromLastDate();

//        cmTransformer.transformFromDefaultDate();
//        LOGGER.info("----------");
//        fmTransformer.transformFromDefaultDate();
//        LOGGER.info("----------");
//        dmTransformer.transformFromDefaultDate();
//        LOGGER.info("----------");
//        abTransformer.transformFromDefaultDate();

//        cmTransformer.transformFromDate(LocalDate.of(2017,1,1));
//        LOGGER.info("----------");
//        dmTransformer.transformFromDate(LocalDate.of(2017,1,1));

        LOGGER.info("======================================== Transform Manager");
    }
}

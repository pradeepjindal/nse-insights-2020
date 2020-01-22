package org.pra.nse.csv.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransformationManager {
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


    public void transform() {
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

        LOGGER.info("======================================== Transform Manager");
    }
}

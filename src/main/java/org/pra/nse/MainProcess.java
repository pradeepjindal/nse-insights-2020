package org.pra.nse;

import org.pra.nse.calculation.CalculationManager;
import org.pra.nse.csv.download.DownloadManager;
import org.pra.nse.csv.transformation.TransformationManager;
import org.pra.nse.db.upload.UploadManager;
import org.pra.nse.processor.*;
import org.pra.nse.report.ReportManager;
import org.pra.nse.util.DirUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MainProcess implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainProcess.class);

    private final PraFileUtils praFileUtils;

    private final DownloadManager downloadManager;
    private final TransformationManager transformationManager;
    private final UploadManager uploadManager;
    private final CalculationManager calculationManager;
    private final ProcessManager processManager;
    private final ReportManager reportManager;

    public MainProcess(PraFileUtils praFileUtils,
                       DownloadManager downloadManager,
                       TransformationManager transformationManager,
                       UploadManager uploadManager,
                       CalculationManager calculationManager,
                       ProcessManager processManager,
                       ReportManager reportManager) {
        this.praFileUtils = praFileUtils;
        this.downloadManager = downloadManager;
        this.transformationManager = transformationManager;
        this.uploadManager = uploadManager;
        this.calculationManager = calculationManager;
        this.processManager = processManager;
        this.reportManager = reportManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("");
        LOGGER.info("Main Process | ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ | commencing");
        LOGGER.info("");
        try {
            DirUtils.createRootFolder();
            downloadManager.execute();
            transformationManager.execute();
            uploadManager.execute();
            if(praFileUtils.validateDownload() != null) {
                calculationManager.execute();
                processManager.execute();
                reportManager.execute();
            }
        } catch(Exception e) {
            LOGGER.error("ERROR: {}", e);
        }
        LOGGER.info("");
        LOGGER.info("Main Process | ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ | finishing");
        LOGGER.info("");
    }

}

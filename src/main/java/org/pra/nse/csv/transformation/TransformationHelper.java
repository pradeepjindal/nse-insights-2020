package org.pra.nse.csv.transformation;

import org.pra.nse.util.NseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class TransformationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformationHelper.class);

    private final NseFileUtils nseFileUtils;

    public TransformationHelper(NseFileUtils nseFileUtils) {
        this.nseFileUtils = nseFileUtils;
    }

    void transform(String dataDir, String filePrefix, String nseFileName, String praFileName) {
        String source = dataDir + File.separator + nseFileName;
        String target = dataDir + File.separator + praFileName;
        if(nseFileUtils.isFileExist(target)) {
            LOGGER.info("{} file already transformed - {}", filePrefix, target);
        } else if (nseFileUtils.isFileExist(source)) {
            try {
                nseFileUtils.unzipNew(source, filePrefix);
                LOGGER.info("{} file transformed - {}", filePrefix, target);
            } catch (FileNotFoundException fnfe) {
                LOGGER.info("{} file not found - {}", filePrefix, source);
            } catch (IOException e) {
                LOGGER.warn("Error while unzipping file: {}", e);
            }
        }
    }
}

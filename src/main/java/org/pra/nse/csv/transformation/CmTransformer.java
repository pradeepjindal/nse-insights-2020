package org.pra.nse.csv.transformation;

import org.pra.nse.ApCo;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CmTransformer extends BaseTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmTransformer .class);

    private final String Data_Dir = ApCo.ROOT_DIR + File.separator + ApCo.CM_DIR_NAME;


    public CmTransformer(TransformationHelper transformationHelper, NseFileUtils nseFileUtils, PraFileUtils praFileUtils) {
        super(transformationHelper, nseFileUtils, praFileUtils);
    }


    public void transformFromDefaultDate() {
        transformFromDate(ApCo.DOWNLOAD_FROM_DATE);
    }
    public void transformFromDate(LocalDate fromDate) {
        Map<String, String> filePairMap = prepare(fromDate);
        looper(filePairMap);
    }

    public void transformFromLastDate() {
        String str = praFileUtils.getLatestFileNameFor(Data_Dir, ApCo.PRA_CM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        LocalDate dateOfLatestFile = DateUtils.getLocalDateFromPath(str);
        Map<String, String> filePairMap = prepare(dateOfLatestFile);
        looper(filePairMap);
    }


    private Map<String, String> prepare(LocalDate fromDate) {
        List<String> fileNames = nseFileUtils.constructFileNames(
                                    fromDate,
                                    ApCo.NSE_CM_FILE_NAME_DATE_FORMAT,
                                    ApCo.NSE_CM_FILE_PREFIX,
                                    ApCo.NSE_CM_FILE_SUFFIX + ApCo.NSE_CM_FILE_EXT);
        //filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(dataDir, null, null));
        //
        Map<String, String> filePairMap = new HashMap<>();
        fileNames.forEach( sourceFileName -> {
            LOGGER.info("{}", sourceFileName);
            //DateUtils.extractDate(fileName, ApCo.NSE_CM_FILE_NAME_DATE_REGEX, ApCo.NSE_CM_FILE_NAME_DATE_FORMAT);
            LocalDate localDate = DateUtils.getLocalDateFromPath(sourceFileName, ApCo.NSE_CM_FILE_NAME_DATE_REGEX, ApCo.NSE_CM_FILE_NAME_DATE_FORMAT);
            String targetFileName = ApCo.PRA_CM_FILE_PREFIX + localDate.toString() + ApCo.REPORTS_FILE_EXT;
            filePairMap.put(sourceFileName, targetFileName);
        });
        return filePairMap;
    }

    private void looper(Map<String, String> filePairMap) {
        filePairMap.forEach( (nseFileName, praFileName) -> {
            transformationHelper.transform(Data_Dir, ApCo.PRA_CM_FILE_PREFIX, nseFileName, praFileName);
        });
    }

}

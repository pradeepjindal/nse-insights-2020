package org.pra.nse.csv.transform;

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
public class FmTransformer extends BaseTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FmTransformer.class);

    private final String Data_Dir = ApCo.ROOT_DIR + File.separator + ApCo.FM_DIR_NAME;


    public FmTransformer(TransformHelper transformHelper, NseFileUtils nseFileUtils, PraFileUtils praFileUtils) {
        super(transformHelper, nseFileUtils, praFileUtils);
    }


    public void transformFromDate() {
        transformFromDate(ApCo.DOWNLOAD_FROM_DATE);
    }
    public void transformFromDate(LocalDate fromDate) {
        Map<String, String> filePairMap = prepare(fromDate);
        looper(filePairMap);
    }

    public void transformFromLastDate() {
        String str = praFileUtils.getLatestFileNameFor(Data_Dir, ApCo.PRA_FM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        LocalDate dateOfLatestFile = DateUtils.getLocalDateFromPath(str);
        Map<String, String> filePairMap = prepare(dateOfLatestFile);
        looper(filePairMap);
    }


    private Map<String, String> prepare(LocalDate fromDate) {
        List<String> fileNames = nseFileUtils.constructFileNames(
                            fromDate,
                            ApCo.NSE_FM_FILE_NAME_DATE_FORMAT,
                            ApCo.NSE_FM_FILE_PREFIX,
                            ApCo.NSE_FM_FILE_SUFFIX + ApCo.NSE_FM_FILE_EXT);
        //filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(dataDir, null, null));
        //
        Map<String, String> filePairMap = new HashMap<>();
        fileNames.forEach( sourceFileName -> {
            LOGGER.info("{}", sourceFileName);
            //DateUtils.extractDate(fileName, ApCo.NSE_FM_FILE_NAME_DATE_REGEX, ApCo.NSE_FM_FILE_NAME_DATE_FORMAT);
            LocalDate localDate = DateUtils.getLocalDateFromPath(sourceFileName, ApCo.NSE_FM_FILE_NAME_DATE_REGEX, ApCo.NSE_FM_FILE_NAME_DATE_FORMAT);
            String targetFileName = ApCo.PRA_FM_FILE_PREFIX + localDate.toString() + ApCo.REPORTS_FILE_EXT;
            filePairMap.put(sourceFileName, targetFileName);
        });
        return filePairMap;
    }

    private void looper(Map<String, String> filePairMap) {
        filePairMap.forEach( (nseFileName, praFileName) -> {
            //TODO - block transforming of 28-Aug-2019 file
            transformHelper.transform(Data_Dir, ApCo.PRA_FM_FILE_PREFIX, nseFileName, praFileName);
        });
    }

}

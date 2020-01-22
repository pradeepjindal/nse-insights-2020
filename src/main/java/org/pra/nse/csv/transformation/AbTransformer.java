package org.pra.nse.csv.transformation;

import org.pra.nse.ApCo;
import org.pra.nse.email.EmailService;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


/**
 * Ami Broker Transformer
 */
@Component
public class AbTransformer extends BaseTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbTransformer.class);

    private final String Data_Dir = ApCo.ROOT_DIR + File.separator + ApCo.AB_DIR_NAME;

    private final EmailService emailService;

    public AbTransformer(TransformationHelper transformationHelper, NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                         EmailService emailService) {
        super(transformationHelper, nseFileUtils, praFileUtils);
        this.emailService = emailService;
    }


    public void transformFromDefaultDate() {
        transformFromDate(ApCo.DOWNLOAD_FROM_DATE);
    }
    public void transformFromDate(LocalDate fromDate) {
        Map<String, String> filePairMap = prepare(fromDate);
        looper(filePairMap);
    }

    public void transformFromLastDate() {
        String str = praFileUtils.getLatestFileNameFor(Data_Dir, ApCo.AB_FILE_PREFIX, ApCo.AB_FILE_EXT,  1,
                LocalDate.now(), ApCo.DOWNLOAD_FROM_DATE, ApCo.AB_FILE_NAME_DTF);
        LocalDate dateOfLatestFile = DateUtils.getLocalDateFromPath(str, ApCo.AB_FILE_NAME_DATE_REGEX, ApCo.AB_FILE_NAME_DATE_FORMAT);
        Map<String, String> filePairMap = prepare(dateOfLatestFile);
        looper(filePairMap);
    }


    private Map<String, String> prepare(LocalDate fromDate) {
        List<String> sourceFileNames = nseFileUtils.constructFileNames(
                                    fromDate,
                                    ApCo.PRA_FILE_NAME_DATE_FORMAT,
                                    ApCo.PRA_CM_FILE_PREFIX,
                                    ApCo.DATA_FILE_EXT);
        //filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(dataDir, null, null));
        //
        Map<String, String> sourceAndTargetFilePairs = new HashMap<>();
        sourceFileNames.forEach( sourceFileName -> {
            LOGGER.info("{}", sourceFileName);
            //DateUtils.extractDate(fileName, ApCo.NSE_CM_FILE_NAME_DATE_REGEX, ApCo.NSE_CM_FILE_NAME_DATE_FORMAT);
            LocalDate localDate = DateUtils.getLocalDateFromPath(sourceFileName, ApCo.DATA_FILE_NAME_DATE_REGEX, ApCo.DATA_FILE_NAME_DATE_FORMAT);
            String targetFileName = ApCo.AB_FILE_PREFIX + ApCo.ddMMMyyyy_DTF.format(localDate) + ApCo.AB_FILE_EXT;
            sourceAndTargetFilePairs.put(sourceFileName, targetFileName);
        });
        return sourceAndTargetFilePairs;
    }

    private void looper(Map<String, String> filePairMap) {
        filePairMap.forEach( (nseFileName, abFileName) -> {
            transform(nseFileName, abFileName);
        });
    }

    private void transform(String nseFileName, String abFileName) {
        String source = ApCo.ROOT_DIR + File.separator + ApCo.CM_DIR_NAME + File.separator + nseFileName;
        String target = Data_Dir + File.separator + abFileName;
        if(nseFileUtils.isFileExist(target)) {
            LOGGER.info("AB | target exists - {}", target);
        } else if (nseFileUtils.isFileExist(source)) {
            try {
                transformToAbCsv(source, target);
                LOGGER.info("AB | source transformed to - {}", target);
                email(target);
            } catch (Exception e) {
                LOGGER.warn("AB | Error while transforming file: {} {}", source, e);
            }
        } else {
            LOGGER.info("AB | source not found - {}", source);
        }
    }

    private void transformToAbCsv(String source, String target) {
        AtomicInteger atomicInteger = new AtomicInteger();
        File csvOutputFile = new File(target);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            try (Stream<String> stream = Files.lines(Paths.get(source))) {
                stream
                        .filter( line-> {
                            String [] splittedLine = line.split(",");
                            if("EQ".equals(splittedLine[1])) return true;
                            return false;
                        })
                        .map( filteredLine -> {
                            String [] splitted_cm_row = filteredLine.split(",");
                            LocalDate tradeDate = DateUtils.toLocalDate(splitted_cm_row[10], ApCo.PRA_CM_DATA_DATE_FORMAT);
                            String abRow = splitted_cm_row[0]
                                            + "," + tradeDate.format(ApCo.AB_DATA_DTF)
                                            + "," + splitted_cm_row[2]
                                            + "," + splitted_cm_row[3]
                                            + "," + splitted_cm_row[4]
                                            + "," + splitted_cm_row[5]
                                            + "," + splitted_cm_row[8]
                                            + "," + "0";
                            atomicInteger.incrementAndGet();
                            return abRow;
                        })
                        .forEach(pw::println);
                LOGGER.info("AB | total {} rows printed", atomicInteger.get());
            } catch (IOException e) {
                LOGGER.warn("Error in CM entry: {}", e);
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn("Error: {}", e);
        }
    }

    private void email(String pathToAttachment) {
        String fileName = pathToAttachment.substring(pathToAttachment.indexOf("EQ_"));
        if(nseFileUtils.isFileExist(pathToAttachment)) {
            emailService.sendAttachmentMessage("ca.manish.thakkar@gmail.com", fileName, fileName, pathToAttachment, null);
            LOGGER.info(".");
            emailService.sendAttachmentMessage("pradeepjindal.mca@gmail.com", fileName, fileName, pathToAttachment, null);
        } else {
            LOGGER.error("skipping email: AmiBroker file not found at disk");
        }
    }
}
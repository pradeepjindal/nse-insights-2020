package org.pra.nse.db.upload;

import org.pra.nse.ApCo;
import org.pra.nse.calculation.MfiCalculator;
import org.pra.nse.calculation.CalcCons;
import org.pra.nse.csv.data.MfiBean;
import org.pra.nse.db.dao.calc.MfiCalculationDao;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.repository.CalcMfiRepository;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CalcMfiUploader extends BaseUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcMfiUploader.class);

    private final String calc_name = CalcCons.MFI_DATA_NAME;

    private final MfiCalculationDao dao;
    private final CalcMfiRepository repo;
    private final MfiCalculator calculator;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    public CalcMfiUploader(MfiCalculationDao dao, CalcMfiRepository repo, MfiCalculator calculator,
                           NseFileUtils nseFileUtils, PraFileUtils praFileUtils) {
        super(praFileUtils, CalcCons.MFI_DIR_NAME, CalcCons.MFI_FILE_PREFIX);
        this.dao = dao;
        this.repo = repo;
        this.calculator = calculator;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
    }


    public void uploadForDate(LocalDate forDate) {
        //
        String fileName = CalcCons.MFI_FILE_PREFIX +forDate+ ApCo.DATA_FILE_EXT;
        String fromFile = CalcCons.MFI_FILES_PATH +File.separator+ fileName;
        LOGGER.info("{} upload | looking for file Name along with path:[{}]",calc_name, fromFile);

        if(!nseFileUtils.isFileExist(fromFile)) {
            LOGGER.warn("{} upload | file does not exist: [{}]", calc_name, fromFile);
            return;
        }

        //
        int dataCtr = dao.dataCount(forDate);
//        List<MfiBean> beans = calculator.calculateAndReturn(forDate);
//        if (dataCtr == 0) {
//            LOGGER.info("{} upload | uploading | for date:[{}]", calc_name, forDate);
//            upload(beans);
//        } else if (dataCtr == beans.size()) {
//            LOGGER.info("{} | upload skipped, already uploaded", calc_name);
//        } else {
//            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", calc_name, dataCtr, beans.size());
//        }
        if (dataCtr == 0) {
            LOGGER.info("{} upload | uploading | for date:[{}]", calc_name, forDate);
            List<MfiBean> beans = calculator.calculateAndReturn(forDate);
            upload(beans);
        }
    }

    private void upload(List<MfiBean> dtos) {
        AtomicInteger recordSucceed = new AtomicInteger();
        AtomicInteger recordFailed = new AtomicInteger();

        CalcMfiTab tab = new CalcMfiTab();
        dtos.forEach(dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());

            tab.setVolAtpMfi05Sma(dto.getVolMfi05());
            tab.setDelAtpMfi05Sma(dto.getDelMfi05());

            tab.setVolAtpMfi10Sma(dto.getVolMfi10());
            tab.setDelAtpMfi10Sma(dto.getDelMfi10());

            tab.setVolAtpMfi20Sma(dto.getVolMfi20());
            tab.setDelAtpMfi20Sma(dto.getDelMfi20());

            try {
                repo.save(tab);
                recordSucceed.incrementAndGet();
            } catch(DataIntegrityViolationException dive) {
                recordFailed.incrementAndGet();
            }
        });
        LOGGER.info("{} upload | record - uploaded {}, failed: [{}]", calc_name, recordSucceed.get(), recordFailed.get());
    }

}


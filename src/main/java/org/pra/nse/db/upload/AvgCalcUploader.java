package org.pra.nse.db.upload;

import org.pra.nse.ApCo;
import org.pra.nse.calculation.AvgCalculator;
import org.pra.nse.calculation.CalcCons;
import org.pra.nse.csv.data.AvgBean;
import org.pra.nse.db.dao.calc.AvgCalculationDao;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.repository.CalcAvgRepository;
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

import static org.pra.nse.calculation.CalcCons.AVG_FILE_PREFIX;

@Component
public class AvgCalcUploader extends BaseUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCalcUploader.class);

    private final String calc_name = CalcCons.AVG_DATA_NAME;

    private final AvgCalculationDao dao;
    private final CalcAvgRepository repo;
    private final AvgCalculator calculator;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    public AvgCalcUploader(AvgCalculationDao dao, CalcAvgRepository repo, AvgCalculator calculator,
                           NseFileUtils nseFileUtils, PraFileUtils praFileUtils) {
        super(praFileUtils, CalcCons.AVG_DIR_NAME, AVG_FILE_PREFIX);
        this.dao = dao;
        this.repo = repo;
        this.calculator = calculator;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
    }

    public void uploadForDate(LocalDate forDate) {
        //
        String fileName = CalcCons.AVG_FILE_PREFIX +forDate+ ApCo.DATA_FILE_EXT;
        String fromFile = CalcCons.AVG_FILES_PATH +File.separator+ fileName;
        LOGGER.info("{} upload | looking for file Name along with path:[{}]",calc_name, fromFile);

        if(!nseFileUtils.isFileExist(fromFile)) {
            LOGGER.warn("{} upload | file does not exist: [{}]", calc_name, fromFile);
            return;
        }

        //
        int dataCtr = dao.dataCount(forDate);
//        List<AvgBean> beans = calculator.calculateAndReturn(forDate);
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
            List<AvgBean> beans = calculator.calculateAndReturn(forDate);
            upload(beans);
        }
    }

    private void upload(List<AvgBean> dtos) {
        AtomicInteger recordSucceed = new AtomicInteger();
        AtomicInteger recordFailed = new AtomicInteger();

        CalcAvgTab tab = new CalcAvgTab();
        dtos.forEach(dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());

            tab.setAtpAvg05Sma(dto.getAtpAvg10());
            tab.setVolAvg05Sma(dto.getVolAvg10());
            tab.setDelAvg05Sma(dto.getDelAvg10());
            tab.setOiAvg05Sma(dto.getFoiAvg10());

            tab.setAtpAvg10Sma(dto.getAtpAvg10());
            tab.setVolAvg10Sma(dto.getVolAvg10());
            tab.setDelAvg10Sma(dto.getDelAvg10());
            tab.setOiAvg10Sma(dto.getFoiAvg10());

            tab.setAtpAvg20Sma(dto.getAtpAvg20());
            tab.setVolAvg20Sma(dto.getVolAvg20());
            tab.setDelAvg20Sma(dto.getDelAvg20());
            tab.setOiAvg20Sma(dto.getFoiAvg20());

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


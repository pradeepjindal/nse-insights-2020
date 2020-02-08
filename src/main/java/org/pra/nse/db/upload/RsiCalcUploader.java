package org.pra.nse.db.upload;

import org.pra.nse.ApCo;
import org.pra.nse.calculation.CalcCons;
import org.pra.nse.calculation.RsiCalculator;
import org.pra.nse.csv.data.RsiBean;
import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
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

import static org.pra.nse.calculation.CalcCons.RSI_FILE_PREFIX;

@Component
public class RsiCalcUploader extends BaseUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsiCalcUploader.class);

    private final String calc_name = CalcCons.RSI_DATA_NAME;

    private final RsiCalculationDao dao;
    private final CalcRsiRepository repo;
    private final RsiCalculator calculator;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    public RsiCalcUploader(RsiCalculationDao dao, CalcRsiRepository repo, RsiCalculator calculator,
                           NseFileUtils nseFileUtils, PraFileUtils praFileUtils) {
        super(praFileUtils, CalcCons.RSI_DIR_NAME, RSI_FILE_PREFIX);
        this.dao = dao;
        this.repo = repo;
        this.calculator = calculator;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
    }


    public void uploadForDate(LocalDate forDate) {
        //
        String fileName = CalcCons.RSI_FILE_PREFIX +forDate+ ApCo.DATA_FILE_EXT;
        String fromFile = CalcCons.RSI_FILES_PATH +File.separator+ fileName;
        LOGGER.info("{} upload | looking for file Name along with path:[{}]",calc_name, fromFile);

        if(!nseFileUtils.isFileExist(fromFile)) {
            LOGGER.warn("{} upload | file does not exist: [{}]", calc_name, fromFile);
            return;
        }

        //
        int dataCtr = dao.dataCount(forDate);
        List<RsiBean> beans = calculator.calculateAndReturn(forDate);
        if (dataCtr == 0) {
            LOGGER.info("{} upload | uploading | for date:[{}]", calc_name, forDate);
            upload(beans);
        } else if (dataCtr == beans.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", calc_name);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", calc_name, dataCtr, beans.size());
        }
    }

    private void upload(List<RsiBean> dtos) {
        AtomicInteger recordSucceed = new AtomicInteger();
        AtomicInteger recordFailed = new AtomicInteger();

        CalcRsiTab tab = new CalcRsiTab();
        dtos.forEach(dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());

            tab.setCloseRsi10Sma(dto.getCloseRsi10());
            tab.setLastRsi10Sma(dto.getLastRsi10());
            tab.setAtpRsi10Sma(dto.getAtpRsi10());

            tab.setCloseRsi20Sma(dto.getCloseRsi20());
            tab.setLastRsi20Sma(dto.getLastRsi20());
            tab.setAtpRsi20Sma(dto.getAtpRsi20());

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


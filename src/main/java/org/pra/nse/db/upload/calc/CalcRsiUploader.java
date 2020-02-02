package org.pra.nse.db.upload.calc;

import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CalcRsiUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcRsiUploader.class);

    private final CalcRsiRepository repository;
    private final RsiCalculationDao dao;

    public CalcRsiUploader(CalcRsiRepository nseCashMarketRepository,
                           RsiCalculationDao nseCashMarketDao) {
        this.repository = nseCashMarketRepository;
        this.dao = nseCashMarketDao;

    }


    public void upload(LocalDate forDate, List<CalcRsiTab> list) {
        //TODO not in use
        if(dao.dataCount(forDate) > 0) {
            LOGGER.info("rsi-upload | SKIPPING - already uploaded | for date:[{}]", forDate);
            return;
        } else {
            LOGGER.info("rsi-upload | uploading | for date:[{}]", forDate);
        }

        CalcRsiTab target = new CalcRsiTab();
        AtomicInteger totalRecordFailed = new AtomicInteger();
        list.forEach( source -> {
            target.reset();
            target.setSymbol(source.getSymbol());
            target.setOpenRsi10Sma(source.getOpenRsi10Sma());
            target.setHighRsi10Sma(source.getHighRsi10Sma());
            target.setLowRsi10Sma(source.getLowRsi10Sma());
            target.setCloseRsi10Sma(source.getCloseRsi10Sma());
            target.setLastRsi10Sma(source.getLastRsi10Sma());
            target.setAtpRsi10Sma(source.getAtpRsi10Sma());
            target.setHlmRsi10Sma(source.getHlmRsi10Sma());
            try {
                repository.save(target);
            } catch(DataIntegrityViolationException dive) {
                totalRecordFailed.incrementAndGet();
            }
        });
        LOGGER.info("rsi record failed for data integrity: [{}]", totalRecordFailed.get());
    }

}


package org.pra.nse.db.upload.calc;

import org.pra.nse.db.dao.CalcRsiDao;
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
    private final CalcRsiDao dao;

    public CalcRsiUploader(CalcRsiRepository nseCashMarketRepository,
                           CalcRsiDao nseCashMarketDao) {
        this.repository = nseCashMarketRepository;
        this.dao = nseCashMarketDao;

    }


    public void upload(LocalDate forDate, List<CalcRsiTab> list) {

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
            target.setOpenRsi10Ema(source.getOpenRsi10Ema());
            target.setHighRsi10Ema(source.getHighRsi10Ema());
            target.setLowRsi10Ema(source.getLowRsi10Ema());
            target.setCloseRsi10Ema(source.getCloseRsi10Ema());
            target.setLastRsi10Ema(source.getLastRsi10Ema());
            target.setAtpRsi10Ema(source.getAtpRsi10Ema());
            target.setHlmRsi10Ema(source.getHlmRsi10Ema());
            try {
                repository.save(target);
            } catch(DataIntegrityViolationException dive) {
                totalRecordFailed.incrementAndGet();
            }
        });
        LOGGER.info("rsi record failed for data integrity: [{}]", totalRecordFailed.get());
    }

}


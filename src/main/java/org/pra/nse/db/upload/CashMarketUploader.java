package org.pra.nse.db.upload;

import org.pra.nse.ApCo;
import org.pra.nse.csv.bean.in.CmBean;
import org.pra.nse.csv.read.CmCsvReader;
import org.pra.nse.db.dao.nse.CashMarketDao;
import org.pra.nse.db.model.NseCashMarketTab;
import org.pra.nse.db.repository.NseCashMarketRepository;
import org.pra.nse.util.DateUtils;

import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CashMarketUploader extends BaseUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashMarketUploader.class);

    private final NseCashMarketRepository repository;
    private final CashMarketDao dao;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final CmCsvReader csvReader;

    public CashMarketUploader(NseCashMarketRepository nseCashMarketRepository,
                              CashMarketDao cashMarketDao,
                              NseFileUtils nseFileUtils,
                              PraFileUtils praFileUtils,
                              CmCsvReader cmCsvReader ) {
        super(praFileUtils, ApCo.CM_DIR_NAME, ApCo.PRA_CM_FILE_PREFIX);
        this.repository = nseCashMarketRepository;
        this.dao = cashMarketDao;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.csvReader = cmCsvReader;
    }


    public void uploadForDate(LocalDate forDate) {

        if(dao.dataCount(forDate) > 0) {
            LOGGER.info("CM-upload | SKIPPING - already uploaded | for date:[{}]", forDate);
            return;
        } else {
            LOGGER.info("CM-upload | uploading | for date:[{}]", forDate);
        }

        String fromFile = ApCo.CM_FILES_PATH + File.separator+ ApCo.PRA_CM_FILE_PREFIX +forDate+ ApCo.REPORTS_FILE_EXT;
        //LOGGER.info("CM-upload | looking for file Name along with path:[{}]",fromFile);

        if(!nseFileUtils.isFileExist(fromFile)) {
            LOGGER.info("CM-upload | file does not exist: [{}]", fromFile);
            return;
        }
        Map<String, CmBean> latestBeanMap = csvReader.read(fromFile);

        NseCashMarketTab target = new NseCashMarketTab();
        AtomicInteger recordSucceed = new AtomicInteger();
        AtomicInteger recordFailed = new AtomicInteger();
        latestBeanMap.values().forEach( source -> {
            target.reset();
            target.setSymbol(source.getSymbol());
            target.setSeries(source.getSeries());
            target.setOpen(source.getOpen());
            target.setHigh(source.getHigh());
            target.setLow(source.getLow());
            target.setClose(source.getClose());
            target.setLast(source.getLast());
            target.setPrevClose(source.getPrevClose());
            target.setTotTrdQty(source.getTotTrdQty());
            target.setTotTrdVal(source.getTotTrdVal());
            target.setTradeDate(DateUtils.toLocalDate(source.getTimestamp()));
            target.setTotalTrades(source.getTotalTrades());
            target.setIsin(source.getIsin());
            try {
                repository.save(target);
                recordSucceed.incrementAndGet();
            } catch(DataIntegrityViolationException dive) {
                recordFailed.incrementAndGet();
            }
        });
        LOGGER.info("CM-upload | record - uploaded {}, failed: [{}]", recordSucceed.get(), recordFailed.get());
    }

}

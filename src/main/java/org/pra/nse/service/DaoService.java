package org.pra.nse.service;

import org.pra.nse.db.dao.mfi.*;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

@Component
public class DaoService {

    private final MfiDao03 mfiDao03;
    private final MfiDao05 mfiDao05;
    private final MfiDao10 mfiDao10;
    private final MfiDao20 mfiDao20;

    private final Map<Integer, MfiDao> mfiDaoMap = new HashMap<>();


    public DaoService(MfiDao03 mfiDao03, MfiDao05 mfiDao05, MfiDao10 mfiDao10, MfiDao20 mfiDao20) {
        this.mfiDao03 = mfiDao03;
        this.mfiDao05 = mfiDao05;
        this.mfiDao10 = mfiDao10;
        this.mfiDao20 = mfiDao20;

        mfiDaoMap.put(3, mfiDao03);
        mfiDaoMap.put(5, mfiDao05);
        mfiDaoMap.put(10, mfiDao10);
        mfiDaoMap.put(20, mfiDao20);
    }


    public MfiDao getMfiDao(int daoNum) {
        return mfiDaoMap.get(daoNum);
    }

}

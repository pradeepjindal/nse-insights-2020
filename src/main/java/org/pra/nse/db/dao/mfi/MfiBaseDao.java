package org.pra.nse.db.dao.mfi;

import org.pra.nse.db.model.CalcMfiTab;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class MfiBaseDao implements MfiDao {
    private final JdbcTemplate jdbcTemplate;

    protected String mfiDataCountForDateSql;
    protected String mfiSql;

    public MfiBaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int dataCount(LocalDate tradeDate) {
        Object[] args = new Object[] {tradeDate.toString()};
        return jdbcTemplate.queryForObject(mfiDataCountForDateSql, args, Integer.class);
    }


    @Override
    public List<CalcMfiTab> getMfi() {
        return getMfi(null, null);
    }

    @Override
    public List<CalcMfiTab> getMfi(LocalDate forDate) {
        String param = forDate.plusDays(1).toString();
        List<CalcMfiTab> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTab>(CalcMfiTab.class), param);
        return result;
    }

    @Override
    public List<CalcMfiTab> getMfi(LocalDate fromDate, LocalDate toDate) {
        List<CalcMfiTab> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTab>(CalcMfiTab.class));
        return result;
    }



}

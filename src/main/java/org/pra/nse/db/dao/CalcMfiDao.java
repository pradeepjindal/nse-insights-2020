package org.pra.nse.db.dao;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.pra.nse.db.model.CalcMfiTab;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@PropertySource(value = "classpath:mfi-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class CalcMfiDao {
    private final JdbcTemplate jdbcTemplate;

    @Value("${mfiDataCountForDateSql}")
    private String mfiDataCountForDateSql;
    @Value("${mfiSql}")
    private String mfiSql;

    CalcMfiDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public int dataCount(LocalDate tradeDate) {
        Object[] args = new Object[] {tradeDate.toString()};
        return jdbcTemplate.queryForObject(mfiDataCountForDateSql, args, Integer.class);
    }


    public List<CalcMfiTab> getMfi() {
        return getMfi(null, null);
    }

    public List<CalcMfiTab> getMfi(LocalDate forDate) {
        String param = forDate.plusDays(1).toString();
        List<CalcMfiTab> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTab>(CalcMfiTab.class), param);
        return result;
    }

    public List<CalcMfiTab> getMfi(LocalDate fromDate, LocalDate toDate) {
        List<CalcMfiTab> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTab>(CalcMfiTab.class));
        return result;
    }


}

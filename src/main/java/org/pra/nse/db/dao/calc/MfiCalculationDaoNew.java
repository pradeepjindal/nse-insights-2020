package org.pra.nse.db.dao.calc;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.pra.nse.db.model.CalcMfiTabNew;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@PropertySource(value = "classpath:mfi-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class MfiCalculationDaoNew {
    private final JdbcTemplate jdbcTemplate;

    @Value("${mfiDataCountForDateSqlNew}")
    private String mfiDataCountForDateSql;
    @Value("${mfiSqlNew}")
    private String mfiSql;

    MfiCalculationDaoNew(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int dataCount(LocalDate forDate) {
        Object[] args = new Object[] {forDate.toString()};
        return jdbcTemplate.queryForObject(mfiDataCountForDateSql, args, Integer.class);
    }

    public int dataCount(LocalDate forDate, int forDays) {
        Object[] args = new Object[] {forDate.toString(), forDays};
        return jdbcTemplate.queryForObject(mfiDataCountForDateSql, args, Integer.class);
    }


    public List<CalcMfiTabNew> getMfi(int forDays) {
        return getMfi(null, null);
    }

    public List<CalcMfiTabNew> getMfi(LocalDate forDate) {
        String param = forDate.plusDays(1).toString();
        List<CalcMfiTabNew> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTabNew>(CalcMfiTabNew.class), param);
        return result;
    }

    public List<CalcMfiTabNew> getMfi(LocalDate forDate, int forDays) {
        Object[] args = new Object[] {forDate.plusDays(1).toString(), forDays};
        List<CalcMfiTabNew> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTabNew>(CalcMfiTabNew.class), args);
        return result;
    }

    public List<CalcMfiTabNew> getMfi(LocalDate fromDate, LocalDate toDate) {
        List<CalcMfiTabNew> result = jdbcTemplate.query(mfiSql, new BeanPropertyRowMapper<CalcMfiTabNew>(CalcMfiTabNew.class));
        return result;
    }


}

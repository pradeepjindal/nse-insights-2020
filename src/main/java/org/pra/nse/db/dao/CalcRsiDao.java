package org.pra.nse.db.dao;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.pra.nse.db.model.CalcRsiTab;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@PropertySource(value = "classpath:rsi-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class CalcRsiDao {
    private final JdbcTemplate jdbcTemplate;

    @Value("${rsiDataCountForDateSql}")
    private String rsiDataCountForDateSql;
    @Value("${rsiSql}")
    private String rsiSql;

    CalcRsiDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public int dataCount(LocalDate tradeDate) {
        Object[] args = new Object[] {tradeDate.toString()};
        return jdbcTemplate.queryForObject(rsiDataCountForDateSql, args, Integer.class);
    }


    public List<CalcRsiTab> getRsi() {
        return getRsi(null, null);
    }

    public List<CalcRsiTab> getRsi(LocalDate forDate) {
        String param = forDate.plusDays(1).toString();
        List<CalcRsiTab> result = jdbcTemplate.query(rsiSql, new BeanPropertyRowMapper<CalcRsiTab>(CalcRsiTab.class), param);
        return result;
    }

    public List<CalcRsiTab> getRsi(LocalDate fromDate, LocalDate toDate) {
        List<CalcRsiTab> result = jdbcTemplate.query(rsiSql, new BeanPropertyRowMapper<CalcRsiTab>(CalcRsiTab.class));
        return result;
    }


}

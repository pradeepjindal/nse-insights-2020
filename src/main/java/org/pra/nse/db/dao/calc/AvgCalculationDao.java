package org.pra.nse.db.dao.calc;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.pra.nse.db.dto.OiSumDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@PropertySource(value = "classpath:avg-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class AvgCalculationDao {
    private final JdbcTemplate jdbcTemplate;

    @Value("${avgDataCountForDateSql}")
    private String avgDataCountForDateSql;
    @Value("${avgSql}")
    private String avgSql;
    @Value("${oiSumSql}")
    private String oiSumSql;

    AvgCalculationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int dataCount(LocalDate tradeDate) {
        Object[] args = new Object[] {tradeDate.toString()};
        return jdbcTemplate.queryForObject(avgDataCountForDateSql, args, Integer.class);
    }


    public List<CalcAvgTab> getAvg() {
        return getAvg(null, null);
    }

    public List<CalcAvgTab> getAvg(LocalDate forDate) {
        String param = forDate.plusDays(1).toString();
        List<CalcAvgTab> result = jdbcTemplate.query(avgSql, new BeanPropertyRowMapper<CalcAvgTab>(CalcAvgTab.class), param);
        return result;
    }

    public List<CalcAvgTab> getAvg(LocalDate fromDate, LocalDate toDate) {
        List<CalcAvgTab> result = jdbcTemplate.query(avgSql, new BeanPropertyRowMapper<CalcAvgTab>(CalcAvgTab.class));
        return result;
    }

    public List<OiSumDto> getOiSum(LocalDate forDate) {
        String param = forDate.toString();
        List<OiSumDto> result = jdbcTemplate.query(oiSumSql, new BeanPropertyRowMapper<OiSumDto>(OiSumDto.class), param);
        return result;
    }


}

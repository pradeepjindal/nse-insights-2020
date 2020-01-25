package org.pra.nse.db.dao.nse;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.pra.nse.db.dto.OiDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@PropertySource(value = "classpath:oi-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class OiDao {
    private final JdbcTemplate jdbcTemplate;

    @Value("${oiAllSql}")
    private String oiAllSql;
    @Value("${oiForDateSql}")
    private String oiForDateSql;

    OiDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OiDto> getOiAll() {
        List<OiDto> result = jdbcTemplate.query(oiAllSql, new BeanPropertyRowMapper<OiDto>(OiDto.class));
        return result;
    }

    public List<OiDto> getOiForDate(LocalDate forDate) {
        String param = forDate.toString();
        List<OiDto> result = jdbcTemplate.query(oiForDateSql, new BeanPropertyRowMapper<OiDto>(OiDto.class), param);
        return result;
    }

}

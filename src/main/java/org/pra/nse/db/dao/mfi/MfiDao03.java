package org.pra.nse.db.dao.mfi;

import org.pra.nse.config.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:mfi-query.yaml", factory = YamlPropertyLoaderFactory.class)
public class MfiDao03 extends MfiBaseDao {

//    @Value("${mfiDataCountForDateSql}")
//    private String mfiDataCountForDateSql;
//    @Value("${mfiSql}")
//    private String mfiSql;

    public MfiDao03(JdbcTemplate jdbcTemplate,
                    @Value("${mfiDataCountForDateSql_03}") String mfiDataCountForDateSql,
                    @Value("${mfiSql_03}")String mfiSql) {
        super(jdbcTemplate);
        super.mfiDataCountForDateSql = mfiDataCountForDateSql;
        super.mfiSql = mfiSql;
    }

}

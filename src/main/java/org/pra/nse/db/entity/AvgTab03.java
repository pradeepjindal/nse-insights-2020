package org.pra.nse.db.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class AvgTab03 extends AvgTab {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_avg_seq_03", sequenceName = "calc_avg_se_03", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_avg_seq_03")
    private Long id;


}

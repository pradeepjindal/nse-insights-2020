package org.pra.nse.db.entity.mfi;

import javax.persistence.*;

@Entity
@Table(name = "Calc_Mfi_Tab_03")
public class MfiTab03 extends MfiBaseTab {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_mfi_seq_03", sequenceName = "calc_mfi_seq_03", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_mfi_seq_03")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

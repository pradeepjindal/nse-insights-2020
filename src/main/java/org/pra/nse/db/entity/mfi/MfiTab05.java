package org.pra.nse.db.entity.mfi;

import javax.persistence.*;

@Entity
@Table(name = "Calc_Mfi_Tab_05")
public class MfiTab05 extends MfiBaseTab {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_mfi_seq_05", sequenceName = "calc_mfi_seq_05", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_mfi_seq_05")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

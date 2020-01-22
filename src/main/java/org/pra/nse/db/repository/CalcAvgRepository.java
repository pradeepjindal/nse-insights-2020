package org.pra.nse.db.repository;

import org.pra.nse.db.model.CalcAvgTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcAvgRepository extends JpaRepository<CalcAvgTab, Long> {

}

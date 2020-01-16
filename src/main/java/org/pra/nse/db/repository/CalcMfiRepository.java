package org.pra.nse.db.repository;

import org.pra.nse.db.model.CalcMfiTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcMfiRepository extends JpaRepository<CalcMfiTab, Long> {

}

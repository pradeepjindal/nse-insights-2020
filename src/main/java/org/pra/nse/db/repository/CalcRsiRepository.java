package org.pra.nse.db.repository;

import org.pra.nse.db.model.CalcRsiTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcRsiRepository extends JpaRepository<CalcRsiTab, Long> {

}

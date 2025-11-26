package com.CrazyBet.Repository;

import com.CrazyBet.Model.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartitaRepository extends JpaRepository<Partita,Long> {
}

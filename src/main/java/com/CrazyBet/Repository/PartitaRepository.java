package com.CrazyBet.Repository;

import com.CrazyBet.Model.Partita;
import com.CrazyBet.Model.Scommessa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartitaRepository extends JpaRepository<Partita,Long> {
}

package com.CrazyBet.Repository;

import com.CrazyBet.Model.Scommessa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScommessaRepository extends JpaRepository <Scommessa, Long> {
    List<Scommessa> findByUtenteId(Long utenteId);
    List<Scommessa> findByPartitaId(Long partitaId);

}



package com.CrazyBet.Repository;

import com.CrazyBet.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository <Utente, Long> {
}

package com.CrazyBet.Repository;

import com.CrazyBet.Model.Scommessa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScommessaRepository extends JpaRepository <Scommessa, Long> {
}

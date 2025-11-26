package com.CrazyBet.Repository;

import com.CrazyBet.Model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadraRepository extends JpaRepository<Squadra,Long> {
}

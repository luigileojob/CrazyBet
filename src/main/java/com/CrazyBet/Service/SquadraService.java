package com.CrazyBet.Service;

import com.CrazyBet.Exception.SquadraNotFoundException;
import com.CrazyBet.Exception.UtenteNotFoundException;
import com.CrazyBet.Model.Squadra;
import com.CrazyBet.Model.Utente;
import com.CrazyBet.Repository.SquadraRepository;
import com.CrazyBet.Repository.UtenteRepository;
import com.CrazyBet.Utils.Sicurezza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Optional;

@Service
public class SquadraService {

    @Autowired
    private SquadraRepository squadraRepository;
    @Autowired
    private UtenteRepository utenteRepository;


    public Squadra creaSquadra (Long utenteId, Squadra squadra) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(()-> new UtenteNotFoundException("Utente non trovato con ID: "+ utenteId));
        Sicurezza.verificaAdmin(utente);
        return squadraRepository.save(squadra);
    }

    public Squadra cercaSquadraConId (Long squadraId) {
        return squadraRepository.findById(squadraId)
                .orElseThrow(() -> new SquadraNotFoundException("Squadra non trovata con ID: " + squadraId));
    }

    public void eliminaSquadra(Long utenteId, Long squadraId) {
        Utente utente = utenteRepository.findById(utenteId)
            .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + utenteId));
        Sicurezza.verificaAdmin(utente);
       if (!squadraRepository.existsById(squadraId)) {
           throw new SquadraNotFoundException("Squadra non trovata con ID: " + squadraId);
       }
    squadraRepository.deleteById(squadraId);
}

    public Squadra aggiornaSquadra (Long utenteId,Long squadraId, Squadra richiestaSquadra) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + utenteId));
        Sicurezza.verificaAdmin(utente);
        Optional<Squadra> squadraOptional = squadraRepository.findById(squadraId);
        if (squadraOptional.isPresent()) {
            Squadra newSquadra = squadraOptional.get();
            if (richiestaSquadra.getNomeSquadra() != null) {
                newSquadra.setNomeSquadra(richiestaSquadra.getNomeSquadra());
            }
            return squadraRepository.save(newSquadra);
        }
        throw new SquadraNotFoundException("Squadra non trovata");
    }
}
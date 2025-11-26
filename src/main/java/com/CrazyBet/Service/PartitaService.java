package com.CrazyBet.Service;

import com.CrazyBet.Exception.PartitaNotFoundException;
import com.CrazyBet.Exception.UtenteNotFoundException;
import com.CrazyBet.Model.Partita;
import com.CrazyBet.Model.Scommessa;
import com.CrazyBet.Model.Utente;
import com.CrazyBet.Repository.PartitaRepository;
import com.CrazyBet.Repository.UtenteRepository;
import com.CrazyBet.Utils.Sicurezza;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;
    @Autowired
    private UtenteRepository utenteRepository;

    public Partita creaPartita(Long utenteId, Partita partita) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + utenteId));
        Sicurezza.verificaAdmin(utente);
        System.out.println("Evento Partita creato con successo");
        return partitaRepository.save(partita);
    }

    public Partita cercaPartitaConId(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() -> new PartitaNotFoundException("Partita non trovata con ID: " + id));
    }

    public List<Partita> aggiornaStatusPartite(Long utenteId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + utenteId));

        Sicurezza.verificaAdmin(utente);
        LocalDateTime ora = LocalDateTime.now();

        List<Partita> partite = partitaRepository.findAll();
        if (partite.isEmpty()) {
            System.err.println("Nessuna partita trovata nel database. Nessuno stato aggiornato.");
            return Collections.emptyList();
        }

        for (Partita partita : partite) {
            if (partita.getOraInizioPartita() == null || partita.getOraFinePartita() == null) {
                System.err.println("Partita con ID " + partita.getId() + " ha date nulle, stato non aggiornato.");
                continue;
            }
            if (partita.getOraInizioPartita().isAfter(ora)) {
                partita.setStatusPartita(Partita.Status.DA_GIOCARE);
            } else if (partita.getOraInizioPartita().isBefore(ora) && partita.getOraFinePartita().isAfter(ora)) {
                partita.setStatusPartita(Partita.Status.IN_CORSO);
            } else if (partita.getOraFinePartita().isBefore(ora)) {
                partita.setStatusPartita(Partita.Status.FINITA);
            } else {
                System.err.println("Impossibile aggiornare lo stato della partita con ID: " + partita.getId());
            }
        }
        return partitaRepository.saveAll(partite);
    }

    @Transactional
    public Partita esitoPartita(Long partitaId) {
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new PartitaNotFoundException("La partita non è stata trovata con ID: " + partitaId));

        if (partita.getStatusPartita() != Partita.Status.FINITA) {
            throw new RuntimeException("La partita è ancora da giocare o in corso");
        }

        int goalCasa = partita.getGoalCasa() != null ? partita.getGoalCasa() : 0;
        int goalOspite = partita.getGoalOspite() != null ? partita.getGoalOspite() : 0;

        if (goalCasa > goalOspite) {
            partita.setEsitoPartita(Partita.Esito.VITTORIA_CASA);
        } else if (goalCasa < goalOspite) {
            partita.setEsitoPartita((Partita.Esito.VITTORIA_OSPITE));
        } else {
            partita.setEsitoPartita(Partita.Esito.PAREGGIO);
        }
        return partitaRepository.save(partita);
    }
    public Partita aggiungiScommessa( Partita scommessaPartita) {
        return partitaRepository.save(scommessaPartita);
    }

}


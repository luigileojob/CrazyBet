package com.CrazyBet.Service;

import com.CrazyBet.Exception.PartitaNotFoundException;
import com.CrazyBet.Exception.ScommessaNotFoundException;
import com.CrazyBet.Exception.UtenteNotFoundException;
import com.CrazyBet.Model.Partita;
import com.CrazyBet.Model.Scommessa;
import com.CrazyBet.Model.Utente;
import com.CrazyBet.Repository.PartitaRepository;
import com.CrazyBet.Repository.ScommessaRepository;
import com.CrazyBet.Repository.UtenteRepository;
import com.CrazyBet.Utils.Sicurezza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ScommessaService {

    @Autowired
    private ScommessaRepository scommessaRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PartitaRepository partitaRepository;

     public Scommessa aggiungiScommessaPartita(Long utenteId, List<Long> partitaIdList, Scommessa.EsitoScommessa esitoScommessa, BigDecimal importoScommesso) {

         Utente utente = utenteRepository.findById(utenteId)
                 .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + utenteId));

         List<Partita> partite = partitaRepository.findAllById(partitaIdList);
         if (partite.isEmpty()) {
             throw new PartitaNotFoundException("Le partite non sono state trovate con gli ID: " + partitaIdList);
         }

         for (Partita partita : partite) {
             if (partita.getStatusPartita() != Partita.Status.DA_GIOCARE) {
                 throw new ScommessaNotFoundException(
                         "Scommessa non accettata: la partita " + partita.getId() + " non è giocabile");
             }
         }

         if (importoScommesso == null || importoScommesso.compareTo(BigDecimal.ZERO) <= 0
                 || importoScommesso.compareTo(utente.getSaldo()) > 0) {
             throw new IllegalArgumentException("Saldo insufficiente o importo non valido.");
         }

         utente.setSaldo(utente.getSaldo().subtract(importoScommesso));
         utenteRepository.save(utente);

         Scommessa scommessa = new Scommessa();
         scommessa.setUtente(utente);
         scommessa.setPartite(partite);
         scommessa.setImportoScommessa(importoScommesso);
         scommessa.setEsitoScommessaUtente(esitoScommessa);
         scommessa.setStatusScommessa(Scommessa.StatusScommessa.IN_ATTESA);

         for (Partita partita : partite) {
             partita.getScommesse().add(scommessa);
         }
         partitaRepository.saveAll(partite);

         return scommessaRepository.save(scommessa);
     }

     public Scommessa cercaScommessaConId (Long scommessaId) {
         return scommessaRepository.findById(scommessaId)
                 .orElseThrow(()-> new ScommessaNotFoundException("La scommessa con ID " + scommessaId + " non è stata trovata"));
     }

    public List<Scommessa> cercaListaScommesseUtenteConId(Long utenteId) {
       List<Scommessa> scommesse = scommessaRepository.findByUtenteId(utenteId);

       if (scommesse.isEmpty()) {
           boolean utenteEsiste = utenteRepository.existsById(utenteId);
           if (!utenteEsiste) {
               throw new UtenteNotFoundException("L'utente con ID : " + utenteId + " non è stato trovato");
           }
       }
       return scommesse;
    }

    public List<Scommessa> cercaScommessePartitaConId(Long partitaId) {
        List<Scommessa> scommesse = scommessaRepository.findByPartite_Id(partitaId);

        if (scommesse.isEmpty()) {
            boolean partitaEsiste = partitaRepository.existsById(partitaId);
            if (!partitaEsiste) {
                throw new PartitaNotFoundException("La partita con ID " + partitaId + " non esiste.");
            }
        }
        return scommesse;
    }

    @Transactional
    public List<Scommessa> aggiornaValidazioneScommesseUtente(Long utenteId) {

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato"));

        List<Scommessa> scommesse = scommessaRepository.findByUtenteId(utenteId);

        for (Scommessa scommessa : scommesse) {

            boolean tuttePartiteFinite = true;
            boolean tutteIndovinate = true;

            for (Partita partita : scommessa.getPartite()) {

                if (partita.getStatusPartita() != Partita.Status.FINITA) {
                    tuttePartiteFinite = false;
                    break;
                }

                if (!scommessa.getEsitoScommessaUtente().name().equals(partita.getEsitoPartita().name())) {
                    tutteIndovinate = false;
                }
            }

            if (!tuttePartiteFinite) {
                scommessa.setStatusScommessa(Scommessa.StatusScommessa.IN_ATTESA);
                continue;
            }

            if (tutteIndovinate) {
                scommessa.setStatusScommessa(Scommessa.StatusScommessa.VINTA);

                BigDecimal vincita = scommessa.getImportoScommessa().multiply(BigDecimal.valueOf(2));
                utente.setSaldo(utente.getSaldo().add(vincita));

            } else {
                scommessa.setStatusScommessa(Scommessa.StatusScommessa.PERSA);
            }
        }
        utenteRepository.save(utente);
        return scommessaRepository.saveAll(scommesse);
    }

    @Transactional
    public List<Scommessa> aggiornaValidazioneTutteScommesse(Long utenteIdAdmin) {

        Utente utente = utenteRepository.findById(utenteIdAdmin)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato"));
        Sicurezza.verificaAdmin(utente);

        List<Scommessa> scommesse = scommessaRepository.findByStatusScommessa(Scommessa.StatusScommessa.IN_ATTESA);

        for (Scommessa scommessa : scommesse) {

            boolean tuttePartiteFinite = true;
            boolean tutteIndovinate = true;

            for (Partita partita : scommessa.getPartite()) {

                if (partita.getStatusPartita() != Partita.Status.FINITA) {
                    tuttePartiteFinite = false;
                    break;
                }

                if (!scommessa.getEsitoScommessaUtente().name().equals(partita.getEsitoPartita().name())) {
                    tutteIndovinate = false;
                }
            }

            if (!tuttePartiteFinite) {
                continue;
            }

            if (tutteIndovinate) {
                scommessa.setStatusScommessa(Scommessa.StatusScommessa.VINTA);

                Utente scommettitore = scommessa.getUtente();
                BigDecimal vincita = scommessa.getImportoScommessa().multiply(BigDecimal.valueOf(2));
                scommettitore.setSaldo(scommettitore.getSaldo().add(vincita));
                utenteRepository.save(scommettitore);

            } else {
                scommessa.setStatusScommessa(Scommessa.StatusScommessa.PERSA);
            }
        }

        return scommessaRepository.saveAll(scommesse);
    }


}





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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //DEFINIRE STATUS SCOMMESSA
    //DEFINIRE ESITO SCOMMESSA SCELTA

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

}

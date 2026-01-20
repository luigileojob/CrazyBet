package com.CrazyBet.Controller;


import com.CrazyBet.Model.Scommessa;
import com.CrazyBet.Service.ScommessaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping ("/scommessa")
public class ScommessaController {

    @Autowired
    private ScommessaService scommessaService;


    @PostMapping ("/salva-scommessa-partita")
    public Scommessa salvaScommessaPartita(
            @RequestParam Long utenteId,
            @RequestParam List<Long> partiteIdList,
            @RequestParam Scommessa.EsitoScommessa esitoScommessa,
            @RequestParam BigDecimal importoScommesso) {
        return scommessaService.aggiungiScommessaPartita(
                utenteId,partiteIdList, esitoScommessa, importoScommesso);
    }

    @GetMapping ("/cerca-scommessa-id")
    public Scommessa cercaScommessaConId (@RequestParam Long scommessaId) {
        return scommessaService.cercaScommessaConId(scommessaId);
    }

    @GetMapping ("/lista-scommesse-utente-id")
    public List<Scommessa> cercaListaScommessaUtenteId (@RequestParam Long utenteId) {
        return scommessaService.cercaListaScommesseUtenteConId(utenteId);
    }

    @GetMapping ("/lista-scommesse-partita-id")
    public List<Scommessa> cercaListaScommessaPartitaId (@RequestParam Long partitaId) {
        return scommessaService.cercaScommessePartitaConId(partitaId);
    }

    @PutMapping ("/validazione-stato-scommessa-utente-id")
    public List<Scommessa> validazioneStatoScommessaUtenteId (@RequestParam Long utenteId) {
        return scommessaService.aggiornaValidazioneScommesseUtente(utenteId);
    }

    @PutMapping ("/validazione-stato-tutte-scommesse")
    public List<Scommessa> validazioneStatoTutteScommesse (@RequestParam Long utenteIdAdmin){
        return scommessaService.aggiornaValidazioneTutteScommesse(utenteIdAdmin);
    }




}




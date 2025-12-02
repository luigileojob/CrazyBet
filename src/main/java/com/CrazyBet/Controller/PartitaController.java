package com.CrazyBet.Controller;

import com.CrazyBet.Model.Partita;
import com.CrazyBet.Service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/partita")
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    @PostMapping ("/salva-partita")
    public Partita salvaPartita ( @RequestParam Long utenteId, @RequestBody Partita partita) {
      return partitaService.creaPartita(utenteId,partita);
    }

    @GetMapping ("/cerca-partita-id")
    public Partita cercaPartitaId (@RequestParam Long partitaId) {
        return partitaService.cercaPartitaConId(partitaId);
    }

    @PatchMapping ("/aggiorna-partita-id")
    public List<Partita> aggiornaStatusPartite (@RequestParam Long partitaId) {
        return partitaService.aggiornaStatusPartite(partitaId);
    }

    @GetMapping ("/esito-patita")
    public Partita esitoRisultatoPartita (@RequestParam Long partitaId) {
        return partitaService.esitoPartita(partitaId);
    }

}

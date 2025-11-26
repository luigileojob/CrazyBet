package com.CrazyBet.Controller;

import com.CrazyBet.Model.Utente;
import com.CrazyBet.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping ("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @PostMapping ("/salva-utente")
    public Utente salvaUtente (@RequestBody Utente utente) {
        return utenteService.creaUtente(utente);
    }

    @GetMapping ("/mostra-utente")
    public Utente mostraUtente (@RequestParam Long id) {
        return utenteService.cercaUtenteConId(id);
    }

    @DeleteMapping("/elimina-utente-id")
    public ResponseEntity<Void> eliminaUtente(@RequestParam Long id) {
        utenteService.eliminaUtente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping ("/aggiorna-utente")
    public Utente aggiornaUtente (@RequestParam Long id, @RequestBody Utente utente) {
        return utenteService.aggiornaUtenteConId( id,utente);
    }

    @PatchMapping ("/deposito-saldo-utente")
    public Utente depositaSaldo (@RequestParam Long id, @RequestBody BigDecimal importoDeposito ) {
        return utenteService.deposito(id,importoDeposito);
    }

    @PatchMapping("/prelievo-saldo-utente")
    public Utente prelievoSaldo (@RequestParam Long id, @RequestBody BigDecimal importoPrelievo) {
        return utenteService.prelievo(id, importoPrelievo);
    }
}

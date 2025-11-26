package com.CrazyBet.Controller;

import com.CrazyBet.Model.Squadra;
import com.CrazyBet.Service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/squadra")
public class SquadraController {

    @Autowired
    private SquadraService squadraService;


    @PostMapping ("/salva-squadra")
    public Squadra salvaSquadra (@RequestParam Long utenteId,@RequestBody Squadra squadra) {
        return squadraService.creaSquadra(utenteId,squadra);
    }

    @GetMapping ("/cerca-squadra-con-id")
    public Squadra cercaSquadraConId (@RequestParam Long squadraId) {
        return squadraService.cercaSquadraConId(squadraId);
    }

    @DeleteMapping("/elimina-squadra-id")
    public void eliminaSquadraConId(@RequestParam Long utenteId, @RequestParam Long squadraId) {
        squadraService.eliminaSquadra(utenteId,squadraId);
    }

    @PutMapping("/aggiorna-squadra-id")
    public Squadra aggiornaSquadraConId(@RequestParam Long utenteId,@RequestParam Long squadraId, @RequestBody Squadra richiestaSquadra){
        return squadraService.aggiornaSquadra(utenteId,squadraId,richiestaSquadra);
    }

}

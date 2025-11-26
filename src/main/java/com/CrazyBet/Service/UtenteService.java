package com.CrazyBet.Service;

import com.CrazyBet.Model.Utente;
import com.CrazyBet.Repository.UtenteRepository;
import com.CrazyBet.Exception.UtenteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente creaUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    public Utente cercaUtenteConId(Long id) {
        Optional<Utente> opzioneUtente = utenteRepository.findById(id);
        if (opzioneUtente.isPresent()) {
            return opzioneUtente.get();
        }
        throw new UtenteNotFoundException("Utente non trovato");
    }

    public void eliminaUtente(Long id){
        Optional<Utente> opzioneUtente = utenteRepository.findById(id);
        if(opzioneUtente.isPresent()) {
            utenteRepository.deleteById(id);
            return;
        }
        throw new UtenteNotFoundException("Utente non trovato");
    }

    public Utente aggiornaUtenteConId(Long id, Utente richiestaUtente) {
        Optional<Utente> optionalUtente = utenteRepository.findById(id);
        if (optionalUtente.isPresent()) {
            Utente newUtente = optionalUtente.get();

            if (richiestaUtente.getNome() != null) {
                newUtente.setNome(richiestaUtente.getNome());
            }
            if (richiestaUtente.getDataDiNascita() != null) {
                newUtente.setDataDiNascita(richiestaUtente.getDataDiNascita());
            }
            if (richiestaUtente.getEmail() != null) {
                newUtente.setEmail(richiestaUtente.getEmail());
            }
            if (richiestaUtente.getCellulare() != null) {
                newUtente.setCellulare(richiestaUtente.getCellulare());
            }
            if (richiestaUtente.getPassword() != null) {
                newUtente.setPassword(richiestaUtente.getPassword());
            }
            return utenteRepository.save(newUtente);
        }
        throw new UtenteNotFoundException("Utente non trovato");
    }

    public Utente deposito(Long id, BigDecimal importoDeposito) {
        Optional<Utente> optionalUtente = utenteRepository.findById(id);
        if (optionalUtente.isEmpty()) {
            throw new UtenteNotFoundException("Utente non trovato");
        }
        Utente utente = optionalUtente.get();
        if (importoDeposito == null || importoDeposito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo del deposito non valido");
        }
        utente.setSaldo(utente.getSaldo().add(importoDeposito));

        return utenteRepository.save(utente);
    }

    public Utente prelievo(Long id, BigDecimal importoPrelievo) {
        Optional<Utente> optionalUtente = utenteRepository.findById(id);
        if (optionalUtente.isEmpty()) {
            throw new UtenteNotFoundException("Utente non trovato");
        }
        Utente utente = optionalUtente.get();
        if (importoPrelievo == null || importoPrelievo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo del prelievo non valido");
        }
        if (utente.getSaldo().compareTo(importoPrelievo) < 0) {
            throw new IllegalStateException("Saldo insufficiente per il prelievo richiesto");
        }
        utente.setSaldo(utente.getSaldo().subtract(importoPrelievo));
        return utenteRepository.save(utente);
    }

}




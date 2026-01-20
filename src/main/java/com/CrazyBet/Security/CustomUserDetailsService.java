package com.CrazyBet.Security;

import com.CrazyBet.Model.Utente;
import com.CrazyBet.Repository.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    public CustomUserDetailsService (UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utente con Username " + email + " non trovato."));

        return new UserDetailsAdapter(utente);
    }

}

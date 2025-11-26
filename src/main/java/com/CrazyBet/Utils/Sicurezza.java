package com.CrazyBet.Utils;

import com.CrazyBet.Model.Utente;

public final class Sicurezza {

    private Sicurezza() {}
    public static void verificaAdmin (Utente utente) {
        if (utente == null || utente.getRuolo() != Utente.Ruolo.ADMIN) {
            throw new SecurityException("Accesso negato: L'operazione può essere effettuata solo dall'Admin ");
        }
    }

}

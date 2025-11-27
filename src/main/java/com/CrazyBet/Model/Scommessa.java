package com.CrazyBet.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Scommesse")
public class Scommessa {

    public enum EsitoScommessa { VITTORIA_CASA, PAREGGIO, VITTORIA_OSPITE }
    public enum StatusScommessa { IN_ATTESA, VINTA, PERSA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true, message = "L'importo della scommessa deve essere minimo di € 1,00")
    private BigDecimal importoScommessa;

    @Enumerated(EnumType.STRING)
    private StatusScommessa statusScommessa;

    @Enumerated(EnumType.STRING)
    private EsitoScommessa esitoScommessaUtente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToMany
    @JoinTable(
            name = "scommessa_partita",
            joinColumns = @JoinColumn(name = "id_scommessa"),
            inverseJoinColumns = @JoinColumn(name = "id_partita")
    )
    private List<Partita> partite = new ArrayList<>();
}


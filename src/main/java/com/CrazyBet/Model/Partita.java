package com.CrazyBet.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Partite")
public class Partita {

    public enum Status { DA_GIOCARE, IN_CORSO, FINITA }
    public enum Esito { VITTORIA_CASA, PAREGGIO, VITTORIA_OSPITE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime oraInizioPartita;

    private LocalDateTime oraFinePartita;

    @NotNull
    private String cittaPartita;

    private Integer goalCasa;
    private Integer goalOspite;

    @Transient
    private int risultato;

    @Enumerated(EnumType.STRING)
    private Status statusPartita;

    @Enumerated(EnumType.STRING)
    private Esito esitoPartita;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_squadraCasa")
    private Squadra squadraCasa;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_squadraOspite")
    private Squadra squadraOspite;

    @ManyToMany(mappedBy = "partite")
    private List<Scommessa> scommesse = new ArrayList<>();
}

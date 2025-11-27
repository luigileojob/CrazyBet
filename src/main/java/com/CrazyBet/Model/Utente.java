package com.CrazyBet.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name="Utenti")
public class Utente {

    public enum Ruolo {USER, ADMIN}

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @NotNull
    private LocalDate dataDiNascita;

    @Email
    @Column(unique = true)
    private String email;

    private String cellulare;

    @Size(min = 8)
    private String password;
    private BigDecimal saldo = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @OneToMany(mappedBy = "utente")
    private List<Scommessa> scommesse;

}


package com.CrazyBet.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table (name = "Scommesse")
public class Scommessa {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @DecimalMin(value = "1.0",inclusive = true,message = ("L'importo della scommessa deve essere minimo di € 1,00"))
    private BigDecimal importoScommessa;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

}

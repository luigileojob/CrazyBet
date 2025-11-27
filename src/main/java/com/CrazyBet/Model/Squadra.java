package com.CrazyBet.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "Squadre")
public class Squadra {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true)
    private String nomeSquadra;

    @OneToMany (mappedBy = "squadraCasa")
    private List<Partita> partiteInCasa;

    @OneToMany (mappedBy = "squadraOspite")
    private List<Partita> partiteInTrasferta;

}

package com.refuge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refuge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du refuge est obligatoire")
    private String nom;

    private String adresse;

    private String telephone;

    private String email;

    @OneToMany(mappedBy = "refuge", cascade = CascadeType.ALL)
    private List<Animal> animaux = new ArrayList<>();

    private Long CapaciteMax;
    private String LogoUrl;



}

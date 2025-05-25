package com.refuge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refuge.model.Animal;
import com.refuge.model.EspeceAnimal;
import com.refuge.model.StatutAnimal;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByEspece(EspeceAnimal espece);

    List<Animal> findByStatut(StatutAnimal statut);

    List<Animal> findByEspeceAndStatut(EspeceAnimal espece, StatutAnimal statut);

    List<Animal> findByRefugeId(Long refugeId);

    List<Animal> findByEspeceAndRefugeId(EspeceAnimal espece, Long refugeId);

    List<Animal> findByNomContainingIgnoreCase(String nom);
}

package com.refuge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refuge.model.VisiteMedicale;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisiteMedicaleRepository extends JpaRepository<VisiteMedicale, Long> {

    List<VisiteMedicale> findByAnimalId(Long animalId);

    List<VisiteMedicale> findByDateVisiteBetween(LocalDate debut, LocalDate fin);

    List<VisiteMedicale> findByVeterinaire(String veterinaire);

    List<VisiteMedicale> findByVeterinaireContainingIgnoreCase(String veterinaire);

    List<VisiteMedicale> findByMotifContainingIgnoreCase(String motif);
}
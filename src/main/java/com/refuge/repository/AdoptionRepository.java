package com.refuge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refuge.model.Adoption;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    List<Adoption> findByFamilleId(Long familleId);

    Optional<Adoption> findByAnimalId(Long animalId);

    List<Adoption> findByDateAdoptionBetween(LocalDate debut, LocalDate fin);

    List<Adoption> findBySuiviPost(boolean suiviPost);
}
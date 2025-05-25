package com.refuge.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.refuge.model.Famille;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, Long> {

    List<Famille> findByNomContainingIgnoreCase(String nom);

    Optional<Famille> findByEmail(String email);

    List<Famille> findByTelephone(String telephone);

    boolean existsByEmail(@Email(message = "Format d'email invalide") @NotBlank(message = "L'email est obligatoire") String email);
    @Query("SELECT f FROM Famille f WHERE SIZE(f.adoptions) = 0")
    List<Famille> findFamillesWithoutAdoptions();
    @Query("SELECT f FROM Famille f JOIN f.adoptions a GROUP BY f HAVING COUNT(a) > 1")

    List<Famille> findFamillesWithMultipleAdoptions();

    List<Famille> findByEmailContainingIgnoreCase(String email);
}

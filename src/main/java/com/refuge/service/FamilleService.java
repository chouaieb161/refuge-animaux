package com.refuge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refuge.exception.ResourceNotFoundException;
import com.refuge.model.Famille;
import com.refuge.model.Adoption;
import com.refuge.repository.FamilleRepository;
import com.refuge.repository.AdoptionRepository;

import java.util.List;

@Service
public class FamilleService {

    private final FamilleRepository familleRepository;
    private final AdoptionRepository adoptionRepository;

    @Autowired
    public FamilleService(FamilleRepository familleRepository, AdoptionRepository adoptionRepository) {
        this.familleRepository = familleRepository;
        this.adoptionRepository = adoptionRepository;
    }

    public List<Famille> getAllFamilles() {
        return familleRepository.findAll();
    }

    public Famille getFamilleById(Long id) {
        return familleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Famille non trouvée avec l'id: " + id));
    }

    @Transactional
    public Famille createFamille(Famille famille) {
        // Vérifier si l'email existe déjà
        if (familleRepository.existsByEmail(famille.getEmail())) {
            throw new IllegalStateException("Une famille avec cet email existe déjà");
        }

        return familleRepository.save(famille);
    }

    @Transactional
    public Famille updateFamille(Long id, Famille familleDetails) {
        Famille famille = getFamilleById(id);

        // Vérifier si le nouvel email existe déjà et s'il est différent de l'email actuel
        if (!famille.getEmail().equals(familleDetails.getEmail()) &&
                familleRepository.existsByEmail(familleDetails.getEmail())) {
            throw new IllegalStateException("Une famille avec cet email existe déjà");
        }

        famille.setNom(familleDetails.getNom());
        famille.setPrenom(familleDetails.getPrenom());
        famille.setAdresse(familleDetails.getAdresse());
        famille.setTelephone(familleDetails.getTelephone());
        famille.setEmail(familleDetails.getEmail());
        famille.setNotes(familleDetails.getNotes());

        return familleRepository.save(famille);
    }

    @Transactional
    public void deleteFamille(Long id) {
        Famille famille = getFamilleById(id);

        // Vérifier si la famille a des adoptions
        List<Adoption> adoptions = adoptionRepository.findByFamilleId(id);
        if (!adoptions.isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer la famille car elle a déjà réalisé des adoptions");
        }

        familleRepository.delete(famille);
    }

    public List<Famille> searchFamillesByNom(String nom) {
        return familleRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Famille> searchFamillesByEmail(String email) {
        return familleRepository.findByEmailContainingIgnoreCase(email);
    }

    public int getNombreAdoptionsByFamille(Long familleId) {
        return adoptionRepository.findByFamilleId(familleId).size();
    }

    public List<Famille> getFamillesWithoutAdoptions() {
        return familleRepository.findFamillesWithoutAdoptions();
    }

    public List<Famille> getFamillesMultipleAdoptions() {
        return familleRepository.findFamillesWithMultipleAdoptions();
    }

    @Transactional
    public Famille updateFamilleNotes(Long id, String notes) {
        Famille famille = getFamilleById(id);
        famille.setNotes(notes);
        return familleRepository.save(famille);
    }
}

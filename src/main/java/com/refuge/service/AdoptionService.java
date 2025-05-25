package com.refuge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refuge.exception.ResourceNotFoundException;
import com.refuge.model.Adoption;
import com.refuge.model.Animal;
import com.refuge.model.Famille;
import com.refuge.model.StatutAnimal;
import com.refuge.repository.AdoptionRepository;
import com.refuge.repository.AnimalRepository;
import com.refuge.repository.FamilleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final FamilleRepository familleRepository;
    private final AnimalService animalService;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository,
                           AnimalRepository animalRepository,
                           FamilleRepository familleRepository,
                           AnimalService animalService) {
        this.adoptionRepository = adoptionRepository;
        this.animalRepository = animalRepository;
        this.familleRepository = familleRepository;
        this.animalService = animalService;
    }

    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    public Adoption getAdoptionById(Long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption non trouvée avec l'id: " + id));
    }

    @Transactional
    public Adoption createAdoption(Adoption adoption, Long animalId, Long familleId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal non trouvé avec l'id: " + animalId));

        Famille famille = familleRepository.findById(familleId)
                .orElseThrow(() -> new ResourceNotFoundException("Famille non trouvée avec l'id: " + familleId));

        // Vérifier si l'animal est déjà adopté
        if (animal.getStatut() == StatutAnimal.ADOPTE) {
            throw new IllegalStateException("Cet animal est déjà adopté");
        }

        adoption.setAnimal(animal);
        adoption.setFamille(famille);

        if (adoption.getDateAdoption() == null) {
            adoption.setDateAdoption(LocalDate.now());
        }

        // Mettre à jour le statut de l'animal
        animalService.updateAnimalStatut(animalId, StatutAnimal.ADOPTE);

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public Adoption updateAdoption(Long id, Adoption adoptionDetails) {
        Adoption adoption = getAdoptionById(id);

        adoption.setDateAdoption(adoptionDetails.getDateAdoption());
        adoption.setCommentaires(adoptionDetails.getCommentaires());
        adoption.setFraisAdoption(adoptionDetails.getFraisAdoption());
        adoption.setDateContrat(adoptionDetails.getDateContrat());
        adoption.setSuiviPost(adoptionDetails.isSuiviPost());
        adoption.setDateSuivi(adoptionDetails.getDateSuivi());

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public void deleteAdoption(Long id) {
        Adoption adoption = getAdoptionById(id);
        Long animalId = adoption.getAnimal().getId();

        // Remettre l'animal en disponible
        animalService.updateAnimalStatut(animalId, StatutAnimal.DISPONIBLE);

        adoptionRepository.delete(adoption);
    }

    public List<Adoption> getAdoptionsByFamille(Long familleId) {
        return adoptionRepository.findByFamilleId(familleId);
    }

    public List<Adoption> getAdoptionsBetweenDates(LocalDate debut, LocalDate fin) {
        return adoptionRepository.findByDateAdoptionBetween(debut, fin);
    }

    @Transactional
    public void enregistrerSuiviPost(Long adoptionId, LocalDate dateSuivi) {
        Adoption adoption = getAdoptionById(adoptionId);
        adoption.setSuiviPost(true);
        adoption.setDateSuivi(dateSuivi);
        adoptionRepository.save(adoption);
    }

    public List<Adoption> getAdoptionsSansSuivi() {
        return adoptionRepository.findBySuiviPost(false);
    }
}
package com.refuge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refuge.exception.ResourceNotFoundException;
import com.refuge.model.Refuge;
import com.refuge.model.Animal;
import com.refuge.repository.RefugeRepository;
import com.refuge.repository.AnimalRepository;

import java.util.List;

@Service
public class RefugeService {

    private final RefugeRepository refugeRepository;
    private final AnimalRepository animalRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public RefugeService(RefugeRepository refugeRepository,
                         AnimalRepository animalRepository,
                         FileStorageService fileStorageService) {
        this.refugeRepository = refugeRepository;
        this.animalRepository = animalRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Refuge> getAllRefuges() {
        return refugeRepository.findAll();
    }

    public Refuge getRefugeById(Long id) {
        return refugeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refuge non trouvé avec l'id: " + id));
    }

    @Transactional
    public Refuge createRefuge(Refuge refuge) {
        return refugeRepository.save(refuge);
    }

    @Transactional
    public Refuge updateRefuge(Long id, Refuge refugeDetails) {
        Refuge refuge = getRefugeById(id);

        refuge.setNom(refugeDetails.getNom());
        refuge.setAdresse(refugeDetails.getAdresse());
        refuge.setTelephone(refugeDetails.getTelephone());
        refuge.setEmail(refugeDetails.getEmail());
        refuge.setCapaciteMax(refugeDetails.getCapaciteMax());

        // Ne pas écraser l'URL du logo si non fournie
        if (refugeDetails.getLogoUrl() != null && !refugeDetails.getLogoUrl().isEmpty()) {
            // Supprimer l'ancien logo si existant
            if (refuge.getLogoUrl() != null && !refuge.getLogoUrl().isEmpty()) {
                fileStorageService.deleteFile(refuge.getLogoUrl());
            }
            refuge.setLogoUrl(refugeDetails.getLogoUrl());
        }

        return refugeRepository.save(refuge);
    }

    @Transactional
    public void deleteRefuge(Long id) {
        Refuge refuge = getRefugeById(id);

        // Vérifier s'il y a des animaux dans ce refuge
        List<Animal> animaux = animalRepository.findByRefugeId(id);
        if (!animaux.isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer le refuge car il contient encore des animaux");
        }

        // Supprimer le logo si existant

        refugeRepository.delete(refuge);
    }

    public List<Refuge> searchRefugesByNom(String nom) {
        return refugeRepository.findByNomContainingIgnoreCase(nom);
    }

    public int getNombreAnimauxByRefuge(Long refugeId) {
        return animalRepository.findByRefugeId(refugeId).size();
    }

    public boolean hasCapacityForMoreAnimals(Long refugeId) {
        Refuge refuge = getRefugeById(refugeId);
        int currentAnimalCount = animalRepository.findByRefugeId(refugeId).size();
        return currentAnimalCount < refuge.getCapaciteMax();
    }
}

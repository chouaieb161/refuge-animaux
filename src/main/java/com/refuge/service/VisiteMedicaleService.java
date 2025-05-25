package com.refuge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refuge.exception.ResourceNotFoundException;
import com.refuge.model.VisiteMedicale;
import com.refuge.model.Animal;
import com.refuge.model.StatutAnimal;
import com.refuge.repository.VisiteMedicaleRepository;
import com.refuge.repository.AnimalRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VisiteMedicaleService {

    private final VisiteMedicaleRepository visiteMedicaleRepository;
    private final AnimalRepository animalRepository;
    private final AnimalService animalService;

    @Autowired
    public VisiteMedicaleService(VisiteMedicaleRepository visiteMedicaleRepository,
                                 AnimalRepository animalRepository,
                                 AnimalService animalService) {
        this.visiteMedicaleRepository = visiteMedicaleRepository;
        this.animalRepository = animalRepository;
        this.animalService = animalService;
    }

    public List<VisiteMedicale> getAllVisitesMedicales() {
        return visiteMedicaleRepository.findAll();
    }

    public VisiteMedicale getVisiteMedicaleById(Long id) {
        return visiteMedicaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visite médicale non trouvée avec l'id: " + id));
    }

    @Transactional
    public VisiteMedicale createVisiteMedicale(VisiteMedicale visiteMedicale, Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal non trouvé avec l'id: " + animalId));

        visiteMedicale.setAnimal(animal);

        // Si la date de visite n'est pas fournie, utiliser la date du jour
        if (visiteMedicale.getDateVisite() == null) {
            visiteMedicale.setDateVisite(LocalDate.now());
        }

        // Si le motif de la visite contient des mots-clés liés à une maladie ou à un traitement
        // mettre l'animal en statut EN_SOINS
        String motif = visiteMedicale.getMotif().toLowerCase();
        String observations = visiteMedicale.getObservations() != null ?
                visiteMedicale.getObservations().toLowerCase() : "";

        if ((motif.contains("maladie") || motif.contains("traitement") ||
                motif.contains("opération") || motif.contains("blessure") ||
                observations.contains("maladie") || observations.contains("traitement") ||
                observations.contains("opération") || observations.contains("blessure")) &&
                animal.getStatut() != StatutAnimal.ADOPTE) {

            animalService.updateAnimalStatut(animalId, StatutAnimal.EN_SOINS);
        }

        return visiteMedicaleRepository.save(visiteMedicale);
    }

    @Transactional
    public VisiteMedicale updateVisiteMedicale(Long id, VisiteMedicale visiteMedicaleDetails) {
        VisiteMedicale visiteMedicale = getVisiteMedicaleById(id);

        visiteMedicale.setDateVisite(visiteMedicaleDetails.getDateVisite());
        visiteMedicale.setMotif(visiteMedicaleDetails.getMotif());
        visiteMedicale.setObservations(visiteMedicaleDetails.getObservations());
        visiteMedicale.setTraitement(visiteMedicaleDetails.getTraitement());
        visiteMedicale.setVeterinaire(visiteMedicaleDetails.getVeterinaire());

        return visiteMedicaleRepository.save(visiteMedicale);
    }

    @Transactional
    public void deleteVisiteMedicale(Long id) {
        VisiteMedicale visiteMedicale = getVisiteMedicaleById(id);
        visiteMedicaleRepository.delete(visiteMedicale);
    }

    public List<VisiteMedicale> getVisiteMedicalesByAnimal(Long animalId) {
        return visiteMedicaleRepository.findByAnimalId(animalId);
    }

    public List<VisiteMedicale> getVisiteMedicalesBetweenDates(LocalDate debut, LocalDate fin) {
        return visiteMedicaleRepository.findByDateVisiteBetween(debut, fin);
    }

    @Transactional
    public void declarerAnimalGueri(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal non trouvé avec l'id: " + animalId));

        if (animal.getStatut() == StatutAnimal.EN_SOINS) {
            animalService.updateAnimalStatut(animalId, StatutAnimal.DISPONIBLE);
        }
    }

    public List<VisiteMedicale> getVisitesMedicalesByVeterinaire(String veterinaire) {
        return visiteMedicaleRepository.findByVeterinaireContainingIgnoreCase(veterinaire);
    }

    public List<VisiteMedicale> getVisitesMedicalesByMotif(String motif) {
        return visiteMedicaleRepository.findByMotifContainingIgnoreCase(motif);
    }
}

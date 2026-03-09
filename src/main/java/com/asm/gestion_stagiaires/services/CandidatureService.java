package com.asm.gestion_stagiaires.services;

import com.asm.gestion_stagiaires.models.Candidature;
import com.asm.gestion_stagiaires.models.StatusCandidature;
import com.asm.gestion_stagiaires.models.SujetStage;
import com.asm.gestion_stagiaires.models.Utilisateur;
import com.asm.gestion_stagiaires.repositories.CandidatureRepository;
import com.asm.gestion_stagiaires.repositories.SujetStageRepository;
import com.asm.gestion_stagiaires.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private SujetStageRepository sujetStageRepository; // Pour lier la Table 3

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Pour lier la Table 1

    // Sauvegarder et initialiser la candidature avec les liaisons
    public Candidature saveCandidature(Candidature candidature, Long sujetId, String email) {

        // 1. Récupérer le sujet cible
        SujetStage sujet = sujetStageRepository.findById(sujetId)
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));

        // 2. Récupérer le stagiaire connecté via son email
        Utilisateur stagiaire = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Stagiaire non trouvé"));

        // 3. Initialisation automatique
        candidature.setSujet(sujet); // Lie à la Table 3
        candidature.setStagiaire(stagiaire); // Lie à la Table 1
        candidature.setStatut(StatusCandidature.EN_ATTENTE); //
        candidature.setDateDepot(LocalDate.now()); //

        return candidatureRepository.save(candidature);
    }

    // Récupérer toutes les candidatures pour le RH
    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    // Récupérer les candidatures d'un stagiaire spécifique
    public List<Candidature> getCandidaturesByStagiaire(Long stagiaireId) {
        return candidatureRepository.findAll()
                .stream()
                .filter(c -> c.getStagiaire().getId().equals(stagiaireId))
                .toList();
    }

    // Filtrer par statut
    public List<Candidature> getCandidaturesParStatut(StatusCandidature statut) {
        return candidatureRepository.findAll()
                .stream()
                .filter(c -> c.getStatut() == statut)
                .toList();
    }
}
package com.asm.gestion_stagiaires.services;

import com.asm.gestion_stagiaires.models.*;
import com.asm.gestion_stagiaires.repositories.StagiaireRepository;
import com.asm.gestion_stagiaires.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StagiaireService {

    @Autowired private StagiaireRepository stagiaireRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;

    public Stagiaire creerStagiaire(Utilisateur utilisateur, Candidature candidature) {
        Stagiaire stagiaire = new Stagiaire();
        stagiaire.setUtilisateur(utilisateur);
        stagiaire.setCandidature(candidature);
        stagiaire.setSujet(candidature.getSujet());
        stagiaire.setDateDebut(LocalDate.now());
        stagiaire.setStatusStage(StatusStage.EN_COURS);
        return stagiaireRepository.save(stagiaire);
    }

    public List<Stagiaire> getAllStagiaires() {
        return stagiaireRepository.findAll();
    }

    public Stagiaire getStagiaireByUtilisateurId(Long utilisateurId) {
        return stagiaireRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Stagiaire non trouvé"));
    }

    public boolean existsByUtilisateurId(Long utilisateurId) {
        return stagiaireRepository.existsByUtilisateurId(utilisateurId);
    }

    public Stagiaire terminerStage(Long id) {
        Stagiaire stagiaire = stagiaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stagiaire non trouvé"));
        stagiaire.setStatusStage(StatusStage.TERMINE);
        stagiaire.setDateFin(LocalDate.now());
        return stagiaireRepository.save(stagiaire);
    }

    public Stagiaire affecterEncadrant(Long stagiaireId, Long encadrantId) {
        Stagiaire stagiaire = stagiaireRepository.findById(stagiaireId)
                .orElseThrow(() -> new RuntimeException("Stagiaire non trouvé"));
        Utilisateur encadrant = utilisateurRepository.findById(encadrantId)
                .orElseThrow(() -> new RuntimeException("Encadrant non trouvé"));
        stagiaire.setEncadrant(encadrant);
        return stagiaireRepository.save(stagiaire);
    }

    public List<Utilisateur> getEncadrants() {
        return utilisateurRepository.findByRole(Role.ROLE_ENCADRANT);
    }
}
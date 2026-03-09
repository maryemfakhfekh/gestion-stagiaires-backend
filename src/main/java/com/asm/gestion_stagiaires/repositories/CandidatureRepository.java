package com.asm.gestion_stagiaires.repositories;

import com.asm.gestion_stagiaires.models.Candidature;
import com.asm.gestion_stagiaires.models.StatusCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    // Pour récupérer les candidatures d'un stagiaire spécifique
    List<Candidature> findByStagiaireId(Long stagiaireId);

    // Pour filtrer les candidatures par statut (EN_ATTENTE, ACCEPTE, etc.)
    List<Candidature> findByStatut(StatusCandidature statut);
}
package com.asm.gestion_stagiaires.repositories;

import com.asm.gestion_stagiaires.models.Stagiaire;
import com.asm.gestion_stagiaires.models.StatusStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StagiaireRepository extends JpaRepository<Stagiaire, Long> {
    Optional<Stagiaire> findByUtilisateurId(Long utilisateurId);
    List<Stagiaire> findByStatusStage(StatusStage statusStage);
    boolean existsByUtilisateurId(Long utilisateurId);
}
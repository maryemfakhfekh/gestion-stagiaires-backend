package com.asm.gestion_stagiaires.repositories;

import com.asm.gestion_stagiaires.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Cette méthode permettra de vérifier si un email existe lors de la connexion
    Optional<Utilisateur> findByEmail(String email);
}

package com.asm.gestion_stagiaires.repositories;

import com.asm.gestion_stagiaires.models.Role;
import com.asm.gestion_stagiaires.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
}
package com.asm.gestion_stagiaires.Controller;

import com.asm.gestion_stagiaires.models.Stagiaire;
import com.asm.gestion_stagiaires.models.Utilisateur;
import com.asm.gestion_stagiaires.services.CandidatureService;
import com.asm.gestion_stagiaires.services.StagiaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/stagiaires")
@CrossOrigin("*")
public class StagiaireController {

    @Autowired private StagiaireService stagiaireService;
    @Autowired private CandidatureService candidatureService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public List<Stagiaire> getAllStagiaires() {
        return stagiaireService.getAllStagiaires();
    }

    @GetMapping("/mon-dossier")
    @PreAuthorize("hasAuthority('ROLE_STAGIAIRE')")
    public ResponseEntity<Stagiaire> getMonDossier(Principal principal) {
        Utilisateur utilisateur = candidatureService.getUtilisateurByEmail(principal.getName());
        Stagiaire stagiaire = stagiaireService.getStagiaireByUtilisateurId(utilisateur.getId());
        return ResponseEntity.ok(stagiaire);
    }

    @GetMapping("/has-dossier")
    @PreAuthorize("hasAuthority('ROLE_STAGIAIRE')")
    public ResponseEntity<Boolean> hasDossier(Principal principal) {
        Utilisateur utilisateur = candidatureService.getUtilisateurByEmail(principal.getName());
        boolean exists = stagiaireService.existsByUtilisateurId(utilisateur.getId());
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/encadrants")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public List<Utilisateur> getEncadrants() {
        return stagiaireService.getEncadrants();
    }

    @PutMapping("/{id}/affecter-encadrant")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<Stagiaire> affecterEncadrant(
            @PathVariable Long id,
            @RequestParam Long encadrantId) {
        return ResponseEntity.ok(stagiaireService.affecterEncadrant(id, encadrantId));
    }

    @PutMapping("/{id}/terminer")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<Stagiaire> terminerStage(@PathVariable Long id) {
        return ResponseEntity.ok(stagiaireService.terminerStage(id));
    }
}
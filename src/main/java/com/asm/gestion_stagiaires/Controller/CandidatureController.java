package com.asm.gestion_stagiaires.Controller;

import com.asm.gestion_stagiaires.models.Candidature;
import com.asm.gestion_stagiaires.models.StatusCandidature;
import com.asm.gestion_stagiaires.repositories.CandidatureRepository;
import com.asm.gestion_stagiaires.services.CandidatureService;
import com.asm.gestion_stagiaires.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin("*")
public class CandidatureController {

    @Autowired private CandidatureService    candidatureService;
    @Autowired private FileStorageService    fileStorageService;
    @Autowired private CandidatureRepository candidatureRepository;

    @PostMapping("/postuler")
    public ResponseEntity<Candidature> postuler(
            @RequestParam("File")    MultipartFile file,
            @RequestParam("sujetId") Long sujetId,
            Principal principal) {
        String fileName = fileStorageService.save(file);
        Candidature candidature = new Candidature();
        candidature.setCvPath(fileName);
        return ResponseEntity.ok(
                candidatureService.saveCandidature(candidature, sujetId, principal.getName()));
    }

    @GetMapping
    public List<Candidature> voirCandidatures(@RequestParam(required = false) Long stagiaireId) {
        if (stagiaireId != null) return candidatureService.getCandidaturesByStagiaire(stagiaireId);
        return candidatureService.getAllCandidatures();
    }

    @PutMapping("/{id}/accepter")
    public ResponseEntity<Candidature> accepter(@PathVariable Long id) {
        return candidatureRepository.findById(id).map(c -> {
            c.setStatut(StatusCandidature.ACCEPTE);
            return ResponseEntity.ok(candidatureRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/refuser")
    public ResponseEntity<Candidature> refuser(@PathVariable Long id) {
        return candidatureRepository.findById(id).map(c -> {
            c.setStatut(StatusCandidature.REFUSEE);
            return ResponseEntity.ok(candidatureRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/entretien")
    public ResponseEntity<Candidature> planifierEntretien(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return candidatureRepository.findById(id).map(c -> {
            c.setDateEntretien(LocalDateTime.parse(body.get("dateEntretien")));
            return ResponseEntity.ok(candidatureRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!candidatureRepository.existsById(id)) return ResponseEntity.notFound().build();
        candidatureRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
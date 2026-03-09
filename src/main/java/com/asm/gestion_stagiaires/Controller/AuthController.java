package com.asm.gestion_stagiaires.Controller;

import com.asm.gestion_stagiaires.config.JwtUtils;
import com.asm.gestion_stagiaires.models.LoginRequest;
import com.asm.gestion_stagiaires.models.Utilisateur;
import com.asm.gestion_stagiaires.repositories.UtilisateurRepository;
import com.asm.gestion_stagiaires.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UtilisateurRepository utilisateurRepository;

    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody Utilisateur user) {
        return ResponseEntity.ok(authService.inscription(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Utilisateur user = authService.login(loginRequest);
            String token = jwtUtils.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("id",          user.getId());
            response.put("token",       token);
            response.put("email",       user.getEmail());
            response.put("role",        user.getRole());
            response.put("nomComplet",  user.getNomComplet());
            response.put("telephone",   user.getTelephone()); // ✅ ajouté
            response.put("cycle",       user.getCycle());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Utilisateur updatedData) {
        return utilisateurRepository.findById(id).map(user -> {
            user.setFiliere(updatedData.getFiliere());
            user.setCycle(updatedData.getCycle());
            user.setEtablissement(updatedData.getEtablissement());
            user.setTelephone(updatedData.getTelephone());
            user.setDateNaissance(updatedData.getDateNaissance());
            utilisateurRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Profil stagiaire complété !"));
        }).orElse(ResponseEntity.notFound().build());
    }
}
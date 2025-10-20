package ma.jaouad.bank.virementservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.jaouad.bank.virementservice.clients.BeneficiaireClient;
import ma.jaouad.bank.virementservice.entities.Virement;
import ma.jaouad.bank.virementservice.repositories.VirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/virements")
@Tag(name = "Virement", description = "API de gestion des virements")
public class VirementController {

    @Autowired
    private VirementRepository virementRepository;

    @Autowired
    private BeneficiaireClient beneficiaireClient;

    @GetMapping("/full/{id}")
    @Operation(summary = "Récupérer un virement avec les détails du bénéficiaire")
    public ResponseEntity<Virement> getVirementWithBeneficiaire(@PathVariable Long id) {
        Virement virement = virementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Virement introuvable avec l'id: " + id));
        
        try {
            virement.setBeneficiaire(beneficiaireClient.getBeneficiaireById(virement.getIdBeneficiaire()));
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du bénéficiaire: " + e.getMessage());
        }
        
        return ResponseEntity.ok(virement);
    }

    @GetMapping("/full")
    @Operation(summary = "Récupérer tous les virements avec les détails des bénéficiaires")
    public ResponseEntity<List<Virement>> getAllVirementsWithBeneficiaires() {
        List<Virement> virements = virementRepository.findAll();
        
        virements.forEach(virement -> {
            try {
                virement.setBeneficiaire(beneficiaireClient.getBeneficiaireById(virement.getIdBeneficiaire()));
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du bénéficiaire: " + e.getMessage());
            }
        });
        
        return ResponseEntity.ok(virements);
    }
}

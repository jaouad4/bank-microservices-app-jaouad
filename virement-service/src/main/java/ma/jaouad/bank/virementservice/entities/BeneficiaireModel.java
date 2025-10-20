package ma.jaouad.bank.virementservice.entities;

import lombok.Data;

@Data
public class BeneficiaireModel {
    private Long id;
    private String nom;
    private String prenom;
    private String rib;
    private String type;
}

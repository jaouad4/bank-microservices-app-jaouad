package ma.jaouad.bank.beneficiaireservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.jaouad.bank.beneficiaireservice.enums.TypeBeneficiaire;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiaire {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true, length = 24)
    private String rib;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeBeneficiaire type;
}

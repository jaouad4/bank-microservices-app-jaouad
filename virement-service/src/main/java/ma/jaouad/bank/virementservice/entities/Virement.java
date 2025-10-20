package ma.jaouad.bank.virementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.jaouad.bank.virementservice.enums.TypeVirement;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Virement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long idBeneficiaire;
    
    @Column(nullable = false, length = 24)
    private String ribSource;
    
    @Column(nullable = false)
    private Double montant;
    
    @Column(length = 500)
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateVirement;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVirement type;
    
    @Transient
    private BeneficiaireModel beneficiaire;
}

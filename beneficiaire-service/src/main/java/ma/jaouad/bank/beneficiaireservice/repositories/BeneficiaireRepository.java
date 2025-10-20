package ma.jaouad.bank.beneficiaireservice.repositories;

import ma.jaouad.bank.beneficiaireservice.entities.Beneficiaire;
import ma.jaouad.bank.beneficiaireservice.enums.TypeBeneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "beneficiaires")
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long> {
    
    @RestResource(path = "byType")
    List<Beneficiaire> findByType(TypeBeneficiaire type);
    
    @RestResource(path = "byRib")
    Beneficiaire findByRib(String rib);
    
    @RestResource(path = "byNom")
    List<Beneficiaire> findByNomContainingIgnoreCase(String nom);
}

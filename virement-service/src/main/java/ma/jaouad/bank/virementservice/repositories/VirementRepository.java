package ma.jaouad.bank.virementservice.repositories;

import ma.jaouad.bank.virementservice.entities.Virement;
import ma.jaouad.bank.virementservice.enums.TypeVirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "virements")
public interface VirementRepository extends JpaRepository<Virement, Long> {
    
    @RestResource(path = "byBeneficiaire")
    List<Virement> findByIdBeneficiaire(Long idBeneficiaire);
    
    @RestResource(path = "byType")
    List<Virement> findByType(TypeVirement type);
    
    @RestResource(path = "byRibSource")
    List<Virement> findByRibSource(String ribSource);
    
    @RestResource(path = "byDateBetween")
    List<Virement> findByDateVirementBetween(Date startDate, Date endDate);
}

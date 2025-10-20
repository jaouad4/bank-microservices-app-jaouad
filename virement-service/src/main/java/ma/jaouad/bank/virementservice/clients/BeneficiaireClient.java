package ma.jaouad.bank.virementservice.clients;

import ma.jaouad.bank.virementservice.entities.BeneficiaireModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "beneficiaire-service")
public interface BeneficiaireClient {
    
    @GetMapping("/api/beneficiaires/{id}")
    BeneficiaireModel getBeneficiaireById(@PathVariable Long id);
}

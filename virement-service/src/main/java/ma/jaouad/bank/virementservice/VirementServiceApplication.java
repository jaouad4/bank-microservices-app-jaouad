package ma.jaouad.bank.virementservice;

import ma.jaouad.bank.virementservice.entities.Virement;
import ma.jaouad.bank.virementservice.enums.TypeVirement;
import ma.jaouad.bank.virementservice.repositories.VirementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@EnableFeignClients
public class VirementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirementServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(VirementRepository virementRepository) {
        return args -> {
            // Création de virements normaux
            virementRepository.save(Virement.builder()
                    .idBeneficiaire(1L)
                    .ribSource("MA000111222333444555666777")
                    .montant(5000.0)
                    .description("Paiement de loyer")
                    .dateVirement(new Date())
                    .type(TypeVirement.NORMAL)
                    .build());

            virementRepository.save(Virement.builder()
                    .idBeneficiaire(2L)
                    .ribSource("MA000111222333444555666777")
                    .montant(1500.0)
                    .description("Facture électricité")
                    .dateVirement(new Date())
                    .type(TypeVirement.NORMAL)
                    .build());

            virementRepository.save(Virement.builder()
                    .idBeneficiaire(3L)
                    .ribSource("MA000111222333444555666777")
                    .montant(2500.0)
                    .description("Achat matériel informatique")
                    .dateVirement(new Date())
                    .type(TypeVirement.INSTANTANE)
                    .build());

            // Création de virements instantanés
            virementRepository.save(Virement.builder()
                    .idBeneficiaire(4L)
                    .ribSource("MA000111222333444555666777")
                    .montant(850.0)
                    .description("Abonnement Maroc Telecom")
                    .dateVirement(new Date())
                    .type(TypeVirement.NORMAL)
                    .build());

            virementRepository.save(Virement.builder()
                    .idBeneficiaire(5L)
                    .ribSource("MA000111222333444555666777")
                    .montant(15000.0)
                    .description("Virement urgent")
                    .dateVirement(new Date())
                    .type(TypeVirement.INSTANTANE)
                    .build());

            virementRepository.save(Virement.builder()
                    .idBeneficiaire(6L)
                    .ribSource("MA000111222333444555666777")
                    .montant(450.0)
                    .description("Billet de train ONCF")
                    .dateVirement(new Date())
                    .type(TypeVirement.INSTANTANE)
                    .build());

            System.out.println("============================================");
            System.out.println("Virements initialisés avec succès !");
            System.out.println("Total: " + virementRepository.count());
            System.out.println("============================================");
        };
    }
}

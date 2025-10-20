package ma.jaouad.bank.beneficiaireservice;

import ma.jaouad.bank.beneficiaireservice.entities.Beneficiaire;
import ma.jaouad.bank.beneficiaireservice.enums.TypeBeneficiaire;
import ma.jaouad.bank.beneficiaireservice.repositories.BeneficiaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeneficiaireServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeneficiaireServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(BeneficiaireRepository beneficiaireRepository) {
        return args -> {
            // Création de bénéficiaires physiques
            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("ALAMI")
                    .prenom("Hassan")
                    .rib("MA001234567890123456789012")
                    .type(TypeBeneficiaire.PHYSIQUE)
                    .build());

            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("BENJELLOUN")
                    .prenom("Fatima")
                    .rib("MA002345678901234567890123")
                    .type(TypeBeneficiaire.PHYSIQUE)
                    .build());

            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("IDRISSI")
                    .prenom("Mohamed")
                    .rib("MA003456789012345678901234")
                    .type(TypeBeneficiaire.PHYSIQUE)
                    .build());

            // Création de bénéficiaires moraux (entreprises)
            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("MAROC TELECOM")
                    .prenom("SA")
                    .rib("MA004567890123456789012345")
                    .type(TypeBeneficiaire.MORALE)
                    .build());

            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("BANK AL-MAGHRIB")
                    .prenom("Etablissement Public")
                    .rib("MA005678901234567890123456")
                    .type(TypeBeneficiaire.MORALE)
                    .build());

            beneficiaireRepository.save(Beneficiaire.builder()
                    .nom("ONCF")
                    .prenom("Office")
                    .rib("MA006789012345678901234567")
                    .type(TypeBeneficiaire.MORALE)
                    .build());

            System.out.println("============================================");
            System.out.println("Bénéficiaires initialisés avec succès !");
            System.out.println("Total: " + beneficiaireRepository.count());
            System.out.println("============================================");
        };
    }
}

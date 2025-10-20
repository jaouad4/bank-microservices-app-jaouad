package ma.jaouad.bank.chatbotservice.services;

import lombok.RequiredArgsConstructor;
import ma.jaouad.bank.chatbotservice.models.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            Vous êtes un assistant bancaire intelligent spécialisé dans les services bancaires marocains.
            Vous aidez les clients avec leurs questions concernant:
            - Les virements bancaires (normaux et instantanés)
            - La gestion des bénéficiaires
            - Les RIB et comptes bancaires
            - Les services bancaires en ligne
            - Les frais et commissions
            
            Répondez de manière professionnelle, claire et concise en français.
            Si vous ne connaissez pas la réponse, dites-le honnêtement et proposez de contacter le service client.
            
            Contexte des documents disponibles:
            {context}
            """;

    public ChatResponse askQuestion(String question) {
        try {
            // Contexte bancaire prédéfini
            String bankContext = """
                    Services Bancaires Disponibles:
                    
                    1. VIREMENTS:
                    - Virement Normal: Traité sous 24-48h, frais standard de 5 DH
                    - Virement Instantané: Traité immédiatement, frais de 10 DH
                    - Limite quotidienne: 50,000 DH pour les particuliers
                    - Limite mensuelle: 200,000 DH pour les particuliers
                    
                    2. BÉNÉFICIAIRES:
                    - Bénéficiaire Physique: Personne individuelle avec RIB
                    - Bénéficiaire Moral: Entreprise ou organisation avec RIB
                    - Format RIB: 24 caractères (MA + 22 chiffres)
                    - Validation RIB obligatoire avant ajout
                    
                    3. SÉCURITÉ:
                    - Authentification à deux facteurs (2FA) obligatoire
                    - Code de confirmation par SMS pour les virements > 5,000 DH
                    - Plafond de sécurité personnalisable
                    - Notification instantanée pour chaque transaction
                    
                    4. HORAIRES DE SERVICE:
                    - Virements normaux: Lundi-Vendredi 8h-20h
                    - Virements instantanés: 24h/24, 7j/7
                    - Support client: 8h-22h tous les jours
                    - Support technique: support@bank.ma
                    """;

            // Créer le prompt système avec le contexte
            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
            Message systemMessage = systemPromptTemplate.createMessage(Map.of("context", bankContext));
            
            // Créer le message utilisateur
            UserMessage userMessage = new UserMessage(question);
            
            // Créer le prompt complet
            Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
            
            // Obtenir la réponse du modèle
            String answer = chatClient.prompt(prompt).call().content();
            
            // Construire la réponse
            List<String> sources = new ArrayList<>();
            sources.add("Base de connaissances bancaire");
            sources.add("Politique des services bancaires");
            
            return ChatResponse.builder()
                    .answer(answer)
                    .sources(sources)
                    .build();
                    
        } catch (Exception e) {
            return ChatResponse.builder()
                    .answer("Désolé, une erreur s'est produite. Veuillez réessayer ou contacter notre service client au 0800-123-456.")
                    .sources(List.of("Erreur: " + e.getMessage()))
                    .build();
        }
    }

    public ChatResponse getSimpleAnswer(String question) {
        // Version simplifiée sans RAG pour les questions basiques
        String answer = chatClient.prompt()
                .user(question)
                .call()
                .content();
        
        return ChatResponse.builder()
                .answer(answer)
                .sources(List.of("GPT-4o"))
                .build();
    }
}

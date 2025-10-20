# 🏛️ ARCHITECTURE TECHNIQUE DÉTAILLÉE

## Vue d'ensemble

Cette application bancaire suit les principes de l'architecture micro-services avec Spring Cloud.

## 📊 Diagramme d'Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                          CLIENT LAYER                                │
│  (Web Browser, Mobile App, Postman, etc.)                           │
└────────────────────────────┬────────────────────────────────────────┘
                             │
                             │ HTTP Requests
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      API GATEWAY (Port 8080)                         │
│  • Spring Cloud Gateway                                              │
│  • Load Balancing                                                    │
│  • Dynamic Routing via Eureka                                        │
│  • Single Entry Point                                                │
└────────────┬───────────────┬────────────────┬────────────────────────┘
             │               │                │
             │               │                │
   ┌─────────▼─────┐  ┌─────▼─────┐  ┌──────▼──────┐
   │ Beneficiaire  │  │ Virement   │  │  Chat-Bot   │
   │   Service     │  │  Service   │  │   Service   │
   │  Port 8081    │  │ Port 8082  │  │  Port 8083  │
   └───────┬───────┘  └─────┬──────┘  └──────┬──────┘
           │                │                 │
           │                │ Feign Client    │
           │                └────────┐        │
           │                         │        │
           ▼                         ▼        ▼
   ┌────────────────────────────────────────────┐
   │           H2 Databases (In-Memory)         │
   │  • beneficiairedb                          │
   │  • virementdb                              │
   └────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE SERVICES                            │
│                                                                       │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │ Discovery Service│  │  Config Service  │  │   Spring AI      │  │
│  │  (Eureka Server) │  │  (Git Backend)   │  │   (GPT-4o)       │  │
│  │   Port 8761      │  │   Port 8888      │  │                  │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
└──────────────────────────────────────────────────────────────────────┘
```

## 🔧 Composants Techniques

### 1. Discovery Service (Eureka Server)

**Rôle :** Registry centralisé pour tous les microservices

**Technologies :**
- Spring Cloud Netflix Eureka Server
- Port : 8761

**Fonctionnalités :**
- Enregistrement automatique des services
- Health check périodique
- Dashboard de monitoring
- Service discovery dynamique
- Load balancing côté client

**Configuration clé :**
```yaml
eureka:
  client:
    register-with-eureka: false    # Ne s'enregistre pas lui-même
    fetch-registry: false           # Ne récupère pas le registre
```

### 2. Config Service

**Rôle :** Gestion centralisée des configurations

**Technologies :**
- Spring Cloud Config Server
- Git comme backend de stockage
- Port : 8888

**Fonctionnalités :**
- Configuration externalisée
- Support de profils (dev, prod, test)
- Rafraîchissement dynamique avec @RefreshScope
- Encryption des propriétés sensibles
- Version control des configurations

**Repository Git :**
- Localisation : `C:\Users\JD\config-repo\`
- Fichiers :
  - `beneficiaire-service.yml`
  - `virement-service.yml`
  - `gateway-service.yml`
  - `chat-bot-service.yml`

### 3. Gateway Service

**Rôle :** Point d'entrée unique pour toutes les requêtes API

**Technologies :**
- Spring Cloud Gateway (Reactive)
- Spring WebFlux
- Port : 8080

**Fonctionnalités :**
- Routage dynamique basé sur Eureka
- Load balancing automatique
- Filtres globaux (logging, auth, etc.)
- Circuit breaker (Resilience4j)
- Rate limiting

**Routes configurées :**
```yaml
routes:
  - id: beneficiaire-service
    uri: lb://beneficiaire-service
    predicates:
      - Path=/api/beneficiaires/**
  
  - id: virement-service
    uri: lb://virement-service
    predicates:
      - Path=/api/virements/**
  
  - id: chat-bot-service
    uri: lb://chat-bot-service
    predicates:
      - Path=/api/chat/**
```

### 4. Beneficiaire Service

**Rôle :** Gestion des bénéficiaires bancaires

**Technologies :**
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Data REST
- H2 Database (in-memory)
- Lombok
- SpringDoc OpenAPI
- Port : 8081

**Architecture interne :**
```
beneficiaire-service/
├── entities/
│   └── Beneficiaire.java (JPA Entity)
├── enums/
│   └── TypeBeneficiaire.java (PHYSIQUE, MORALE)
├── repositories/
│   └── BeneficiaireRepository.java (Spring Data REST)
└── BeneficiaireServiceApplication.java (CommandLineRunner)
```

**Modèle de données :**
```java
@Entity
public class Beneficiaire {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String prenom;
    private String rib;              // 24 caractères (MA + 22 chiffres)
    @Enumerated(EnumType.STRING)
    private TypeBeneficiaire type;   // PHYSIQUE ou MORALE
}
```

**Endpoints REST (via Spring Data REST) :**
- GET `/api/beneficiaires` - Pagination automatique
- POST `/api/beneficiaires` - Validation automatique
- PUT `/api/beneficiaires/{id}` - Mise à jour
- DELETE `/api/beneficiaires/{id}` - Suppression
- GET `/api/beneficiaires/search/byType?type=PHYSIQUE`
- GET `/api/beneficiaires/search/byRib?rib=MA...`

### 5. Virement Service

**Rôle :** Gestion des virements et communication avec Beneficiaire Service

**Technologies :**
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Data REST
- Spring Cloud OpenFeign
- H2 Database (in-memory)
- Lombok
- SpringDoc OpenAPI
- Port : 8082

**Architecture interne :**
```
virement-service/
├── entities/
│   ├── Virement.java (JPA Entity)
│   └── BeneficiaireModel.java (DTO Feign)
├── enums/
│   └── TypeVirement.java (NORMAL, INSTANTANE)
├── repositories/
│   └── VirementRepository.java (Spring Data REST)
├── clients/
│   └── BeneficiaireClient.java (Feign Client)
├── controllers/
│   └── VirementController.java (Endpoints enrichis)
└── VirementServiceApplication.java
```

**Modèle de données :**
```java
@Entity
public class Virement {
    @Id @GeneratedValue
    private Long id;
    private Long idBeneficiaire;     // Foreign key logique
    private String ribSource;
    private Double montant;
    private String description;
    private Date dateVirement;
    @Enumerated(EnumType.STRING)
    private TypeVirement type;       // NORMAL ou INSTANTANE
    
    @Transient
    private BeneficiaireModel beneficiaire;  // Enrichi via Feign
}
```

**Communication inter-services avec Feign :**
```java
@FeignClient(name = "beneficiaire-service")
public interface BeneficiaireClient {
    @GetMapping("/api/beneficiaires/{id}")
    BeneficiaireModel getBeneficiaireById(@PathVariable Long id);
}
```

**Endpoints personnalisés :**
- GET `/api/virements/full/{id}` - Virement + Bénéficiaire
- GET `/api/virements/full` - Tous les virements enrichis

### 6. Chat-Bot Service

**Rôle :** Assistant bancaire intelligent avec IA

**Technologies :**
- Spring Boot 3.5.6
- Spring AI (Framework)
- OpenAI GPT-4o
- RAG (Retrieval Augmented Generation)
- Port : 8083

**Architecture interne :**
```
chat-bot-service/
├── models/
│   ├── ChatRequest.java
│   └── ChatResponse.java
├── services/
│   └── ChatBotService.java (Business Logic + RAG)
├── controllers/
│   └── ChatBotController.java (REST API)
├── config/
│   └── ChatConfig.java (ChatClient Bean)
└── ChatBotServiceApplication.java
```

**Système RAG implémenté :**
```java
// Contexte bancaire intégré
String bankContext = """
    - Virements normaux : 24-48h, 5 DH
    - Virements instantanés : immédiat, 10 DH
    - Limite quotidienne : 50,000 DH
    - Format RIB : MA + 22 chiffres
    - Support : 8h-22h
    """;

// Prompt système avec contexte
SystemPromptTemplate template = new SystemPromptTemplate(SYSTEM_PROMPT);
Message systemMessage = template.createMessage(Map.of("context", bankContext));

// Requête utilisateur
UserMessage userMessage = new UserMessage(question);

// Génération de réponse
String answer = chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
                          .call()
                          .content();
```

**Endpoints :**
- POST `/api/chat/ask` - Question avec contexte RAG
- GET `/api/chat/ask?question=...` - Question simple
- POST `/api/chat/simple` - Réponse sans RAG

## 🔄 Flux de Communication

### Scénario 1 : Récupérer un virement enrichi

```
1. Client → Gateway (8080)
   GET /api/virements/full/1

2. Gateway → Virement Service (8082)
   Resolve via Eureka: virement-service
   
3. Virement Service → Repository
   findById(1) → Virement entity
   
4. Virement Service → Feign Client
   getBeneficiaireById(virement.idBeneficiaire)
   
5. Feign → Gateway → Beneficiaire Service (8081)
   GET /api/beneficiaires/{id}
   
6. Beneficiaire Service → Response
   Return BeneficiaireModel
   
7. Virement Service → Enrichment
   virement.setBeneficiaire(beneficiaireModel)
   
8. Virement Service → Client
   Return Virement with Beneficiaire data
```

### Scénario 2 : Question au Chat-Bot

```
1. Client → Gateway (8080)
   POST /api/chat/ask
   Body: {"question": "Frais virement ?"}

2. Gateway → Chat-Bot Service (8083)
   Route to chat-bot-service
   
3. Chat-Bot Service → RAG System
   - Load bank context
   - Build system prompt with context
   - Create user message
   
4. Chat-Bot Service → OpenAI API
   POST https://api.openai.com/v1/chat/completions
   Model: gpt-4o
   Messages: [system, user]
   
5. OpenAI → Response
   Generate contextual answer
   
6. Chat-Bot Service → Client
   Return ChatResponse with answer + sources
```

## 🛡️ Sécurité et Résilience

### Load Balancing
- Ribbon (client-side load balancing)
- Round-robin par défaut
- Health-based routing

### Circuit Breaker (À implémenter)
```java
@CircuitBreaker(name = "beneficiaire-service", fallbackMethod = "fallback")
public BeneficiaireModel getBeneficiaire(Long id) {
    return beneficiaireClient.getBeneficiaireById(id);
}
```

### Retry Mechanism (À implémenter)
```yaml
resilience4j:
  retry:
    instances:
      beneficiaire-service:
        max-attempts: 3
        wait-duration: 1s
```

### Configuration Encryption (À implémenter)
```yaml
# Dans config-repo
spring:
  datasource:
    password: '{cipher}AQA123encrypted456...'
```

## 📈 Monitoring et Observabilité

### Spring Actuator
Endpoints exposés sur tous les services :
- `/actuator/health` - État de santé
- `/actuator/info` - Informations du service
- `/actuator/metrics` - Métriques
- `/actuator/env` - Variables d'environnement

### Logging
Configuration recommandée :
```yaml
logging:
  level:
    org.springframework.cloud: DEBUG
    com.netflix.eureka: INFO
    feign: DEBUG
```

## 🚀 Scalabilité

### Horizontal Scaling
```bash
# Démarrer plusieurs instances du même service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8084
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085
```

Eureka et Gateway géreront automatiquement le load balancing.

### Database Scaling
Pour la production, remplacer H2 par :
- PostgreSQL (relationnel)
- MongoDB (NoSQL)
- Utiliser un cluster de bases de données

## 📦 Déploiement

### Containerisation (Docker)
```dockerfile
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Orchestration (Kubernetes)
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: beneficiaire-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: beneficiaire-service
  template:
    metadata:
      labels:
        app: beneficiaire-service
    spec:
      containers:
      - name: beneficiaire-service
        image: bank/beneficiaire-service:latest
        ports:
        - containerPort: 8081
```

## 🔮 Évolutions Futures

1. **Sécurité**
   - OAuth2/JWT avec Spring Security
   - API Key management
   - Rate limiting avancé

2. **Messaging**
   - Apache Kafka pour événements asynchrones
   - RabbitMQ pour file d'attente

3. **Monitoring**
   - Prometheus + Grafana
   - ELK Stack (Elasticsearch, Logstash, Kibana)
   - Distributed tracing avec Zipkin

4. **Base de données**
   - Migration vers PostgreSQL
   - Redis pour cache
   - MongoDB pour données non structurées

5. **Frontend**
   - Angular/React pour interface web
   - Mobile app (iOS/Android)

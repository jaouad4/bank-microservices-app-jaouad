# Salah-Eddine JAOUAD

---

# 🏦 Bank Microservices - Application Bancaire

Une architecture micro-services complète pour la gestion des virements et bénéficiaires bancaires, construite avec Spring Boot 3.5.6 et Spring Cloud 2025.0.0.

## 📋 Table des Matières

- [Architecture](#architecture)
- [Technologies Utilisées](#technologies-utilisées)
- [Modules du Projet](#modules-du-projet)
- [Prérequis](#prérequis)
- [Installation et Démarrage](#installation-et-démarrage)
- [Configuration](#configuration)
- [APIs et Documentation](#apis-et-documentation)
- [Tests](#tests)
- [Fonctionnalités](#fonctionnalités)

## 🏗️ Architecture

Le projet suit une architecture micro-services moderne avec les composants suivants :

![Architecture](assets\architecture.svg)

## 🛠️ Technologies Utilisées

### Backend
- **Java 25**
- **Spring Boot 3.5.6**
- **Spring Cloud 2025.0.0**
  - Eureka (Service Discovery)
  - Config Server
  - Gateway
  - OpenFeign (Communication inter-services)
- **Spring Data JPA & REST**
- **Spring AI (OpenAI GPT-OSS-20B)**
- **H2 Database** (mode mémoire)
- **Lombok** (Réduction du boilerplate)
- **SpringDoc OpenAPI 3** (Documentation API)
- **dotenv-java** (Gestion sécurisée des secrets)

### Infrastructure
- **Maven** (Gestion des dépendances)
- **Git** (Configuration centralisée)
- **Spring Actuator** (Monitoring)

## 📦 Modules du Projet

### 1. Discovery Service (Port 8761)
Service de découverte utilisant Netflix Eureka Server.
- Enregistrement automatique de tous les microservices
- Dashboard de monitoring : http://localhost:8761

### 2. Config Service (Port 8888)
Service de configuration centralisée avec Spring Cloud Config.
- Repository Git local pour les configurations
- Support de profils multiples
- Rafraîchissement dynamique des configurations

### 3. Gateway Service (Port 8080)
API Gateway pour le routage des requêtes.
- Routes dynamiques basées sur Eureka
- Load balancing automatique
- Point d'entrée unique pour tous les services

**Routes configurées :**
- `/api/beneficiaires/**` → Beneficiaire Service
- `/api/virements/**` → Virement Service
- `/api/chat/**` → Chat-Bot Service

### 4. Beneficiaire Service (Port 8081)
Gestion des bénéficiaires bancaires.

**Entité Bénéficiaire :**
- `id` : Long (auto-généré)
- `nom` : String
- `prenom` : String
- `rib` : String (24 caractères)
- `type` : TypeBeneficiaire (PHYSIQUE, MORALE)

**Endpoints REST :**
- `GET /api/beneficiaires` - Liste tous les bénéficiaires
- `GET /api/beneficiaires/{id}` - Récupère un bénéficiaire
- `POST /api/beneficiaires` - Crée un bénéficiaire
- `PUT /api/beneficiaires/{id}` - Met à jour un bénéficiaire
- `DELETE /api/beneficiaires/{id}` - Supprime un bénéficiaire
- `GET /api/beneficiaires/search/byType?type=PHYSIQUE` - Recherche par type
- `GET /api/beneficiaires/search/byRib?rib=MA...` - Recherche par RIB

### 5. Virement Service (Port 8082)
Gestion des virements bancaires avec communication Feign.

**Entité Virement :**
- `id` : Long (auto-généré)
- `idBeneficiaire` : Long
- `ribSource` : String (24 caractères)
- `montant` : Double
- `description` : String
- `dateVirement` : Date
- `type` : TypeVirement (NORMAL, INSTANTANE)

**Endpoints REST :**
- `GET /api/virements` - Liste tous les virements
- `GET /api/virements/{id}` - Récupère un virement
- `POST /api/virements` - Crée un virement
- `GET /api/virements/full/{id}` - Virement avec données du bénéficiaire (Feign)
- `GET /api/virements/full` - Tous les virements enrichis
- `GET /api/virements/search/byBeneficiaire?idBeneficiaire=1`
- `GET /api/virements/search/byType?type=INSTANTANE`

### 6. Chat-Bot Service (Port 8083)
Assistant bancaire intelligent avec Spring AI et OpenAI GPT-OSS-20B.

**Configuration :**
- Modèle: `gpt-oss-20b`
- Temperature: `0.7`
- Stockage sécurisé de la clé API via fichier `.env`

**Fonctionnalités :**
- Système RAG (Retrieval Augmented Generation)
- Base de connaissances bancaire intégrée
- Support des questions en français
- Contexte bancaire spécialisé

**Endpoints REST :**
- `POST /api/chat/ask` - Poser une question (avec RAG)
- `GET /api/chat/ask?question=...` - Poser une question (GET)
- `POST /api/chat/simple` - Question simple sans RAG
- `GET /api/chat/health` - Vérifier le statut

## 🚀 Prérequis

- **JDK 25** installé
- **Maven 3.8+** installé
- **Git** installé
- **OpenAI API Key** pour le chat-bot (modèle: gpt-oss-20b)

## 📥 Installation et Démarrage

### 1. Cloner le projet

```powershell
cd "bank-microservices/"
```

### 2. Configurer l'API OpenAI (IMPORTANT)

#### Option A : Script automatique (Recommandé)
```powershell
.\setup-openai.ps1
```
Ce script vous guidera pour configurer votre clé API de manière sécurisée.

#### Option B : Configuration manuelle
1. Copiez le fichier `.env.example` vers `.env`:
   ```powershell
   Copy-Item .env.example .env
   ```

2. Ouvrez `.env` et remplacez `your-openai-api-key-here` par votre vraie clé API:
   ```
   OPENAI_API_KEY=sk-your-actual-api-key-here
   ```

**⚠️ Sécurité:** Le fichier `.env` est automatiquement ignoré par Git et ne sera jamais committé.

📖 **Pour plus de détails:** Consultez [OPENAI-SETUP.md](OPENAI-SETUP.md)

### 3. Compiler le projet parent

```powershell
mvn clean install -DskipTests
```

### 4. Démarrage automatique de tous les services

```powershell
.\start-all-services.ps1
```

Ce script va:
- ✅ Charger automatiquement les variables d'environnement depuis `.env`
- ✅ Compiler le projet
- ✅ Démarrer tous les services dans le bon ordre
- ✅ Ouvrir chaque service dans une nouvelle fenêtre PowerShell

### 5. Démarrage manuel (optionnel)

#### Étape 1 : Discovery Service (OBLIGATOIRE EN PREMIER)
```powershell
cd discovery-service
mvn spring-boot:run
```
Attendre que le service démarre complètement (Dashboard disponible à http://localhost:8761)

#### Étape 2 : Config Service
```powershell
cd ..\config-service
mvn spring-boot:run
```
Attendre l'enregistrement dans Eureka

#### Étape 3 : Gateway Service
```powershell
cd ..\gateway-service
mvn spring-boot:run
```

#### Étape 4 : Services Métiers (en parallèle dans des terminaux séparés)

**Beneficiaire Service :**
```powershell
cd ..\beneficiaire-service
mvn spring-boot:run
```

**Virement Service :**
```powershell
cd ..\virement-service
mvn spring-boot:run
```

**Chat-Bot Service :**
```powershell
cd ..\chat-bot-service
mvn spring-boot:run
```
**Note:** La clé API OpenAI sera automatiquement chargée depuis le fichier `.env`

### 6. Vérifier le démarrage

Tous les services doivent apparaître dans le dashboard Eureka :
👉 http://localhost:8761

## ⚙️ Configuration

### Repository Git de Configuration

Les configurations sont centralisées dans : `config-repo/`

**Fichiers de configuration :**
- `beneficiaire-service.yml`
- `virement-service.yml`
- `gateway-service.yml`
- `chat-bot-service.yml`

Pour modifier une configuration :
```powershell
git add .
git commit -m "Update configuration"
# Redémarrer le service concerné
```

### Clé API OpenAI

Pour utiliser le Chat-Bot Service, configurez votre clé API :

**Option 1 : Variable d'environnement (recommandé)**
```powershell
$env:OPENAI_API_KEY="sk-votre-cle-api"
```

**Option 2 : Fichier application.yml**
```yaml
spring:
  ai:
    openai:
      api-key: sk-votre-cle-api
```

**Option 3 : Config Server**
Modifier `C:\Users\JD\config-repo\chat-bot-service.yml`

## 📚 APIs et Documentation

### Swagger UI (Documentation Interactive)

Chaque service expose sa documentation Swagger :

- **Beneficiaire Service :** http://localhost:8081/swagger-ui.html
- **Virement Service :** http://localhost:8082/swagger-ui.html

### Via Gateway (Point d'entrée unique)

- **Beneficiaires :** http://localhost:8080/api/beneficiaires
- **Virements :** http://localhost:8080/api/virements
- **Chat-Bot :** http://localhost:8080/api/chat/ask

### Consoles H2 (Base de données)

- **Beneficiaire :** http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:beneficiairedb`
  - Username: `sa`
  - Password: *(vide)*

- **Virement :** http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:virementdb`
  - Username: `sa`
  - Password: *(vide)*

### Actuator (Monitoring)

- **Discovery :** http://localhost:8761/actuator/health
- **Config :** http://localhost:8888/actuator/health
- **Gateway :** http://localhost:8080/actuator/health
- **Beneficiaire :** http://localhost:8081/actuator/health
- **Virement :** http://localhost:8082/actuator/health
- **Chat-Bot :** http://localhost:8083/actuator/health

## 🧪 Tests

### Test 1 : Vérifier tous les services dans Eureka
```
Ouvrir : http://localhost:8761
Résultat attendu : 5 services enregistrés (CONFIG-SERVICE, GATEWAY-SERVICE, BENEFICIAIRE-SERVICE, VIREMENT-SERVICE, CHAT-BOT-SERVICE)
```

### Test 2 : Récupérer les bénéficiaires via Gateway
```powershell
curl http://localhost:8080/api/beneficiaires
```

### Test 3 : Créer un bénéficiaire
```powershell
curl -X POST http://localhost:8080/api/beneficiaires `
  -H "Content-Type: application/json" `
  -d '{
    "nom": "AMRANI",
    "prenom": "Karim",
    "rib": "MA007890123456789012345678",
    "type": "PHYSIQUE"
  }'
```

### Test 4 : Récupérer un virement avec bénéficiaire (Feign)
```powershell
curl http://localhost:8080/api/virements/full/1
```

### Test 5 : Tester le Chat-Bot
```powershell
curl -X POST http://localhost:8080/api/chat/ask `
  -H "Content-Type: application/json" `
  -d '{
    "question": "Quels sont les frais pour un virement instantané ?"
  }'
```

### Test 6 : Vérifier la communication Feign
```powershell
# Récupérer tous les virements enrichis
curl http://localhost:8082/api/virements/full
```

## 🎯 Fonctionnalités

### ✅ Fonctionnalités Implémentées

- [x] Service Discovery avec Eureka
- [x] Configuration centralisée avec Config Server
- [x] API Gateway avec routage dynamique
- [x] Gestion complète des bénéficiaires (CRUD)
- [x] Gestion complète des virements (CRUD)
- [x] Communication inter-services avec OpenFeign
- [x] Chat-Bot intelligent avec Spring AI (GPT-4o)
- [x] Système RAG pour le contexte bancaire
- [x] Documentation API avec Swagger/OpenAPI
- [x] Base de données H2 en mémoire
- [x] Monitoring avec Spring Actuator
- [x] Données de test au démarrage
- [x] Support des types de bénéficiaires (PHYSIQUE/MORALE)
- [x] Support des types de virements (NORMAL/INSTANTANE)

### 🔄 Flux de Communication

1. **Client** → **Gateway (8080)** → **Service Métier (8081/8082/8083)**
2. **Virement Service** → **Feign Client** → **Beneficiaire Service**
3. **Tous les services** → **Eureka Discovery (8761)**
4. **Tous les services** → **Config Server (8888)** → **Git Repository**

## 🗂️ Structure du Projet

```
bank-microservices/
├── pom.xml (Parent POM)
├── discovery-service/
│   ├── src/main/java/.../DiscoveryServiceApplication.java
│   └── src/main/resources/application.yml
├── config-service/
│   ├── src/main/java/.../ConfigServiceApplication.java
│   └── src/main/resources/application.yml
├── gateway-service/
│   ├── src/main/java/.../GatewayServiceApplication.java
│   └── src/main/resources/application.yml
├── beneficiaire-service/
│   ├── src/main/java/
│   │   ├── entities/Beneficiaire.java
│   │   ├── enums/TypeBeneficiaire.java
│   │   ├── repositories/BeneficiaireRepository.java
│   │   └── BeneficiaireServiceApplication.java
│   └── src/main/resources/application.yml
├── virement-service/
│   ├── src/main/java/
│   │   ├── entities/Virement.java, BeneficiaireModel.java
│   │   ├── enums/TypeVirement.java
│   │   ├── repositories/VirementRepository.java
│   │   ├── clients/BeneficiaireClient.java (Feign)
│   │   ├── controllers/VirementController.java
│   │   └── VirementServiceApplication.java
│   └── src/main/resources/application.yml
├── chat-bot-service/
│   ├── src/main/java/
│   │   ├── models/ChatRequest.java, ChatResponse.java
│   │   ├── services/ChatBotService.java
│   │   ├── controllers/ChatBotController.java
│   │   ├── config/ChatConfig.java
│   │   └── ChatBotServiceApplication.java
│   └── src/main/resources/application.yml
└── README.md
```

## 📄 Licence

Ce projet est développé dans le cadre d'un TP académique à l'ENSET.

---

## Salah-Eddine JAOUAD
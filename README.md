# 📌 Backend API  

![Java](https://img.shields.io/badge/Java-21-blue?style=flat&logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat&logo=spring)  
![JWT](https://img.shields.io/badge/Auth-JWT-orange?style=flat&logo=jsonwebtokens)  
![HTTPS](https://img.shields.io/badge/Security-HTTPS-blue?style=flat&logo=lock)  
![API](https://img.shields.io/badge/API-RESTful-lightgrey?style=flat)  

Ce projet est une API REST développée avec **Spring Boot** et **Java 21**. Elle utilise **JWT** pour l'authentification et communique en **HTTPS**. Les données sont échangées au format **JSON**.

---

## 🏛️ Architecture  

L'application suit une **architecture en 3 couches (Three-Tier Architecture)** basée sur le modèle **MVC** :  

- **Controller** : Gère les requêtes HTTP et les envoie au service approprié.  
- **Service** : Contient la logique métier.  
- **Repository (DAO)** : Interagit avec la base de données via JPA/Hibernate.  

### 📂 Structure du projet  

```plaintext
src
 ├── main
 │   ├── java/com.hepl.backendapi
 │   │   ├── controller     # Gère les requêtes HTTP
 │   │   ├── dto            # Transfert de données (Data Transfer Objects)
 │   │   ├── entity         # Modèles de données (JPA Entities)
 │   │   ├── mappers        # Transformation DTO <-> Entity
 │   │   ├── repository     # Accès aux données (JPA Repositories)
 │   │   ├── service        # Logique métier
 │   │   ├── utils          # Utilitaires divers
 │   │   ├── BackendApiApplication.java # Point d'entrée de l'application
 │   ├── resources
 │   │   ├── application.properties # Configuration de l'application
 │   │   ├── application.yaml        # Alternative en YAML
 ├── test          # Contient les tests unitaires et d’intégration
 ├── target        # Dossier de build
 ├── pom.xml       # Fichier des dépendances
```
---

## 🚀 Pourquoi Java et Spring Boot ?  

### **Java 21**  
✅ **Portabilité** grâce à la JVM  
✅ **Programmation orientée objet**  
✅ **Outils de testing robustes**  
✅ **Écosystème et bibliothèques riches**  

### **Spring Boot**  
✅ **Serveur Web intégré** (Tomcat par défaut)  
✅ **Création facile de REST APIs** (`@RestController`, `@GetMapping`, `@PostMapping`, etc.)  
✅ **Gestion simplifiée des dépendances** (Maven / Gradle)  
✅ **Framework populaire en entreprise** avec **grande communauté** et **documentation abondante**  

---'

## 📦 Dépendances utilisées

| 📦 Dépendance                         | 🔍 Rôle                                                         |
|---------------------------------------|-----------------------------------------------------------------|
| `spring-boot-starter-web`             | Création d’API REST                                             |
| `spring-boot-starter-data-jpa`        | Gestion de la base de données avec JPA/Hibernate                |
| `mysql-connector-j`                   | Driver JDBC pour MySQL                                          |
| `lombok`                              | Réduction du boilerplate Java (Getters, Setters, etc.)          |
| `mapstruct`                           | Mapping d'objets Java                                           |
| `springdoc-openapi-starter-webmvc-ui` | Génération automatique de la documentation OpenAPI (Swagger UI) |
| `spring-boot-starter-validation`      | Validation des données (annotations `@Valid`, etc.)             |
| `spring-boot-starter-security`        | Sécurisation des endpoints avec Spring Security                 |
| `jjwt-api`                            | Gestion des tokens JWT (authentification)                       |
| `spring-cloud-azure-starter-keyvault` | Intégration avec Azure Key Vault pour sécuriser les secrets     |
| `spring-boot-devtools`                | Outils de développement (reload à chaud)                        |



## 🔐 Sécurité  

🔹 **Authentification JWT** pour protéger l'accès aux endpoints.  
🔹 **Communication sécurisée** avec HTTPS (TLS).  

[Client (mobile/web)]  
│  
▼  
[Serveur d'authentification] ──→ [BD AUTH (NoSQL)]  
│  
▼ (JWT signé avec clé privée)  
[Client (avec JWT)]  
│  
▼ (Cookie : JWT, TYPE : GET/POST/DELETE)  
[API (Spring Boot)] ──→ [Serveur de certificats] (Récupère ou vérifie validité du certificat du serveur d'authentification)  
│  
▼  
(Vérification signature JWT avec la clé publique du serveur d'authentification)  
│  
▼  
[BD (MySQL/MongoDB)]



## Explication du Flow

1. **Authentification du client** :  
   Le client (mobile/web) s'authentifie auprès du **serveur d'authentification**. Ce serveur vérifie les identifiants et interagit avec la **base de données NoSQL** pour valider l'utilisateur.

2. **Génération du JWT** :  
   Une fois l'utilisateur validé, le **serveur d'authentification** génère un **JWT signé** avec sa **clé privée** et le renvoie au client.

3. **Envoi du JWT au client** :  
   Le **client** reçoit le JWT et l'utilise pour s'authentifier dans les futures requêtes API. Le JWT est souvent stocké dans un **cookie** et envoyé dans les requêtes de type **GET/POST/DELETE**.

4. **Vérification du certificat du serveur** :  
   L'**API** (généralement une application Spring Boot) contacte le **serveur de certificats** pour récupérer ou vérifier la validité du certificat SSL/TLS du **serveur d'authentification**.

5. **Vérification de la signature JWT** :  
   Une fois la validité du certificat vérifiée, l'API utilise la **clé publique du serveur d'authentification** pour valider la signature du **JWT** et s'assurer qu'il n'a pas été falsifié.

6. **Accès à la base de données** :  
   Après validation du JWT, l'API peut interagir avec la **base de données** (MySQL/MongoDB) pour récupérer ou stocker les informations de l'utilisateur.

## Sécurité

- **Clé privée/clé publique** : La sécurité de l'authentification repose sur la clé privée du serveur d'authentification pour signer les JWT et la clé publique pour les vérifier.
- **Cookies sécurisés** : Les JWT sont envoyés dans des cookies sécurisés avec des attributs comme `HttpOnly` et `Secure` pour éviter les attaques XSS.

---

## 🌍 API Endpoint  

🚀 L'API est déployée sur **Microsoft Azure** et est accessible à l'adresse suivante :  
🔗 **[https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/](https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/)**  

📜 **Swagger UI (Documentation OpenAPI) disponible ici :**  
🔗 **[https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/swagger-ui.html](https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/swagger-ui.html)**  

---

## 🚀 Installation et exécution  

### **Pré-requis**  
- **Java 21** installé  
- **Maven** installé  
- Base de données configurée dans `application.properties` ou `application.yaml`  

## 🔑 Sécurisation des mots de passe

Pour éviter l'exposition des mots de passe dans le code source, les secrets sont gérés et récupérés de manière sécurisée via Azure Key Vault.
Les secrets nécessaires sont stockés dans Azure et injectés au moment de l'exécution dans l'application via des variables d'environnement ou la configuration Azure Spring Boot.

Voici comment cela fonctionne :

- Azure Key Vault stocke les secrets comme le mot de passe de la base de données.
- Lors de l'exécution de l'application, Spring Boot récupère ces secrets et les injecte dans le contexte de l'application.


### **Démarrer l'application**  
```bash
# Cloner le dépôt
git clone https://github.com/votre-utilisateur/votre-repository.git
cd votre-repository

# Configurez votre environnement en définissant les variables suivantes via votre terminal ou dans un fichier .env :
- mysqlurlS
- mysqlusername
- mysqlpassword
- mysqlurlT

# Construire le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```


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

---

## 📦 Dépendances utilisées  

| 📦 Dépendance | 🔍 Rôle |
|--------------|--------|
| `spring-boot-starter-web` | Création d’API REST |
| `spring-boot-starter-data-jpa` | Gestion de la base de données avec JPA/Hibernate |
| `spring-boot-starter-test` | Framework de tests (JUnit, Mockito, etc.) |
| `springdoc-openapi-starter-webmvc-ui` | Génération automatique de la documentation OpenAPI (Swagger UI) |
| `jackson-databind` | Sérialisation/Désérialisation JSON |
| `lombok` | Réduction du boilerplate Java (Getters, Setters, etc.) |

---

## 🔐 Sécurité  

🔹 **Authentification JWT** pour protéger l'accès aux endpoints.  
🔹 **Communication sécurisée** avec HTTPS (TLS).  

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

### **Démarrer l'application**  
```bash
# Cloner le dépôt
git clone https://github.com/votre-utilisateur/votre-repository.git
cd votre-repository

# Construire le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```


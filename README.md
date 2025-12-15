
# TODO App - Application de Gestion de Tâches

## 📝 Description du Projet

Cette application web est un système simple de gestion de tâches ("Todo List") qui implémente les concepts fondamentaux de la plateforme Jakarta EE (anciennement Java EE), incluant la persistance (JPA/EJB), la couche de présentation (JSF) et la création d'une API Web (JAX-RS).

Elle permet :

1.  **Gestion des Tâches** : Créer, visualiser, modifier, supprimer et basculer le statut (`Terminé`/`À faire`) des tâches.
2.  **Gestion des Utilisateurs** : Créer et supprimer des utilisateurs, et associer des tâches à ces utilisateurs.
3.  **API RESTful** : Accéder aux données des tâches via des points de terminaison REST au format JSON.

## 🛠️ Technologies Utilisées

| Catégorie | Technologie | Composant / Version | Fichier Clé |
| :--- | :--- | :--- | :--- |
| **API de Plateforme** | Jakarta EE | Version 11.0.0 (via `jakarta.jakartaee-api`) | `pom.xml` |
| **Frontend** | Jakarta Server Faces (JSF) | Vue déclarative | `index.xhtml` |
| **Logique Métier** | EJB (Enterprise JavaBeans) | Services Stateless | `TaskService.java`, `UserService.java` |
| **Persistance** | JPA (Jakarta Persistence API) | Mapping Objet-Relationnel | `Task.java`, `User.java`, `persistence.xml` |
| **Injection/Contextes** | CDI (Contexts and Dependency Injection) | `@Named`, `@Inject`, Scopes | `TodoController.java` |
| **API Web** | JAX-RS (RESTful Web Services) | Points de terminaison REST | `TaskResource.java`, `TodoApplication.java` |
| **Build Tool** | Maven | Gestion des dépendances | `pom.xml` |

## 🚀 Démarrage de l'Application

L'application est packagée en un fichier WAR (Web ARchive) et nécessite un serveur d'applications Jakarta EE compatible (comme GlassFish, WildFly, Open Liberty, etc.).

1.  **Build (Compilation et Packaging)** :
    ```bash
    ./mvnw clean install
    ```
2.  **Déploiement** : Déployez le fichier `.war` généré (ex: `target/todoApp-1.0-SNAPSHOT.war`) sur votre serveur d'applications.
3.  **Accès Web (JSF)** : L'application JSF sera accessible à l'URL de base, qui pointe sur `index.xhtml`.
4.  **Accès API (REST)** : L'API REST est disponible sous le chemin de base `/api`. Par exemple, pour lister toutes les tâches : `http://[hostname]:[port]/[context-root]/api/tasks`.

## 🔑 Concepts Clés pour l'Examen

Voici une décomposition des concepts importants de Jakarta EE illustrés dans ce projet.

### 1\. La Couche de Persistance (JPA & Base de Données)

| Fichier(s) | Concept / Rôle | Points Clés pour l'Examen |
| :--- | :--- | :--- |
| **`Task.java` & `User.java`** | **Entités JPA** | **Mapping :** Les classes sont annotées `@Entity` et mappées à une table (`@Table(name="tasks")`). **Clé Primaire :** Utilisation de `@Id` et `@GeneratedValue(strategy = GenerationType.AUTO)`. **Relation :** `Task` a une `@ManyToOne` vers `User` (`@JoinColumn(name = "user_id")`) ; `User` a une `@OneToMany` bidirectionnelle vers `Task` (`mappedBy = "user"`) avec des règles de cascade (`CascadeType.ALL`). **Constructeur Vide :** Obligatoire pour JPA. |
| **`persistence.xml`** | **Unité de Persistance** | **Nom :** Définit l'unité de persistance `todoPU`. **Source de Données :** Se connecte via la source de données JTA `jdbc/todoDS`. **DDL Generation :** Utilise `eclipselink.ddl-generation` réglé à `drop-and-create-tables` pour recréer le schéma à chaque déploiement (utile en dev, **non recommandé en prod**). |
| **`TaskService.java` & `UserService.java`** | **Services de Persistance EJB** | **Contextes :** Le service est un EJB `@Stateless` (léger, poolable, transac-tionnel). **Injection :** `@PersistenceContext(unitName = "todoPU")` injecte l'`EntityManager` pour les opérations sur la base. **Opérations :** Les méthodes CRUD utilisent `em.persist()`, `em.find()`, `em.merge()`, et `em.remove()`. |
| **`DataInitializer.java`** | **Initialisation des Données** | **Cycle de Vie :** Annoté `@Singleton` et `@Startup` pour s'assurer que cette classe est instanciée et que sa méthode `@PostConstruct` est exécutée **une seule fois au démarrage de l'application**. **Ordre de Création :** Important de créer le parent (`User`) en premier pour obtenir son ID avant de créer les enfants (`Task`) qui référenceront cet ID (clé étrangère). |

### 2\. La Couche de Présentation (JSF & CDI)

| Fichier(s) | Concept / Rôle | Points Clés pour l'Examen |
| :--- | :--- | :--- |
| **`TodoController.java` & `UserController.java`** | **Contrôleurs (Beans Gérés CDI)** | **CDI :** Annotés `@Named` pour être appelés dans le XHTML (ex: `todoController`). **Injection :** Utilisation de `@Inject` pour obtenir les instances des services EJB (`TaskService`, `UserService`). **Portée (Scope) :** Utilisation de `@ViewScoped`. Le bean vit tant que l'utilisateur interagit avec la même vue JSF (ex: pour l'édition en ligne sans perdre l'état `taskToEdit`). **Navigation :** Les méthodes d'action (`addTask`, `deleteTask`, `saveTask`, etc.) retournent `null` pour rester sur la vue actuelle (`index.xhtml`). |
| **`index.xhtml`** | **Vue JSF** | **Binding :** Utilisation de l'Expression Language (EL) `#{...}` pour lier les composants de la vue aux propriétés du bean (`<h:inputText value="#{todoController.newTask.title}" />`). **Tables/Itération :** Utilisation de `<h:dataTable>` avec l'attribut `var` pour itérer sur une liste (`value="#{todoController.tasks}" var="t"`). **Rendu Conditionnel :** Utilisation de `<h:panelGroup rendered="..."` pour alterner entre l'affichage et l'édition d'une tâche. |
| **`web.xml`** | **Configuration JSF** | **FacesServlet :** Définit le `FacesServlet` qui gère le cycle de vie JSF et le mappe aux extensions `.xhtml`. |

### 3\. L'API RESTful (JAX-RS)

| Fichier(s) | Concept / Rôle | Points Clés pour l'Examen |
| :--- | :--- | :--- |
| **`TodoApplication.java`** | **Point de Départ JAX-RS** | **Base URL :** `@ApplicationPath("/api")` définit le préfixe de base de l'API. |
| **`TaskResource.java`** | **Ressource REST** | **Chemin :** `@Path("/tasks")` définit le chemin d'accès à la ressource. **Opérations HTTP :** Utilisation des annotations `@GET`, `@POST`, `@PUT`, `@DELETE` pour mapper les méthodes aux verbes HTTP. **Média Types :** `@Produces(MediaType.APPLICATION_JSON)` et `@Consumes(MediaType.APPLICATION_JSON)` définissent les formats d'entrée et de sortie. **Paramètres :** Utilisation de `@PathParam("id")` pour capturer l'ID dans l'URL (`/tasks/{id}`). |

-----

Ce document vous donne un aperçu précis de l'architecture de votre application et des annotations/technologies à réviser. Bonne chance pour votre examen \!

Absolument. Les tests unitaires sont une partie essentielle du développement logiciel et il est très important de comprendre comment ils sont configurés et utilisés dans le contexte de votre application.

Voici la section à ajouter au `README.md` concernant les tests JUnit.

-----

### 4\. Tests Unitaires (JUnit 5)

| Fichier(s) | Concept / Rôle | Points Clés pour l'Examen |
| :--- | :--- | :--- |
| **`pom.xml`** | **Configuration de la dépendance** | **Dépendance :** La dépendance `org.junit.jupiter:junit-jupiter` est ajoutée avec le scope `<scope>test</scope>`. Ceci indique à Maven que cette librairie n'est nécessaire que pour compiler et exécuter les tests, et **ne doit pas être incluse dans le fichier WAR** final déployé sur le serveur d'applications. **Exécution :** Le `maven-surefire-plugin` est utilisé pour gérer l'exécution des tests JUnit 5. |
| **`TaskTest.java`** | **Tests de l'Entité JPA `Task`** | **Type de Test :** Il s'agit d'un **test unitaire pur** (POJO - Plain Old Java Object). Il teste la classe `Task` de manière isolée, sans nécessiter de base de données ni de serveur d'applications. **Constructeur JPA :** La méthode `defaultConstructor_shouldInitializeWithNullIdAndDoneFalse()` valide l'état initial des propriétés après l'appel du constructeur vide. Cela vérifie notamment la bonne compréhension du constructeur vide qui est **obligatoire pour JPA**. **Couverture :** Les tests couvrent les fonctionnalités de base de l'entité : les constructeurs, ainsi que le bon fonctionnement des *accessors* (getters) et *mutators* (setters).

#### Comment lancer les tests ?

Vous pouvez lancer les tests à l'aide de Maven avec la commande :

```bash
./mvnw test
```

Cette commande va compiler les sources principales, compiler les sources de test, et exécuter tous les tests JUnit présents dans le répertoire `src/test/java`.
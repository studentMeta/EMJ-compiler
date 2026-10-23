# Résumé du projet et Guide d'utilisation du Compilateur En-Mode-J (EMJ)

Ce document présente un résumé du projet de compilateur **En-Mode-J (EMJ)**, un langage de programmation basé sur des émojis pour contrôler un robot Cutebot (Micro:bit) sur une carte 2D. Il contient également un tutoriel complet sur l'utilisation du compilateur et un exemple de code source complet.

---

## 1. Résumé du projet

Le projet consiste en un **compilateur complet** pour le langage **En-Mode-J (EMJ)**. Ce langage de programmation atypique utilise des émojis à la place des mots-clés textuels pour définir des types, des structures de contrôle, des opérations de déplacement du robot, etc.

Le but de ce langage est de piloter un robot virtuel ou réel (le **Cutebot**) sur une grille contenant des obstacles et un ou plusieurs voleurs à arrêter.

Le compilateur est structuré en plusieurs phases :
1. **Syntaxe & Grammaire (ANTLR4)** : Fichiers `EMJLexer.g4` et `EMJParser.g4` définissant le lexique et la structure grammaticale des programmes (`.moj`) et des fichiers de cartes (`.map`).
2. **Sémantique & Table des Symboles** : Validation des types, de la portée des variables (scope), de la déclaration unique des identifiants et des contraintes physiques de la carte.
3. **Génération de Code MicroPython** : Traduction des émojis en code Python valide pour la plateforme Micro:bit en intégrant une bibliothèque de navigation Cutebot (`cutebot_custom_librairy.py`).

---

## 2. Fonctionnement théorique de la Table des Symboles (Symbol Table)

La **Table des Symboles** (représentée par les classes `SymbolTable` et `Symbol`) est un composant central de l'analyse sémantique. Elle sert d'annuaire dynamique pour stocker et valider tous les identifiants (variables, paramètres, fonctions) déclarés dans le programme en cours de compilation.

### A. Représentation des concepts (`Symbol`)
Un symbole individuel (`Symbol.java`) encapsule :
* **Kind (Catégorie)** : `VARIABLE`, `PARAMETER` (paramètre de fonction) ou `FUNCTION` (déclaration de fonction).
* **Type** : Le type EMJ associé (`INT`, `CHAR`, `STRING`, `BOOL`, `TUPLE_INT`, `TUPLE_CHAR`, etc., ou `VOID`).
* **Name (Nom/Emoji)** : L'identifiant textuel (ex: `[déplacements]`).
* **Scope (Portée)** : Le contexte dans lequel il est défini (généralement le nom de la fonction parent).
* **Initialized (Initialisation)** : Booléen servant à s'assurer qu'une variable a bien été affectée avant d'être lue. Les fonctions et paramètres sont quant à eux considérés comme toujours initialisés.
* **Champs de fonction** : Si le symbole représente une fonction, il stocke également le type et le nom de ses paramètres ainsi que leur nombre total (`paramCount`).

### B. Structure de la table et gestion des contextes (`SymbolTable`)
La table des symboles (`SymbolTable.java`) est modélisée par une **arborescence de contextes (Scopes)** couplée à une **pile (`LinkedList<Scope>`)** pour suivre la trace de l'analyse en cours :
1. **La pile de contextes (`scopeStack`)** :
   * Au début de la compilation, la pile ne contient que le contexte racine nommé `global`.
   * **Entrée dans un contexte (`enterScope(name)`)** : Lorsqu'un visiteur entre dans une fonction ou un bloc d'instructions, le compilateur navigue vers le contexte enfant ou en instancie un nouveau en le reliant à son contexte parent, puis l'empile sur `scopeStack`.
   * **Sortie d'un contexte (`exitScope()`)** : En sortant de la fonction/bloc, la pile est dépilée, et le contexte actuel redevient le parent direct.
2. **Arborescence hiérarchique** :
   Chaque `Scope` est un nœud qui détient :
   * Une référence vers son `parent`.
   * Une table de ses symboles déclarés localement.
   * Une table de ses contextes enfants (`children`).

### C. Règles de recherche (Lookup) et portée lexicale
Portée lexicale : lors de l'analyse sémantique par le visiteur `EMJVisitor`, la recherche de symboles s'effectue à deux niveaux :
* **Recherche locale** (`lookupVariableOrParamLocal`, `existsLocally`) : Vérifie uniquement dans le contexte actuel (`currentScope`). Ceci est crucial lors de la déclaration d'une variable pour interdire les doubles déclarations dans la même portée.
* **Recherche lexicale ascendante** (`lookupVariable`) : Remonte l'arborescence des contextes du plus imbriqué (`currentScope`) vers le plus externe (`parent`, grand-parent, etc.), jusqu'à s'arrêter juste avant le contexte `global`. Cela permet à une fonction imbriquée d'accéder aux variables de son parent lexical direct.
* **Recherche globale de fonction** (`lookupFunction`) : Interroge exclusivement le contexte racine `global`, car toutes les fonctions en EMJ sont déclarées au premier niveau (espace de noms global).

### D. Rôle dans les vérifications sémantiques
Durant la phase de visite de l'AST (Abstract Syntax Tree), la table des symboles permet de valider :
1. **L'existence** : Une variable utilisée dans une expression doit exister dans le scope en cours ou un scope parent.
2. **L'initialisation** : La variable lue doit être marquée comme initialisée.
3. **Le typage** : Le type des valeurs affectées doit correspondre au type stocké pour cette variable.
4. **La signature de fonction** : Lors d'un appel `[f](...)`, elle permet d'extraire la signature de `[f]` enregistrée globalement afin de valider le nombre et le type des arguments passés.

---

## 3. Guide d'utilisation du compilateur

Pour utiliser le compilateur, suivez les instructions ci-dessous étape par étape.

### Étape 1 : Prérequis
Assurez-vous d'avoir installé sur votre machine :
* **Java Development Kit (JDK) 8** ou supérieur.
* **Apache Maven** (pour compiler le compilateur).

### Étape 2 : Compiler le compilateur
Ouvrez un terminal dans le répertoire racine du projet (où se trouve le fichier `pom.xml`) et exécutez la commande suivante :
```bash
mvn clean package
```
Cette commande va :
1. Télécharger les dépendances requises.
2. Générer les classes d'analyse ANTLR4 à partir de la grammaire.
3. Compiler le code source Java.
4. Créer une archive exécutable contenant toutes les dépendances dans le dossier `target/` nommée :  
   `target/en-mode-j-1.0.0-jar-with-dependencies.jar`

### Étape 3 : Lancer le compilateur
Pour compiler un programme écrit en EMJ (`.moj`) en code MicroPython (`.py`), utilisez la commande suivante :
```bash
java -jar target/en-mode-j-1.0.0-jar-with-dependencies.jar -i <chemin_fichier_source.moj> -o <chemin_fichier_sortie.py>
```

**Arguments :**
* `-i` : Fichier source d'entrée (`.moj`).
* `-o` : Fichier de sortie généré en Python (`.py`).

**Résultat :**
* Si le code est correct syntaxiquement et sémantiquement, le terminal affichera `OK` (sur stderr) et créera le fichier Python.
* Si le code comporte des erreurs, le compilateur affichera les erreurs sémantiques/syntaxiques détaillées et retournera `KO`.

### Étape 4 : Exécuter sur le robot Cutebot / Micro:bit
Une fois le fichier Python généré :
1. Rendez-vous sur l'éditeur officiel Micro:bit / MakeCode : [https://python.microbit.org/](https://python.microbit.org/) (ou [https://makecode.microbit.org/](https://makecode.microbit.org/)).
2. Copiez le contenu du fichier Python généré.
3. Collez-le dans l'éditeur.
4. Téléchargez le programme sur la carte Micro:bit de votre Cutebot ou lancez le simulateur.

---

## 4. Exemple de code complet

Voici un exemple complet de carte (`.map`) et de programme EMJ (`.moj`).

### A. La Carte : `exemple_carte.map`
Ce fichier définit une grille de 5 lignes et 5 colonnes, avec une position de départ pour la police (le Cutebot) orientée vers le sud (⬇️).

```text
🗺 with 5, 5, ⬇️;
🚔 🌋 🏘️ 🚧 🚜
🛣️ 🏘️ 🚜 🏘️ 🌊
🛣️ 🏘️ 🚜 🏘️ 🌊
🛣️ 🏘️ 🚧 🏘️ 🌊
🛣️ 🛣️ 🛣️ 🛣️ 🦹
```

**Légende des éléments de la carte :**
* `🚔` : La Police (départ du Cutebot)
* `🛣️` : Route praticable
* `🦹` : Le voleur à arrêter
* `🌋`, `🏘️`, `🚧`, `🚜`, `🌊` : Obstacles infranchissables

---

### B. Le Programme : `exemple_programme.moj`
Ce programme importe la carte ci-dessus, initialise des variables, utilise une boucle `for` et une condition `if-else`, et appelle des actions physiques sur le robot (déplacement, sirène, phares, arrestation).

```text
📢 Importation de la carte associée au programme
📦 "exemple_carte.map"

📢 Fonction principale 🏠
🌀 🏠() {
    📢 Déclaration d'un entier compteur de déplacements
    🔢 [déplacements] = 0;

    📢 Déclaration d'un booléen pour l'état de la sirène
    🔟 [alarme] = ❌;

    📢 Déclaration d'un tuple pour les coordonnées de destination
    👥(🔢) [destination] = (4, 4);

    📢 Allumer les phares (🚨) et la sirène (📻) du Cutebot
    🚨();
    📻();
    [alarme] = ✅;

    📢 Faire avancer le robot de 4 cases vers le bas
    ⬇️(4);
    [déplacements] = [déplacements] + 4;

    📢 Tourner et avancer à droite de 4 cases pour atteindre le voleur
    ➡️(4);
    [déplacements] = [déplacements] + 4;

    📢 Vérification si le robot a atteint sa destination
    🤔 ([déplacements] == 8) {
        📢 Appel de la fonction personnalisée d'arrestation
        [effectuer_arrestation]();
    } 🤨 {
        📢 Ne rien faire (instruction vide)
        👇;
    }

    📢 Retour vide pour la fonction main
    ↩️ 🌀;
}

📢 Fonction personnalisée pour arrêter le voleur
🌀 [effectuer_arrestation]() {
    📢 Lancer la séquence d'arrestation (✋)
    ✋();

    📢 Éteindre la sirène et les phares
    📻();
    🚨();

    ↩️ 🌀;
}
```

---

## 5. Tableau de référence des symboles EMJ

| Symbole Émoji | Rôle | Équivalent standard |
|---|---|---|
| `📦 "nom.map"` | Import de la carte | `#include` / `import` |
| `🏠` | Fonction principale | `main` |
| `🌀` | Type vide ou retour de void | `void` |
| `🔢` | Type entier | `int` |
| `🔣` | Type caractère | `char` |
| `🔡` | Type chaîne de caractères | `string` |
| `🔟` | Type booléen | `bool` |
| `👥(type)` | Type tuple | `tuple<type, type>` |
| `✅` / `❌` | Valeurs booléennes | `true` / `false` |
| `🤔` | Structure conditionnelle | `if` |
| `🤨` | Branche alternative | `else` |
| `👇` | Instruction vide | `pass` / `;` |
| `🔁 (N)` | Boucle répétitive | `for i in range(N)` |
| `♾️ (condition)` | Boucle tant que | `while (condition)` |
| `↩️` | Retour de fonction | `return` |
| `📢` | Commentaire ligne | `//` |
| `🔊 ... 🔈` | Commentaire bloc | `/* ... */` |
| `✋()` | Action : Arrêter un voleur | `arrest()` |
| `📻()` | Action : Activer/Désactiver sirène | `toggle_siren()` |
| `🚨()` | Action : Activer/Désactiver phares | `toggle_lights()` |
| `⬆️(X)` / `⬇️(X)` | Déplacements | Avancer / Reculer de X cases |
| `➡️(X)` / `⬅️(X)` | Déplacements | Tourner à droite / gauche et avancer de X cases |
| `🃏(X)` | Mode fou (Crazy mode) | Mouvements et lumières aléatoires pendant X secondes |
| `[nom]` | Identifiant d'une variable ou fonction | Nom de variable entouré de crochets (ex: `[compteur]`) |
| `0️⃣` / `1️⃣` | Index de tuple | Accès aux composants du tuple (ex: `[t]0️⃣`) |

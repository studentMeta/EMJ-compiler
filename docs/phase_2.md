# Phase 2 : Compilateur EMJ - EMJVisitor

Cette deuxième partie du projet se concentre sur l'aspect de vérification sémantique via la redéfinition (override) des méthodes générées par Maven.

Elle repose sur trois éléments principaux :

## 1. Une analyse sémantique

L'analyse sémantique consiste à vérifier le sens et la cohérence d'une déclaration. Étant donné que le lexer et le parseur ont déjà effectué l'analyse syntaxique en amont, nous savons d'avance que le code soumis au compilateur pour cette phase possède déjà une structure valide et une syntaxe stricte.

> TBD: Explication de l'implémentation de la classe `EMJVisitor`

## 2. Une table des symboles (Symbol Table)

### 2.1 Raisonnement initial et évolution

La version implémentée se base sur le raisonnement de Timothy, qui avait proposé une première approche sous la forme d'un vecteur de vecteurs. L'idée était de reproduire le comportement d'une pile d'appels, inspirée des concepts vus dans le cours *Fonctions et concepts des ordinateurs* - notamment la gestion de la pile lors de l'exécution de fonctions récursives en assembleur MIPS. Cette structure en tableau 2D dynamique permettait de gérer les portées de manière simple, avec des opérations `push` et `pop` sur les vecteurs.

Cependant, en approfondissant les besoins du projet, j'ai constaté que cette structure plate présentait des limitations. En particulier, lorsqu'une fonction est déclarée après la fonction `main` et qu'elle accède à des paramètres définis dans son propre contexte, le modèle à deux niveaux (global ↔ fonction) ne permettait pas de remonter correctement la chaîne des contextes. J'ai donc fait évoluer l'implémentation vers un **modèle arborescent avec pile de navigation**, qui conserve l'esprit de la pile d'appels tout en supportant l'imbrication.

### 2.2 La classe `Symbol`

Avant de structurer la table elle-même, il fallait définir ce qu'on y stocke. J'ai créé une classe `Symbol` qui représente une entrée individuelle dans la table. Chaque symbole encapsule quatre informations essentielles :

- **Le nom** (`name`) : l'identifiant emoji tel qu'il apparaît dans le code source (par exemple `[🧠]`). On le récupère directement via `ctx.EMOJI_ID().getText()` dans le visiteur.

- **Le genre** (`Kind`) : une énumération distinguant trois catégories - `VARIABLE`, `FUNCTION` et `PARAMETER`. Cette distinction est nécessaire car un paramètre de fonction et une variable locale portant le même identifiant dans le même scope doivent déclencher une erreur sémantique (section 6.4.2 de l'énoncé), tandis qu'une fonction et une variable portant le même nom coexistent dans des espaces différents (global vs. local).

- **Le type** (`Type`) : une énumération couvrant les types du langage EMJ - `INT`, `CHAR`, `STRING`, `BOOL`, `TUPLE_INT`, `TUPLE_CHAR`, `TUPLE_STRING`, `TUPLE_BOOL` et `VOID`. J'ai choisi de représenter les types de tuples de manière aplatie (par exemple `TUPLE_INT` plutôt qu'un type générique `TUPLE` paramétré) afin de simplifier les comparaisons de types dans le visiteur. Lorsqu'on rencontre une déclaration comme `👥(🔢) [🐸🌍] = (12, 3);`, le type résolu est directement `TUPLE_INT`, ce qui permet de vérifier en une seule comparaison que l'expression droite est bien du même type.

- **Le scope** (`scope`) : le nom du contexte dans laquelle le symbole est déclaré, permettant de savoir à quelle fonction il appartient.

Pour les fonctions, le symbole stocke également les **types et noms des paramètres** sous forme de tableaux. Ces informations sont indispensables lors de la vérification d'un appel de fonction : on compare le nombre d'arguments fournis au nombre attendu, puis on vérifie que chaque argument correspond au type du paramètre correspondant, dans le bon ordre.

### 2.3 La classe `SymbolTable`

#### Architecture en arbre de portées

La table des symboles repose sur une classe interne `Scope` qui forme les nœuds d'un arbre. Chaque `Scope` contient :

- Un **nom** identifiant du contexte (par exemple `"global"`, `"main"`, `"[👾]"`).
- Une **référence vers le scope parent** (`parent`), qui est `null` pour le scope global. C'est ce lien parent qui permet la résolution lexicale en remontant la chaîne.
- Une **liste de symboles** déclarés directement dans ce contexte.
- Une **map des scopes enfants**, indexée par nom, permettant de créer et retrouver les sous-portées.

À l'initialisation, la table crée un scope racine `"global"` qui contiendra les déclarations de fonctions. Une `LinkedList<Scope>` sert de **pile de navigation** pour suivre le chemin de portées actuel lors de la visite de l'AST.

#### Gestion de l'entrée et de la sortie des portées

Lorsque le visiteur entre dans une fonction (par exemple en visitant `mainFunction` ou `functionDeclaration`), il appelle `enterScope("main")` ou `enterScope("[👾]")`. Cette méthode crée un nouveau scope enfant rattaché au scope courant, et le pousse sur la pile. À la sortie de la fonction, `exitScope()` dépile et remonte au scope parent. Ce mécanisme garantit que les symboles déclarés dans une fonction ne sont visibles que dans celle-ci et dans ses éventuels sous-scopes.

#### Stratégies de recherche

J'ai distingué deux types de recherche, car les besoins sont différents selon le contexte :

**Recherche locale** (méthodes `lookupVariableLocal`, `lookupParameterLocal`) : ces méthodes ne consultent que le scope courant, sans remonter la chaîne. Elles sont utilisées lors des **déclarations**, pour vérifier qu'un identifiant n'est pas déjà pris dans le contexte courant. Par exemple, avant d'ajouter une variable au scope, on appelle `lookupVariableLocal(name)` pour s'assurer qu'elle n'existe pas déjà, puis `lookupParameterLocal(name)` pour vérifier qu'elle n'entre pas en conflit avec un paramètre.

**Recherche avec remontée de chaîne** (méthode `lookupVariable`) : cette méthode parcourt le scope courant, puis le parent, puis le grand-parent, et ainsi de suite jusqu'au scope global (exclu, puisque l'énoncé spécifie qu'il n'y a pas de variables globales). Elle est utilisée lors de l'**utilisation** d'une variable - par exemple dans une expression gauche ou droite - pour vérifier qu'elle a été déclarée quelque part dans le contexte visible.

Une troisième méthode, `lookupVariableInParentScopes`, permet de chercher uniquement dans les scopes parents en excluant le scope courant. Elle pourrait servir à détecter le shadowing (une variable locale qui masque une variable d'un scope englobant), même si l'énoncé ne l'exige pas explicitement.

Enfin, `lookupFunction` cherche exclusivement dans le scope global, puisque toutes les fonctions sont déclarées au niveau global dans EMJ.

#### Exemple concret de fonctionnement

Prenons le programme EMJ suivant :

```
📦 "test.map"
🌀 🏠(){
    🔢 [🎃] = 5;
    🔢 [🧠] = [👾](4, [🎃]);
    ↩️ 🌀;
}

🔢 [👾](🔢 [🐝], 🔢 [👽]){
    🔢 [🔥] = [🐝] + [👽];
    ↩️ [🔥];
}
```

Lors de la visite, la table évolue ainsi :

1. **Passe préliminaire** - Le scope global reçoit les fonctions `main` et `[👾]` avec leurs signatures.
2. **Visite de `main`** - `enterScope("main")` crée le scope `global > main`. Les variables `[🎃]` et `[🧠]` y sont ajoutées. L'appel `[👾](4, [🎃])` déclenche un `lookupFunction("[👾]")` dans le scope global, puis la vérification des types de paramètres.
3. **Visite de `[👾]`** - `enterScope("[👾]")` crée le scope `global > [👾]`. Les paramètres `[🐝]` et `[👽]` sont ajoutés comme `PARAMETER`. La variable `[🔥]` est ajoutée comme `VARIABLE`. Lors du `lookupVariable("[🐝]")` dans l'expression `[🐝] + [👽]`, la recherche trouve le paramètre dans le scope courant.
4. **Sortie** - `exitScope()` à chaque fin de fonction ramène au scope global.

## 4. Un gestionnaire d'erreurs (Error Logger)

Aucune modification n'a été apportée au gestionnaire d'erreurs existant. En effet, celui-ci remplit parfaitement son rôle initial : recueillir les différentes erreurs rencontrées lors de l'analyse et les afficher au moment de la compilation, le cas échéant.
# Phase 1 : Compilateur EMJ - Lexer & Parser

La première phase du projet consiste a implémenter l'analyse lexico-syntaxique du langage En-Mode-J. L'objectif est de construire une grammaire ANTLR capable de reconnaitre et de valider la structure de tout fichier `.moj` (programme) ou `.map` (carte de jeu) conforme à la spécification. Aucune génération de code n'est effectuée à ce stade, il s'agit uniquement de vérifier que le code en entrée est syntaxiquement correct.

## 1. Outils utilisés

J'ai utilisé **ANTLR 4** pour écrire la grammaire. Le principe est simple, on décrit le langage dans des fichiers `.g4`, et ANTLR génère automatiquement les classes Java correspondantes (lexer, parser, visitor de base). Maven se charge d'appeler ANTLR lors de la phase de compilation via le plugin dédié, ce qui fait que les sources Java sont regénérées a chaque `mvn compile`.

La grammaire est séparée en deux fichiers distincts :

- **`EMJLexer.g4`** : le lexer, qui défini les tokens, c'est à dire les unités lexicales du langage (mots-clés, opérateurs, valeurs littérales, identifiants emoji, commentaires, ...).
- **`EMJParser.g4`** : le parser, qui décrit comment ces tokens se combinent pour former des constructions valides (déclarations, instructions, expressions, ...).

Cette séparation permets de garder une vision claire, le lexer s'occupe de *quoi* reconnaitre, le parser s'occupe de *comment* les éléments s'assemblent.

## 2. Méthodologie de travail - TDD

J'ai adopté une approche de type **Test-Driven Development** (TDD), en travaillant fonctionnalité par fonctionnalité. Le processus était le suivant :

1. **Écrire un test** pour la fonctionnalité visée (par exemple, la déclaration de variable avec initialisation).
2. **Écrire le minimum de grammaire** nécessaire pour que le test passe.
3. **Vérifier** qu'aucun autre test ne casse suite à la modification.
4. **Itérer** en ajoutant les cas limites et les cas d'erreur.

Les fichiers de test sont organisés dans `src/test/resources/` et répartis en deux catégories :

- **`passed/`** : des programmes EMJ valides que le parser doit accepter sans erreur.
- **`failed/`** : des programmes EMJ volontairement invalides que le parser doit rejeter.

Les deux catégories sont tout aussi importantes l'une que l'autre. Il est assez facile d'écrire une grammaire trop permissive qui accepte n'importe quoi, les tests en `failed/` sont la pour s'assurer que la grammaire rejète bien les constructions incorrectes. Par exemple, un test pourrait vérifier qu'une déclaration de variable sans point-virgule à la fin est bien refusée, ou qu'un identifiant sans crochets provoque une erreur syntaxique.

Chaque catégorie de test correspond à un aspect précis de la grammaire : actions prédéfinies (`ActionTest`), commentaires (`CommentTest`), conditions (`ConditionTest`), expressions (`ExpressionTest`), fonctions (`FunctionTest`), boucles (`LoopTest`), cartes (`MapTest`), structure générale (`StructureTest`), et variables (`VariableTest`).

## 3. Le Lexer - `EMJLexer.g4`

Le lexer est le premier composant a intervenir dans la chaîne d'analyse. Son rôle est de transformer le flux de caractères brut en une séquence de tokens identifiés.

### 3.1 Gestion des espaces et commentaires

Les espaces, tabulations et retours à la ligne sont ignorés via la directive `-> skip`. De même, les deux formes de commentaires définies par EMJ sont traitées directement au niveau du lexer :

- Les **commentaires de ligne** (📢, `U+1F4E2`) : tout ce qui suit cet emoji jusqu'a la fin de la ligne est ignoré.
- Les **commentaires de bloc** (🔊 ... 🔈, `U+1F50A` ... `U+1F508`) : tout le contenu entre ces deux emojis est ignoré, potentiellement sur plusieurs lignes. Le `.*?` (non-greedy) dans la règle assure que le premier 🔈 rencontré ferme le commentaire, ce qui empêche l'imbrication de commentaires (conforme a l'énoncé).

### 3.2 Emojis et Unicode

Le point le plus délicat du lexer a été la gestion des emojis. En ANTLR, les emojis ne s'écrivent pas directement, il faut utiliser leur code Unicode sous la forme `'\u{XXXX}'`. Par exemple, le booléen vrai (✅) s'écrit `'\u2705'` dans la grammaire.

Certains emojis sont composés de plusieurs code points Unicode. C'est le cas notamment des flèches directionnelles qui incluent le Variant Selector (`U+FE0F`), comme ⬆️ qui s'écrit `'\u2B06\uFE0F'`. Les indices de tuple (0️⃣ et 1️⃣) sont encore plus complexes car ils combinent trois code points : le chiffre, le Variant Selector, et le Combining Enclosing Keycap (`U+20E3`).

Pour les identifiants emoji (les noms de variables et fonctions entre crochets), un fragment `EMOJI` pré-définit nous a été donner celui-ci utilisant la classe de caractères Unicode `\p{Emoji}`, combinée aux caractères `\uFE0F` (Variant Selector) et `\u200D` (Zero Width Joiner). Le token `EMOJI_ID` est ensuite défini comme un ou plusieurs emojis entourés de crochets : `LBRACKET EMOJI+ RBRACKET`.

### 3.3 Valeurs littérales

Les règles pour les valeurs littérales suivent la spécification de l'énoncé :

- **Entiers** : un entier est soit un `0` seul, soit un chiffre non-nul suivi de zéro ou plusieurs chiffres, potentiellement précédé d'un signe moins. La règle `INT_VALUE : (MINUS? NONZERO_DIGIT DIGIT* | '0')` gère cette distinction directement au niveau lexical, ce qui allège un peu les vérifications sémantiques ulterieures.
- **Caractères** : un seul caractère (lettre minuscule/majuscule, chiffre, ou espace) entre guillemets simples.
- **Chaînes de caractères** : zéro ou plusieurs caractères valides entre guillemets doubles.

J'ai utilisé des **fragments** (`fragment DIGIT`, `fragment NONZERO_DIGIT`, `fragment CHAR_CONTENT`) pour factoriser les sous-règles réutilisées dans plusieurs tokens. Les fragments ne créent pas de tokens, ils servent uniquement de briques de construction pour d'autres règles lexicales.

### 3.4 Ordre des règles

L'ordre des règles dans le lexer est important en ANTLR : en cas d'ambiguïté, c'est la première règle qui correspondante qui l'emporte. J'ai donc placé les tokens composés (comme `<=`, `>=`, `==`, `!=`) avant les tokens simples (`<`, `>`, `=`) pour éviter qu'un `<=` soit interprêté comme un `<` suivi d'un `=`.

## 4. Le Parser - `EMJParser.g4`

Le parser défini les règles de grammaire hors-contexte qui structurent le langage. Il s'appuie sur les tokens définis dans le lexer via la directive `options { tokenVocab = EMJLexer; }`.

### 4.1 Racine de la grammaire

La règle racine `root` reflète le fait qu'un fichier EMJ peut être soit une carte, soit un programme (mais pas les deux) :

```
root : (map | program) EOF;
```

Le terminal `EOF` force ANTLR a consommer l'intégralité du fichier, ce qui garantit qu'aucun contenu résiduel n'est ignoré silencieusement.

### 4.2 Carte de jeu

Une carte commence toujours par un en-tête (`mapHeader`) qui spécifie les dimensions et l'orientation initiale du robot, suivi d'un ou plusieurs éléments de carte (`mapElement`). L'en-tête suit la syntaxe `🗺️ with L, C, direction;` où L et C sont des entiers et la direction est une des quatre flèches directionnelles.

Les éléments de carte sont définis comme des alternatives simples : police (🚔), route (🛣️), volcan (🌋), maison (🏘️), travaux (🚧), tracteur (🚜), eau (🌊), ou voleur (🦹). La vérification que la carte contient exactement un policier et au moins un voleur est deleaguée à la phase sémantique.

### 4.3 Structure d'un programme

Un programme EMJ suit une structure fixe définie par la règle :

```
program : importDeclaration mainFunction functionDeclaration*;
```

Cela impose un ordre strict, d'abord l'import de carte (📦 suivi du nom de fichier), ensuite la fonction main obligatoire, puis les déclarations de fonctions supplémentaires (optionelles). Cet ordre est directement encodé dans la grammaire, ce qui signifie qu'un programme qui déclarerait une fonction avant le main serait rejeté syntaxiquement.

La fonction main a une syntaxe particulière : elle utilise l'emoji 🏠 au lieu d'un identifiant entre crochets, et son type est obligatoirement void (🌀). Son retour doit également être `↩️ 🌀;`.

### 4.4 Déclarations de fonctions

Pour les fonctions utilisateur, j'ai choisi de séparer les deux cas en deux alternatives dans la règle `functionDeclaration` :

1. Une fonction avec type de retour (un `typeDeclaration` suivi de l'identifiant), qui se termine par `↩️ rightExpression;`.
2. Une fonction void (🌀 suivi de l'identifiant), qui se termine par `↩️ 🌀;`.

Cette séparation clarifie la structure attendue dans chaque cas, même si elle aurait pu être unifiée en une seule règle au prix d'un peu plus de travail sémantique.

### 4.5 Les expressions - `rightExpression`

La règle `rightExpression` est probablement la plus complexe de la grammaire. Elle gère toutes les expressions qui produisent une valeur : entiers, booléens, chaînes de caractères, caractères, tuples, appels de fonction, accès aux variables, et les combinaisons via des opérateurs arithmétiques, de comparaison et logiques.

La **priorité des opérateurs** est encodée directement dans l'ordre des alternatives de la règle, en exploitant le mécanisme de résolution d'ambiguïté d'ANTLR pour les règles récursives à gauche. L'ordre est le suivant (du plus prioritaire au moins prioritaire) :

1. Parenthèses et négation (`NOT`, unaire `-`)
2. Multiplication et division (`*`, `/`)
3. Addition et soustraction (`+`, `-`)
4. Comparaisons strictes et larges (`<`, `>`, `<=`, `>=`)
5. Égalité et inégalité (`==`, `!=`)
6. ET logique (🖇️)
7. OU logique (📎)

Cet ordre correspond a la convention habituelle utilisée en C et dans la plupart des langages de programmation. ANTLR 4 gère nativement la récursion à gauche dans les règles, ce qui permet d'écrire les alternatives de manière lisible sans devoir créer manuellement des niveaux de précédence intermédiaires (comme on le ferait dans une grammaire LL(1) classique).

J'ai aussi défini une règle `arithmeticExpression` séparée, utilisée spécifiquement pour les paramètres des actions prédéfinies (⬆️(n), ⬇️(n), ...). Cette règle est plus restrictive que `rightExpression` car elle n'accepte que des expressions dont le résultat est entier.

### 4.6 Expressions gauches

La règle `leftExpression` est volontairement simple, elle permet de référencer une variable par son identifiant, ou d'accéder à un élément d'un tuple via les emojis 0️⃣ et 1️⃣. J'ai séparé les trois cas en alternatives distinctes plutôt que de rendre l'index optionnel, ce qui rend l'AST plus explicite et facilite la visite sémantique.

### 4.7 Choix de conception - grammaire permissive

Conformement à la note importante 2 de l'énoncé, j'ai fait le choix d'une **grammaire plutôt permissive** au niveau syntaxique. Par exemple, la grammaire accepte syntaxiquement une déclaration comme `🔢 [🐶] = "hello";` (un type entier avec une valeur chaîne), bien que cela soit sémantiquement invalide. Ce choix réduit la complexité de la grammaire et reporte ces vérifications de cohérence de type à la phase sémantique, où elles sont plus naturelles a implémenter via le visiteur.

## 5. Références

- Documentation ANTLR 4 : https://github.com/antlr/antlr4/blob/master/doc/index.md
- Référence des grammaires ANTLR : https://github.com/antlr/antlr4/blob/master/doc/grammars.md
- Unicode emoji list (pour les codes `U+XXXX`) : https://unicode.org/emoji/charts/emoji-list.html
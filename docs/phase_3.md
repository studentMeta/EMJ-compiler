# Phase 3 : Génération de code 

Pour cette troisième partie du projet, on va suivre la même logique que lors de la deuxième. 
On va utiliser un visiteur pour parcourir le code source moj au travers des règles de grammaires précisées dans notre parser. 
A chaque niveau de contexte, on s'assure que l'ensemble des règles de grammaires possibles pour un non-terminal soit accessible. 
Soit celles-ci nous renvoient vers la visite d'un autre non-terminal (d'un autre contexte), soit renvoient une valeur textuelle qui composera la ligne de code python qui correspond à la ligne de code moj en cours de lecture par le visiteur.
Cette arbre dédié a ensuite été lié au projet via le Main afin d'entamer sa visite si la visite du visiteur sémantique était effectué avec succès.

## 1. Création de la classe auxiliaire EMJPythonCodeGenerator

Comme Python est un langage qui nécessite une bonne gestion des indentations pour représenter les blocs de code (qui en moj sont représentés par des crochets {}), nous avons mis en place une classe auxiliaire CodeGenerator qui permet deux choses : 
* Stocker en mémoire le niveau d'indentation et faire fluctuer celui-ci à chaque entrée/sortie de fonction, de then, de else et de boucle. 
* Encoder les lignes de code dans un StringBuilder en ajoutant les indentations nécessaires en fonction du niveau d'indentation. 
Enfin, on notera quand même que cette classe permet également de récupérer le contenu du StringBuilder dans un String afin de pouvoir écrire facilement le code dans un fichier python. 

## 2. Ajout des lignes de codes au fil de la visite du visiteur dédié à la génération de code

Afin de ne pas tout mélanger et de ne pas "casser" la logique relative au visiteur sémantique, nous avons recréé un nouveau visiteur entièrement dédié à la génération de code.
Suivant rigoureusement la même structure que son frère orienté sur la sémantique, cet arbre nous permet de visiter les différents contextes (les non-terminaux de la grammaire) et de traduire en code python les lignes de codes moj une fois celles-ci entièrement découvertes par le visiteur.

D'un point de vue architectural, notre visiteur distingue deux comportements majeurs selon le type de noeud parcouru :
* Les instructions et déclarations (boucles, conditions, appels de fonctions) qui, lors de leur visite, utilisent les méthodes de notre CodeGenerator (comme addLine() ou increaseIndentation()) pour écrire directement la structure du code python.
* Les expressions qui contiennent les valeurs (calculs, lectures de variables…). La visite de ces noeuds retourne des chaînes de caractères. Ces chaînes remontent l'arbre syntaxique pour être finalement insérées dans les instructions parentes (pour compléter la condition d'un if ou la partie droite d'une assignation par exemple).
 

### 2.1 Obstacles rencontrés et solutions apportées

#### 2.1.1 Conversion des emoji en String

Comme les noms des variables et des fonctions en emoji ne peuvent pas être lues par python, nous avons opté pour une approche de traduction déterministe se basant sur le unicode des emoji. Cela s'est fait au travers de la méthode utilitaire emojiToPython(String emoji) pour laquelle nous avons sollicité l'IA générative afin de trouver la bonne formule de traduction à savoir :
 
```java
emoji.codePoints().forEach(codePoint -> {
pythonVariable.append("_").append(Integer.toHexString(codePoint).toUpperCase());
});
```
#### 2.1.2 Gestion du cutebot et adaption au hardware Micro:bit

Une fois le visiteur général terminé, nous nous sommes penchés sur la gestion du cutebot. Pour cela il nous est rapidement apparu qu'il importait de pouvoir calculer les rotations du cutebot en fonction de deux éléments : l'orientation d'origine du cutebot et l'orientation de destination du cutebot.
Une manière assez évidente de faire est d'attribuer une valeur angulaire à chaque orientation. Ainsi, nous avons, par convention et bon sens, décidé que le nord représenterait 0°, l'est 90°, le sud 180° et l'ouest 270° (soit -90°). 
En soustrayant la valeur angulaire de la source à la valeur de destination, nous pouvons alors déterminer de combien de degrés doit tourner le cutebot. 
Pour gérer cela de manière simple et sans surcharger le code du visiteur de génération de code, nous avons décidé d'implémenter une libraire custom directement en python qui serait importé directement dans le code lors de la compilation.

Ensuite, nous avons profité de l'existence de cette libraire custom pour ajouter l'ensemble des méthodes relatives au cutebot. Cela nous a permis de coder directement en python toutes les méthodes prédéfinies du cutebot : arrest(), sound(),lights(), etc.. 
Cela rend notre code java du visiteur beaucoup plus concis et propre et nous a permis d'aller beaucoup plus loin dans le codage de certaines méthodes (arrest() est un bon exemple, elle contient à peu près tout ce que le cutebot peut faire).
Dans cette librairie, nous avons donc également contourné le fonctionnement séquentiel des instructions en utilisant des tâches en arrière-plan afin de pouvoir à la fois faire bouger le cutebot et utiliser ses lumières et/ou sa sirène.
Voici un exemple relatif à la gestion de la sirène (basé sur deux tonalités qui s'alternent) qui contient une variable globale, un handler et l'appel de ce handler en background : 

```python
siren_on = False

def siren_handler():
    while True:
        if siren_on and not crazy_on:
            music.play_tone(Note.B5, 400) #tonalité et durée en ms
            music.play_tone(Note.E5, 400)
        else:
            basic.pause(100)
            
control.in_background(siren_handler)
```

#### 2.1.3 Récupération de l'orientation initiale du cutebot 

Une fois l'astuce trouvée pour diriger correctement le cutebot selon une orientation d'origine et une orientation cible, nous avons du trouver l'orientation originale.  
Le problème est que cette orientation se trouve dans le fichier .map qui n'est pas réellement importé par le visiteur dans le code python final. Nous avons mis en place un troisième mini visiteur qui se charge uniquement d'aller visiter le fichier .map repris en import pour descendre jusqu'au contexte qui concerne cette orientation initiale ().
D'ailleurs, il s'agit là aussi d'une limitation du microbit qui ne lit pas réellement les lignes "import xxx".
Dans notre fonction visitImportDeclaration(), nous avons donc, sur base du nom de l'import et d'un dossier prédéfini dans le code contenant les fichiers .map visiter ce fichier depuis le root jusqu'au terminal représentant cette orientation : 
``` java 
[...]
 try {
            String cheminMap = "src/main/resources/source_files/" + fileName.replace("\"", "");
            EMJLexer mapLexer = new EMJLexer(CharStreams.fromFileName(cheminMap));
            EMJParser mapParser = new EMJParser(new CommonTokenStream(mapLexer));

            // et on vient directement récupérer l'orientation initiale en ciblant uniquement cette info
            // en pointant sur l'élément pertinent direction du parser :
                // root : (map | program) EOF;
                // map : mapHeader mapElement+;
                // mapHeader : MAP_START WITH INT_VALUE COMMA INT_VALUE COMMA direction SEMICOLON;
                // direction : ACT_UP | ACT_DOWN | ACT_RIGHT | ACT_LEFT;

            EMJParser.RootContext mapTree = mapParser.root();
            if (mapTree.map() != null) {
                initialOrientation = visit(mapTree.map().mapHeader().direction());
            }
[...]
```

## 3. Génération du code

Une fois le projet java compilé, nous pouvons générer le code et l'exécuter en utilisant maven (qui gèrera les librairies externes et dépendances) grâce à l'instruction : 
```bash
mvn exec:java -Dexec.mainClass="be.unamur.info.b314.compiler.main.Main" -Dexec.args="-i src/main/resources/source_files/demo_predefined_actions.moj -o final_test.py"
```
, ce qui fort heureusement est simplifié dans intellij avec une configuration des arguments de l'application.






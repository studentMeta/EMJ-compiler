package be.unamur.info.b314.compiler.emj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Table des symboles pour le compilateur EMJ.
 * Gère les symboles (variables, fonctions, paramètres) en tenant compte de leur contexte (imbriquées ou non).
 *
 * Utilise un modèle d'arborescence de contextes en utilisant une pile pour la gestion :
 * - Chaque contexte possède un nom, une liste de symboles et une référence à son contexte parent.
 * - Lors de l'entrée dans un nouveau contexte (fonction, bloc, etc.), le contexte enfant est ajouté sur la pile.
 * - À la sortie, le contexte est retiré de la pile.
 * - La recherche de symboles remonte la chaîne des contextes du plus récent au plus ancien (actuel -> parent -> ... -> global).
 */
public class SymbolTable {

    // =========================================================================
    // CLASSE INTERNE : SCope (contexte)
    // =========================================================================

    /**
     * Représente un contexte unique dans l'arborescence des contextes.
     * Chaque contexte connaît son contexte parent, ce qui permet une recherche lexicale (imbriquée).
     */
    private static class Scope {
        /** 
         * Nom lisible par l'utilisateur de ce contexte (par exemple, "global", "main", "maSuperFonction", "[🇧🇪]"). 
         */
        final String name;

        /** 
         * Référence au contexte parent.
         * Si null, il s'agit du contexte global (racine de l'arborescence). 
         */
        final Scope parent;

        /** 
         * Liste des symboles déclarés directement dans ce contexte. 
         */
        final List<Symbol> symbols;

        /** 
         * Contextes enfants, indexés par leurs noms. 
         */
        final Map<String, Scope> children;

        /**
         * Constructeur pour créer un nouveau contexte avec un nom et une référence à son parent.
         * 
         * @param name le nom du contexte (ex: "main", "maSuperFonction", "[🇧🇪]", etc.)
         * @param parent la référence au contexte parent (null pour le contexte global)
         */
        Scope(String name, Scope parent) {
            this.name = name;
            this.parent = parent;
            this.symbols = new ArrayList<>();
            this.children = new HashMap<>();
        }

        @Override
        public String toString() {
            return "Scope{" + name + ", symbols=" + symbols.size() + "}";
        }
    }

    // =========================================================================
    // CHAMPS DE LA TABLE DES SYMBOLES
    // =========================================================================

    /** 
     * Le contexte racine (globale). 
     * 
     * Contient les fonctions ou variables déclarées au niveau global et sert de point d'entrée pour la recherche de symboles.
     */
    private final Scope globalScope;

    /**
     * Le contexte actuel dans lequel on se trouve (peut être global, main, ou une fonction imbriquée).
     * Toutes les opérations d'ajout ou de recherche de symboles se font à partir de ce contexte.
     */
    private Scope currentScope;

    /**
     * Pile de contextes pour gérer les entrées/sorties de contextes imbriqués.
     */
    private final LinkedList<Scope> scopeStack;

    // =========================================================================
    // CONSTRUCTEUR DE LA TABLE DES SYMBOLES
    // =========================================================================

    /**
     * Constructeur de la table des symboles. Initialise la table et définit le contexte courant à "global".
     * 
     * Le contexte global est créé comme le contexte racine de l'arborescence des contextes, sans parent.
     * Le contexte courant est initialisé à ce contexte global, et la pile de contextes est initialisée avec ce contexte global comme point de départ.
     */
    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = globalScope;
        this.scopeStack = new LinkedList<>();
        this.scopeStack.push(globalScope);
    }

    // =========================================================================
    // GESTION DES CONTEXTES
    // =========================================================================

    /**
     * Permet d'entrer dans un nouveau contexte enfant au sein du contexte actuelle.
     * Si un contexte enfant portant ce nom existe déjà sous le contexte actuelle, celui-ci est réutilisé. 
     * Sinon, un nouveau contexte est créé et ajouté à la pile de contextes.
     *
     * @param scopeName : le nom du contexte dans lequel entrer (ex: "main", "maSuperFonction", "[🇧🇪]", ...)
     */
    public void enterScope(String scopeName) {
        Scope child = currentScope.children.get(scopeName);
        if (child == null) {
            child = new Scope(scopeName, currentScope);
            currentScope.children.put(scopeName, child);
        }
        currentScope = child;
        scopeStack.push(child);
    }

    /**
     * Quitte le contexte actuelle et revient au contexte parent.
     * Si l'on se trouve déjà dans le contexte global, cette opération n'a aucun effet.
     */
    public void exitScope() {
        if (currentScope.parent != null) {
            scopeStack.pop();
            currentScope = currentScope.parent;
        }
    }

    /**
     * Retourne le nom du contexte actuel.
     *
     * @return le nom du contexte actuel
     */
    public String getCurrentScope() {
        return currentScope.name;
    }

    /**
     * Récupère le chemin d'accès complet sous forme d'une chaîne de caractères.
     * Utile pour le débogage et les messages d'erreur.
     *
     * @return le chemin d'accès complet du contexte actuel (ex: "global > main > maSuperFonction")
     */
    public String getScopePath() {
        List<Scope> path = new ArrayList<>(scopeStack);
        StringBuilder sb = new StringBuilder();
        for (int size = path.size() - 1; size >= 0; size--) {
            sb.append(path.get(size).name);
            if (size > 0) sb.append(" > ");
        }
        return sb.toString();
    }

    /**
     * Retourne la profondeur d'imbrication du contexte actuel (ex: 0 = global, 1 = main, ...).
     *
     * @return la profondeur du contexte actuel
     */
    public int getDepth() {
        return scopeStack.size() - 1;
    }

    // =========================================================================
    // AJOUT DE SYMBOLES AU CONTEXTE ACTUEL
    // =========================================================================

    /**
     * Ajoute un symbole au contexte actuel.
     *
     * @param symbol le symbole à ajouter au contexte actuel
     */
    public void addSymbol(Symbol symbol) {
        currentScope.symbols.add(symbol);
    }

    /**
     * Ajoute un symbole de fonction au contexte global, indépendamment du contexte actuel.
     *
     * @param symbol le symbole de fonction à ajouter au contexte global
     */
    public void addFunction(Symbol symbol) {
        globalScope.symbols.add(symbol);
    }

    // =========================================================================
    // RECHERCHE DE SYMBOLES DANS LE CONTEXTE ACTUEL (SANS IMBRICATION)
    // =========================================================================

    /**
     * Recherche une variable dans le contexte actuel uniquement.
     * Ne remonte PAS l'arborescence des contextes.
     *
     * @param name l'identifiant à rechercher
     * @return le symbole s'il est trouvé, null sinon
     */
    public Symbol lookupVariableLocal(String name) {
        return lookupInScope(currentScope, name, Symbol.Kind.VARIABLE);
    }

    /**
     * Recherche un paramètre dans le contexte actuel uniquement.
     *
     * @param name l'identifiant à rechercher
     * @return le symbole s'il est trouvé, null sinon
     */
    public Symbol lookupParameterLocal(String name) {
        return lookupInScope(currentScope, name, Symbol.Kind.PARAMETER);
    }

    /**
     * Recherche une variable ou un paramètre dans le contexte actuel uniquement.
     *
     * @param name l'identifiant à rechercher
     * @return le symbole s'il est trouvé, null sinon
     */
    public Symbol lookupVariableOrParamLocal(String name) {
        for (Symbol symbole : currentScope.symbols) {
            if (symbole.getName().equals(name) && (symbole.getKind() == Symbol.Kind.VARIABLE || symbole.getKind() == Symbol.Kind.PARAMETER)) {
                return symbole;
            }
        }
        return null;
    }

    // =========================================================================
    // RECHERCHE DE SYMBOLES (AVEC IMBRICATION)
    //
    // Note:
    //  Si l'implémentation de la recherche de symboles dans les contextes imbriqués
    //  n'est pas utilisée, il suffit de faire appel aux méthodes de recherche locale 
    //  (lookupVariableLocal, lookupParameterLocal, etc.) sur le contexte actuel.
    // =========================================================================

    /**
     * Recherche une variable ou un paramètre en parcourant toute la chaîne des 
     * contextes imbriqués,  depuis le contexte actuel jusqu'au contexte global 
     * (sans l'inclure).
     *
     * Cela implémente le contexte lexicale : une fonction interne peut voir
     * les variables/paramètres de toutes les fonctions qui l'encadrent.
     *
     * @param name l'identifiant à rechercher
     * @return le symbole s'il est trouvé, null sinon
     */
    public Symbol lookupVariable(String name) {
        Scope scope = currentScope;
        while (scope != null && !scope.name.equals("global")) {
            for (Symbol symbole : scope.symbols) {
                if (symbole.getName().equals(name) && (symbole.getKind() == Symbol.Kind.VARIABLE || symbole.getKind() == Symbol.Kind.PARAMETER)) {
                    return symbole;
                }
            }
            scope = scope.parent;
        }
        return null;
    }

    /**
     * Recherche une variable ou un paramètre UNIQUEMENT dans les contextes englobants (parents), en ignorant le contexte actuel. 
     * Utile pour vérifier s'il y a masquage ou si un nom est déjà utilisé dans une portée externe.
     *
     * @param name l'identificateur à rechercher
     * @return le symbole s'il est trouvé dans une portée parente, null dans le cas contraire
     */
    public Symbol lookupVariableInParentScopes(String name) {
        if (currentScope.parent == null) return null;
        Scope scope = currentScope.parent;
        while (scope != null && !scope.name.equals("global")) {
            for (Symbol symbole : scope.symbols) {
                if (symbole.getName().equals(name) && (symbole.getKind() == Symbol.Kind.VARIABLE || symbole.getKind() == Symbol.Kind.PARAMETER)) {
                    return symbole;
                }
            }
            scope = scope.parent;
        }
        return null;
    }

    /**
     * Recherche une fonction par son nom dans l'espace des noms global.
     *
     * @param name l'identifiant de la fonction
     * @return le symbole si la fonction est trouvée, null dans le cas contraire
     */
    public Symbol lookupFunction(String name) {
        return lookupInScope(globalScope, name, Symbol.Kind.FUNCTION);
    }

    // =========================================================================
    // CONVENIENCE CHECKS
    // =========================================================================

    /**
     * Vérifie si une variable ou un paramètre avec le nom donné existe
     * n'importe où dans la chaîne de contextes actuelle (recherche lexicale).
     *
     * @param name l'identifiant à vérifier
     * @return true si trouvé
     */
    public boolean existsInScopeChain(String name) {
        return lookupVariable(name) != null;
    }

    /**
     * Vérifie si une variable ou un paramètre portant le nom indiqué existe
     * uniquement dans le contexte actuel (pas de parcours de la chaîne).
     *
     * @param name l'identifiant à vérifier
     * @return true s'il est trouvé localement
     */
    public boolean existsLocally(String name) {
        return lookupVariableOrParamLocal(name) != null;
    }

    /**
     * Vérifie si une fonction portant le nom indiqué existe dans l'espace de noms global.
     *
     * @param name l'identifiant de la fonction
     * @return true si une fonction avec ce nom existe
     */
    public boolean functionExists(String name) {
        return lookupFunction(name) != null;
    }

    // =========================================================================
    // METHODES PRIVÉES
    // =========================================================================

    /**
     * Recherche un symbole d'un type spécifique dans un seul contexte.
     *
     * @param scope le contexte à parcourir
     * @param name  l'identifiant à rechercher
     * @param kind  le type attendu
     * @return le symbole s'il est trouvé, null sinon
     */
    private Symbol lookupInScope(Scope scope, String name, Symbol.Kind kind) {
        for (Symbol symbole : scope.symbols) {
            if (symbole.getName().equals(name) && symbole.getKind() == kind) {
                return symbole;
            }
        }
        return null;
    }

    // =========================================================================
    // AUTRES MÉTHODES (TOSTRING, DEBUG, ...)
    // =========================================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SymbolTable {\n");
        printScope(sb, globalScope, 1);
        sb.append("}");
        return sb.toString();
    }


    /**
     * Retourne une chaîne de caractères utilisée pour l'indentation.
     * 
     * @param indent le niveau d'indentation (0 = pas d'indentation, 1 = 2 espaces, 2 = 4 espaces, ...)
     * @return une chaîne d'espaces correspondant au niveau d'indentation demandé
     */
    private String indentation(int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            pad.append("  ");
        }
        return pad.toString();
    }

    /**
     * Retourne une représentation textuelle de l'arborescence des contextes et de leurs symboles.
     * 
     * @param sb le constructeur de chaîne pour accumuler la représentation
     * @param scope le contexte à afficher
     * @param indent le niveau d'indentation pour une meilleure lisibilité
     */
    private void printScope(StringBuilder sb, Scope scope, int indent) {
        String pad = indentation(indent);
        sb.append(pad).append("Scope '").append(scope.name).append("': [\n");
        for (Symbol s : scope.symbols) {
            sb.append(pad).append("  ").append(s).append("\n");
        }
        for (Scope child : scope.children.values()) {
            printScope(sb, child, indent + 1);
        }
        sb.append(pad).append("]\n");
    }
}
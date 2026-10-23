package be.unamur.info.b314.compiler.emj;

/**
 * Représente un symbole (variable, fonction ou paramètre) dans la table des symboles.
 * Stocke toutes les métadonnées nécessaires aux vérifications sémantiques.
 */
public class Symbol {

    /**
     * Énumération utilisée pour définir le type de symbole : VARIABLE, FUNCTION ou PARAMETER.
     */
    public enum Kind {
        VARIABLE,
        FUNCTION,
        PARAMETER
    }

    /**
     * Types EMJ, qui correspondent à la déclaration de type de la grammaire.
     */
    public enum Type {
        INT,
        CHAR,
        STRING,
        BOOL,
        TUPLE_INT,
        TUPLE_CHAR,
        TUPLE_STRING,
        TUPLE_BOOL,
        VOID
    }

    /**
     * Champs communs à tous les symboles
     * - name : l'identifiant de l'émoji
     * - kind : VARIABLE, FUNCTION ou PARAMETER
     * - type : le type déclaré
     * - scope : le contexte de la fonction à laquelle appartient ce symbole (utile pour les
     */
    private final String name;
    private final Kind kind;
    private final Type type;
    private final String scope;

    /**
     * Indique si le symbole a recu une valeur.
     * - Pour une VARIABLE : true apres une declaration avec initialisation ou une affectation.
     * - Pour un PARAMETER : toujours true (recoit la valeur a l'appel).
     * - Pour une FUNCTION : toujours true.
     */
    private boolean initialized;

    /** Champs spécifiques aux fonctions
     * - paramTypes : les types des paramètres passés à la fonction
     * - paramNames : les noms des paramètres passés à la fonction
     * - paramCount : le nombre de paramètres passés à la fonction
     */
    private Type[] paramTypes;
    private String[] paramNames;
    private int paramCount;

    /**
     * Constructeur pour les variables et les paramètres.
     *
     * @param name  l'identifiant de l'émoji
     * @param kind  VARIABLE ou PARAMETER
     * @param type  le type déclaré
     * @param scope le contexte de la fonction
     */
    public Symbol(String name, Kind kind, Type type, String scope) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.paramTypes = new Type[0];
        this.paramNames = new String[0];
        this.paramCount = 0;
        this.initialized = (kind == Kind.PARAMETER);
    }

    /**
     * Constructeur pour les fonctions.
     *
     * @param name       l'identifiant de l'émoji
     * @param type       le type de retour
     * @param scope      le contexte de la fonction
     * @param paramTypes les types de paramètres
     * @param paramNames les noms des paramètres
     */
    public Symbol(String name, Type type, String scope, Type[] paramTypes, String[] paramNames) {
        this.name = name;
        this.kind = Kind.FUNCTION;
        this.type = type;
        this.scope = scope;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.paramCount = paramTypes.length;
        this.initialized = true;
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    /**
     * Retourne le nom du symbole.
     *
     * @return le nom du symbole
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le type de symbole (VARIABLE, FUNCTION ou PARAMETER).
     *
     * @return le type de symbole
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Retourne le type déclaré du symbole.
     *
     * @return le type déclaré
     */
    public Type getType() {
        return type;
    }

    /**
     * Retourne le contexte de la fonction à laquelle appartient ce symbole.
     *
     * @return le contexte du symbole
     */
    public String getScope() {
        return scope;
    }

    /**
     * Retourne les types des paramètres d'une fonction.
     *
     * @return un tableau de types des paramètres
     */
    public Type[] getParamTypes() {
        return paramTypes;
    }

    /**
     * Retourne les noms des paramètres d'une fonction.
     *
     * @return un tableau de noms des paramètres
     */
    public String[] getParamNames() {
        return paramNames;
    }

    /**
     * Retourne le nombre de paramètres d'une fonction.
     *
     * @return le nombre de paramètres
     */
    public int getParamCount() {
        return paramCount;
    }

    /**
     * Indique si le symbole a recu une valeur.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Marque le symbole comme initialise (ou non).
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", type=" + type +
                ", scope='" + scope + '\'' +
                '}';
    }
}

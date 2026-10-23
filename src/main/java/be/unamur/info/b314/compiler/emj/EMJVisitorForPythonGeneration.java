package be.unamur.info.b314.compiler.emj;

import be.unamur.info.b314.compiler.EMJLexer;
import be.unamur.info.b314.compiler.EMJParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/*
Visitor class for the EMJ language aiming at converting EMJ into Python
@authors : Group 15
@version : 1.0
*/
public class EMJVisitorForPythonGeneration extends be.unamur.info.b314.compiler.EMJParserBaseVisitor<String> {

    // Variable de rep
    private EMJPythonCodeGenerator codeGenerator;

    // Constructeur
    public EMJVisitorForPythonGeneration() {
        this.codeGenerator = new EMJPythonCodeGenerator();
    }

    // =========================================================================
    // ROOT
    // =========================================================================

    /**
     *
     */
    @Override
    public String visitRoot(EMJParser.RootContext ctx) {
        if (ctx.map() != null) {
            return visit(ctx.map());
        }

        if (ctx.program() != null) {
            return visit(ctx.program());
        }

        return null;
    }

    // ==========================================================================
    // MAP
    // ==========================================================================

    /**
     * @brief Visite le nœud principal de la carte et génère la grille MicroPython
     */
    @Override
    public String visitMap(EMJParser.MapContext ctx) {
        // nécessaire de visiter le
        // header pour générer les variables (lignes, colonnes, orientation)
        visit(ctx.mapHeader());

        // On récupère les dimensions de la map
        int lignes = Integer.parseInt(ctx.mapHeader().INT_VALUE(0).getText());
        int colonnes = Integer.parseInt(ctx.mapHeader().INT_VALUE(1).getText());

        // Déclaration de la liste 2D (grille) en Python
        codeGenerator.addLine("grille = [");
        codeGenerator.increaseIndentation();

        // Index pour parcourir la liste complète des éléments de la carte
        int elementIndex = 0;

        // Double boucle produire la grille
        for (int i = 0; i < lignes; i++) {
            StringBuilder ligneTmp = new StringBuilder();

            ligneTmp.append("[");

            for (int j = 0; j < colonnes; j++) {
                // On visite l'élément à l'index courant
                String mapElem = visitMapElement(ctx.mapElement(elementIndex));
                ligneTmp.append(mapElem);

                // On ajoute une virgule s'il reste d'autres éléments sur cette ligne
                if (j < colonnes - 1) {
                    ligneTmp.append(", ");
                }
                elementIndex++; // On passe à la case suivante
            }

            // Fermeture de la sous-liste
            ligneTmp.append("]");

            // Ajout d'une virgule si ce n'est pas la toute dernière ligne de la carte
            if (i < lignes - 1) {
                ligneTmp.append(",");
            }

            codeGenerator.addLine(ligneTmp.toString());
        }

        // Fermeture de la map
        codeGenerator.decreaseIndentation();
        codeGenerator.addLine("]");

        return null;
    }

    @Override
    public String visitMapHeader(EMJParser.MapHeaderContext ctx) {

        String lignes = ctx.INT_VALUE(0).getText();
        String colonnes = ctx.INT_VALUE(1).getText();
        String orientation = visit(ctx.direction());

        codeGenerator.addLine("map_lignes = " + lignes);
        codeGenerator.addLine("map_colonnes = " + colonnes);
        codeGenerator.addLine("map_orientation = " + visitDirection(ctx.direction()));

        // PAS SUR POUR VISITCHILDREN OU RETURN NULL !!!! A VÉRIFIER !!!!!!!!
        return null;
    }

    @Override
    public String visitDirection(EMJParser.DirectionContext ctx) {

        // pour que le code Python généré soit bien interprété comme un char/string.
        // modif Charles : j'ai changé les up/down etc par les points cardinaux pour mieux coller à l'énoncé

        if (ctx.ACT_UP() != null) {
            return "N"; // Nord
        }
        else if (ctx.ACT_DOWN() != null) {
            return "S"; // South
        }
        else if (ctx.ACT_RIGHT() != null) {
            return "E"; // East
        }
        else if (ctx.ACT_LEFT() != null) {
            return "W"; // West
        }

        return null;
    }

    @Override
    public String visitMapElement(EMJParser.MapElementContext ctx) {

        // Éléments standards
        if (ctx.MAP_POLICE() != null) {
            return "P"; // Police
        }
        else if (ctx.MAP_ROAD() != null) {
            return "R"; // Route
        }
        else if (ctx.MAP_THIEF() != null) {
            return "V"; // Voleur
        }

        // Élements bloquants 'X'
        else if (ctx.MAP_VOLCANO() != null ||
                ctx.MAP_HOUSE() != null ||
                ctx.MAP_CONSTRUCTION() != null ||
                ctx.MAP_TRACTOR() != null ||
                ctx.MAP_WATER() != null) {
            return "'X'";
        }

        // pas censé arriver par vérification antérieure
        return null;
    }

    // =========================================================================
    // PROGRAM
    // =========================================================================

    @Override
    public String visitProgram(EMJParser.ProgramContext ctx) {

        // pour gérer toutes les fonctionnalités du cutebot
        codeGenerator.addLine("from microbit import *");

        // on visite la déclaration de l'import et on en récupère l'orientation initiale
        String initialOrientation = visit(ctx.importDeclaration());

        // Ajout de la libraire custom du cutebot
        addCutebotCustomLibrairy(initialOrientation);

        // pour rester plus fidèle à la structure type d'un fichier python, on va d'abord mettre toutes les foncitons auxiliaires et puis le main
        for (EMJParser.FunctionDeclarationContext funcCtx : ctx.functionDeclaration()) {
            visit(funcCtx);
        }

        if (ctx.mainFunction() != null)
            visit(ctx.mainFunction());

        codeGenerator.addLine("main()");

        return null;
    }

    /**
     */
    private String registerFunctionSignature(EMJParser.FunctionDeclarationContext ctx) {
        return null;
    }

    /**
     */
    @Override
    public String visitImportDeclaration(EMJParser.ImportDeclarationContext ctx) {

        String fileName = ctx.STRING_VALUE().getText();
        String importCleanName = fileName
                .replace("\"", "")
                .replace(".map", "");

        codeGenerator.addLine("# import " + importCleanName + " (commenté car MakeCode ne sait pas gérer cet import)");

        // par défaut initialOrientation = "N"
        String initialOrientation = "N";


        // on instancie des nouveaux lexer et parser pour parcourir la map en quête de l'orientation initiale
        // en reprenant la même logique que dans le Main
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
        } catch (Exception e) {
            System.err.println("Erreur : Problème de lecture du fichier " + fileName);
        }

        return initialOrientation;
    }

    // =========================================================================
    // FUNCTION && MAIN
    // =========================================================================

    //mainFunction : VOID MAIN LPAREN RPAREN LBRACE instruction* RETURN VOID SEMICOLON RBRACE;
    @Override
    public String visitMainFunction(EMJParser.MainFunctionContext ctx) {

        codeGenerator.addLine("def main():");
        codeGenerator.increaseIndentation();

        if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
            for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                visit(instrCtx);
            }
        } else {
            codeGenerator.addLine("pass");
        }

        codeGenerator.decreaseIndentation();
        codeGenerator.addLine("");
        return null;
    }

    //functionDeclaration : typeDeclaration EMOJI_ID LPAREN functionParameters RPAREN LBRACE instruction* RETURN rightExpression SEMICOLON RBRACE
    //                    | VOID EMOJI_ID LPAREN functionParameters RPAREN LBRACE instruction* RETURN VOID SEMICOLON RBRACE;
    @Override
    public String visitFunctionDeclaration(EMJParser.FunctionDeclarationContext ctx) {

        String functionCleanName = emojiToPython(ctx.EMOJI_ID().getText());

        String joinedParametersCleanNames = "";
        EMJParser.FunctionParametersContext paramsCtx = ctx.functionParameters();
        if (paramsCtx != null) {
            int numberOfParameters = paramsCtx.typeDeclaration().size();
            String[] parametersCleanNames = new String[numberOfParameters];

            for (int i = 0; i < paramsCtx.typeDeclaration().size(); i++) {
                String paramName = emojiToPython(paramsCtx.EMOJI_ID(i).getText());
                parametersCleanNames[i] = paramName;
            }
            joinedParametersCleanNames = String.join(", ",parametersCleanNames);
        }

        codeGenerator.addLine("def " + functionCleanName +"("+ joinedParametersCleanNames + "):" );
        codeGenerator.increaseIndentation();

        if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
            for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                visit(instrCtx);
            }
        } else {
            codeGenerator.addLine("pass");
        }

        if (ctx.rightExpression() != null) {
            String returnValue = visit(ctx.rightExpression());
            codeGenerator.addLine("return " + returnValue);
        } else {
            codeGenerator.addLine("return");
        }

        codeGenerator.decreaseIndentation();
        codeGenerator.addLine("");

        return null;
    }

    // Pas nécessaire, déjà entièrement gérée dans visitFunctionDeclaration
    @Override
    public String visitFunctionParameters(EMJParser.FunctionParametersContext ctx){
        return null;
    }

    // functionCall : EMOJI_ID LPAREN (rightExpression (COMMA rightExpression)*)? RPAREN ;
    @Override
    public String visitFunctionCall(EMJParser.FunctionCallContext ctx) {

        String functionCleanName = emojiToPython(ctx.EMOJI_ID().getText());

        if (ctx.rightExpression() != null && !ctx.rightExpression().isEmpty()) {
            int size = ctx.rightExpression().size();
            String[] rightExpressions = new String[size];

            for (int i = 0; i < size; i++) {
                rightExpressions[i] = visit(ctx.rightExpression(i));
            }

            String joinedRightExpressions = String.join(", ", rightExpressions);
            return functionCleanName + "(" + joinedRightExpressions + ")";
        } else {
            return functionCleanName + "()";
        }
    }

    // Pas nécessaire (Python pas typé)
    @Override
    public String visitTypeDeclaration(EMJParser.TypeDeclarationContext ctx){
        return null;
    }

    // =========================================================================
    // VARIABLES
    // =========================================================================

    // variableDeclaration : typeDeclaration EMOJI_ID (ASSIGN rightExpression)? ;
    @Override
    public String visitVariableDeclaration(EMJParser.VariableDeclarationContext ctx) {

        String variableCleanName = emojiToPython(ctx.EMOJI_ID().getText());

        if (ctx.rightExpression() != null) {
            String rightExpressionValue = visit(ctx.rightExpression());
            codeGenerator.addLine(variableCleanName +" = " + rightExpressionValue);
        } else {
            codeGenerator.addLine(variableCleanName + "= None");
        }

        return null;
    }

    //assignInstruction : leftExpression ASSIGN rightExpression ;
    @Override
    public String visitAssignInstruction(EMJParser.AssignInstructionContext ctx) {

        String left = visit(ctx.leftExpression());
        String right = visit(ctx.rightExpression());

        codeGenerator.addLine(left + " = " + right);

        return null;
    }

    //instruction : variableDeclaration SEMICOLON
    //            | assignInstruction SEMICOLON
    //            | functionCall SEMICOLON
    //            | predefinedAction SEMICOLON
    //            | conditionInstruction
    //            | loopInstruction ;
    @Override
    public String visitInstruction(EMJParser.InstructionContext ctx) {

        // functionCall
        if (ctx.functionCall() != null) {
            String functionCall = visit(ctx.functionCall());
            codeGenerator.addLine(functionCall);

            return null;
        }

        // pour les autres, visiter le contenu enfant de l'instruction
        return visitChildren(ctx);
    }

    //leftExpression : EMOJI_ID
    //               | EMOJI_ID INDEX_0
    //               | EMOJI_ID INDEX_1 ;
    @Override
    public String visitLeftExpression(EMJParser.LeftExpressionContext ctx) {

        String variableCleanName = emojiToPython(ctx.EMOJI_ID().getText());

        if (ctx.INDEX_0() != null) return variableCleanName+"[0]";
        else if (ctx.INDEX_1() != null) return variableCleanName+"[1]";
        else return variableCleanName;
    }

    @Override
    public String visitRightExpression(EMJParser.RightExpressionContext ctx) {
        // A right expression can be many things (lines 170-186 of EMJParser)

        // INT_VALUE | CHAR_VALUE | STRING_VALUE | BOOL_TRUE | BOOL_FALSE
        if (ctx.INT_VALUE() != null) return ctx.INT_VALUE().getText();
        if (ctx.CHAR_VALUE() != null) return ctx.CHAR_VALUE().getText();
        if (ctx.STRING_VALUE() != null) return ctx.STRING_VALUE().getText();
        if (ctx.BOOL_TRUE() != null) return "True";
        if (ctx.BOOL_FALSE() != null) return "False";

        //  leftExpression | functionCall
        if (ctx.leftExpression() != null) return visit(ctx.leftExpression());
        if (ctx.functionCall() != null) return visit(ctx.functionCall());

        //  | LPAREN rightExpression COMMA rightExpression RPAREN -> càd un tuple
        if (ctx.COMMA() != null && ctx.rightExpression().size() == 2)
            // on utilise ici des [ ] pour faire une liste python plutôt que des () comme pour
            // vrai tuple car le tuple est immutable en python
            return "[" + visit(ctx.rightExpression(0)) + "," + visit(ctx.rightExpression(1)) + "]";

        //  LPAREN rightExpression RPAREN
        if (ctx.LPAREN() != null && ctx.RPAREN() != null && ctx.rightExpression().size() == 1)
            return "(" + visit(ctx.rightExpression(0)) + ")";

        //  | NOT rightExpression
        if (ctx.NOT() != null && ctx.rightExpression().size() == 1)
            return "not " + visit(ctx.rightExpression(0));

        //  | rightExpression AND rightExpression
        //  | rightExpression OR rightExpression
        if (ctx.AND() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " and " + visit(ctx.rightExpression(1));
        if (ctx.OR() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " or " + visit(ctx.rightExpression(1));

        //  | rightExpression (LT | GT | LTE | GTE) rightExpression
        //  | rightExpression (EQ | NEQ) rightExpression
        if (ctx.LT() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " < " + visit(ctx.rightExpression(1));
        if (ctx.GT() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " > " + visit(ctx.rightExpression(1));
        if (ctx.LTE() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " <= " + visit(ctx.rightExpression(1));
        if (ctx.GTE() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " >= " + visit(ctx.rightExpression(1));
        if (ctx.EQ() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " == " + visit(ctx.rightExpression(1));
        if (ctx.NEQ() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " != " + visit(ctx.rightExpression(1));

        //  | MINUS rightExpression
        if (ctx.MINUS()!=null && ctx.rightExpression().size() == 1)
            return "-" + visit(ctx.rightExpression(0));

        //  | rightExpression (MULT | DIV) rightExpression
        //  | rightExpression (PLUS | MINUS) rightExpression
        if (ctx.MULT() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " * " + visit(ctx.rightExpression(1));
        if (ctx.DIV() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " / " + visit(ctx.rightExpression(1));
        if (ctx.PLUS() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " + "+ visit(ctx.rightExpression(1));
        if (ctx.MINUS() != null && ctx.rightExpression().size() == 2)
            return visit(ctx.rightExpression(0)) + " - " + visit(ctx.rightExpression(1));

        // Gestion des Appels de Fonction dans les expressions
        if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        }

        return null;
    }

    //arithmeticExpression : LPAREN arithmeticExpression RPAREN
    //                     | MINUS arithmeticExpression
    //                     | arithmeticExpression (MULT | DIV) arithmeticExpression
    //                     | arithmeticExpression (PLUS | MINUS) arithmeticExpression
    //                     | leftExpression
    //                     | functionCall
    //                     | INT_VALUE ;
    @Override
    public String visitArithmeticExpression(EMJParser.ArithmeticExpressionContext ctx) {

        // INT_VALUE ;
        if (ctx.INT_VALUE() != null) return ctx.INT_VALUE().getText();

        // functionCall
        if (ctx.functionCall() != null) return visit(ctx.functionCall());

        // leftExpression
        if (ctx.leftExpression() != null) return visit(ctx.leftExpression());

        // LPAREN arithmeticExpression RPAREN
        if (ctx.LPAREN() != null && ctx.RPAREN() != null) {
            return "(" + visit(ctx.arithmeticExpression(0)) + ")";
        }

        // MINUS arithmeticExpression
        if (ctx.MINUS() != null && ctx.arithmeticExpression().size() == 1) {
            return "-" + visit(ctx.arithmeticExpression(0));
        }

        // arithmeticExpression (MULT | DIV) arithmeticExpression |
        // arithmeticExpression (PLUS | MINUS) arithmeticExpression
        if (ctx.arithmeticExpression().size() == 2) {
            String left = visit(ctx.arithmeticExpression(0));
            String right = visit(ctx.arithmeticExpression(1));

            if (ctx.PLUS() != null) return left + " + " + right;
            if (ctx.MINUS() != null) return left + " - " + right;
            if (ctx.MULT() != null) return left + " * " + right;
            if (ctx.DIV() != null) return left + " // " + right;
        }

        return null;
    }

    // =========================================================================
    // LOOPS AND CONDITIONS
    // =========================================================================

    // conditionInstruction : IF LPAREN rightExpression RPAREN LBRACE instruction* RBRACE elseInstruction ;
    @Override
    public String visitConditionInstruction(EMJParser.ConditionInstructionContext ctx) {

        String condition = visit(ctx.rightExpression());
        codeGenerator.addLine("if " + condition + ":");
        codeGenerator.increaseIndentation();

        if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
            for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                visit(instrCtx);
            }
        } else {
            codeGenerator.addLine("pass");
        }

        codeGenerator.decreaseIndentation();

        visit(ctx.elseInstruction());

        return null;
    }

    // elseInstruction : ELSE LBRACE (instruction* | SKIP_INSTR SEMICOLON) RBRACE ;
    @Override
    public String visitElseInstruction(EMJParser.ElseInstructionContext ctx) {

        codeGenerator.addLine("else:");
        codeGenerator.increaseIndentation();

        if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
            for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                visit(instrCtx);
            }
        } else {
            codeGenerator.addLine("pass");
        }

        codeGenerator.decreaseIndentation();

        return null;
    }

    // loopInstruction : WHILE LPAREN rightExpression RPAREN LBRACE instruction* RBRACE
    //                | FOR LPAREN rightExpression RPAREN LBRACE instruction* RBRACE ;
    @Override
    public String visitLoopInstruction(EMJParser.LoopInstructionContext ctx) {

        if (ctx.WHILE() != null) {
            String condition = visit(ctx.rightExpression());
            codeGenerator.addLine("while " + condition + ":");
            codeGenerator.increaseIndentation();
            if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
                for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                    visit(instrCtx);
                }
            } else {
                codeGenerator.addLine("pass");
            }
            codeGenerator.decreaseIndentation();
        }

        if (ctx.FOR() != null) {
            String iterations = visit(ctx.rightExpression());
            codeGenerator.addLine("for i in range(" + iterations + "):");
            codeGenerator.increaseIndentation();
            if (ctx.instruction() != null && !ctx.instruction().isEmpty()) {
                for (EMJParser.InstructionContext instrCtx : ctx.instruction()) {
                    visit(instrCtx);
                }
            } else {
                codeGenerator.addLine("pass");
            }
            codeGenerator.decreaseIndentation();
        }
        return null;
    }

    // =========================================================================
    // PREDEFINED ACTIONS
    // =========================================================================

    //predefinedAction : ACT_ARREST LPAREN RPAREN
    //                 | ACT_SOUND LPAREN RPAREN
    //                 | ACT_LIGHTS LPAREN RPAREN
    //                 | ACT_UP LPAREN arithmeticExpression RPAREN
    //                 | ACT_DOWN LPAREN arithmeticExpression RPAREN
    //                 | ACT_RIGHT LPAREN arithmeticExpression RPAREN
    //                 | ACT_LEFT LPAREN arithmeticExpression RPAREN
    //                 | ACT_CRAZY LPAREN arithmeticExpression RPAREN ;

    // Utilisation de la bibliothèque custom cutebot qui gère le code directement en python

    @Override
    public String visitPredefinedAction(EMJParser.PredefinedActionContext ctx) {

        // Actions sans paramètres : ✋(), 📻(), 🚨()
        if (ctx.ACT_ARREST() != null) {
            codeGenerator.addLine("arrest()");
        }

        if (ctx.ACT_SOUND() != null) {
            codeGenerator.addLine("toggle_siren()");
        }

        if (ctx.ACT_LIGHTS() != null) {
            codeGenerator.addLine("toggle_lights()");
        }

        // Actions avec paramètre expression arithmétique : ⬆️(X), ⬇️(X), ➡️(X), ⬅️(X)
        if (ctx.ACT_UP() != null) {
            String x = visit(ctx.arithmeticExpression());
            codeGenerator.addLine("move('N', " + x + ")");
        }
        else if (ctx.ACT_DOWN() != null) {
            String x = visit(ctx.arithmeticExpression());
            codeGenerator.addLine("move('S', " + x + ")");
        }
        else if (ctx.ACT_RIGHT() != null) {
            String x = visit(ctx.arithmeticExpression());
            codeGenerator.addLine("move('E', " + x + ")");
        }
        else if (ctx.ACT_LEFT() != null) {
            String x = visit(ctx.arithmeticExpression());
            codeGenerator.addLine("move('W', " + x + ")");
        }

        // Mode crazy : 🃏(X) — paramètre doit être entre 1 et 5
        if (ctx.ACT_CRAZY() != null) {
            String x = visit(ctx.arithmeticExpression());
            codeGenerator.addLine("crazy_mode(" + x + ")");
        }

        return null;
    }

    // =========================================================================
    // MÉTHODES UTILITAIRES
    // =========================================================================

    /**
     * Get the generated MicroPython code
     * @return The generated code of codeGenerator in String format
     */
    public String getGeneratedPythonCode() {
        return this.codeGenerator.getCode();
    }

    /**
     * Convert an emoji variable to a MicroPython variable
     * @param emoji The emoji variable to be converted
     * @requires emoji != null
     * @return A valid conversion of emoji in MicroPython, in String format
     */
    private String emojiToPython(String emoji) {

        emoji = emoji.replace("[", "");
        emoji = emoji.replace("]", "");

        StringBuilder pythonVariable = new StringBuilder("var");

        // aidé par l'IA sur cette partie
        emoji.codePoints().forEach(codePoint -> {
            pythonVariable.append("_").append(Integer.toHexString(codePoint).toUpperCase());
        });

        return pythonVariable.toString();
    }

    // Lecture et écriture de la libraire externe cutebotNavigation dans le fichier .py final

    /**
     * Add the current orientation of the cutebot and the library cutebot_custom_librairy.py to the generated code
     * @param initialOrientation The current orientation of the cutebot
     * @requires initialOrientation != null
     * @modifies codeGenerator
     * @effects The following content is added to codeGenerator:
     *          - an additional line giving the current orientation of the cutebot
     *          - a blank line
     *          - the contents of the library cutebot_custom_librairy.py
     */
    private void addCutebotCustomLibrairy(String initialOrientation) {
        codeGenerator.addLine("current_orientation = '" + initialOrientation + "'");
        codeGenerator.addLine("");

        String libraryPath = "src/main/resources/librairies/cutebot_custom_librairy.py";

        try {
            List<String> pythonCodeLines = Files.readAllLines(Paths.get(libraryPath));
            for (String line : pythonCodeLines) {
                codeGenerator.addLine(line);
            }
            codeGenerator.addLine("");
        } catch (IOException e) {
            System.err.println("Problème d'import de la librairie custom " + libraryPath);
        }
    }

}
parser grammar EMJParser;

options { tokenVocab = EMJLexer; }

// ==============================================================================
// ROOT
// ==============================================================================

/*
    - root : The Roots of Grammar
        - map : The map part of the grammar
        - program : The program part of the grammar
        - EOF : End of file, to ensure that the entire input is consumed
*/
root : (map | program) EOF;

// ==============================================================================
// MAP
// ==============================================================================

/*
    - mapHeader : The definition of a map always begins with its instruction.
    - mapElement : The elements of the map (one or more instances)
*/
map : mapHeader mapElement+;

/*
   The first instruction in the map file is: 🗺️ with L,C,Z;
    - L: Number of rows in the map
    - C: Number of columns in the map
    - Z: Cutebot initial direction
*/
mapHeader : MAP_START WITH INT_VALUE COMMA INT_VALUE COMMA direction SEMICOLON;

/*
    - direction : Set of cardinal directions for the Cutebot initial state
*/
direction : ACT_UP | ACT_DOWN | ACT_RIGHT | ACT_LEFT;

/*
    - mapElement : Set of terminal symbols representing the map
*/ 
mapElement : MAP_POLICE 
           | MAP_ROAD 
           | MAP_VOLCANO 
           | MAP_HOUSE 
           | MAP_CONSTRUCTION 
           | MAP_TRACTOR 
           | MAP_WATER 
           | MAP_THIEF ;

// ==============================================================================
// PROGRAM
// ==============================================================================

/*
    - program : The root of the program part of the grammar
        - importDeclaration : The instruction for importing a map file only one
        - mainFunction : The main function of the program, where the execution starts, mandatory and only one
        - functionDeclaration : The declaration of a user-defined function (with or without return value)
*/
program : importDeclaration mainFunction functionDeclaration*;

/*
   - importDeclaration : The instructions for importing a map : 📦 "name.map"
*/
importDeclaration : IMPORT STRING_VALUE;

/*
   - mainFunction : The main function of the program : 🏠 () { ... 🌀;}
*/
mainFunction : VOID MAIN LPAREN RPAREN LBRACE instruction* RETURN VOID SEMICOLON RBRACE;

/*
    - functionParameters : The parameters of a function
        - A function can have zero or more parameters, each parameter is defined by its type and its name
*/
functionParameters : (typeDeclaration EMOJI_ID (COMMA typeDeclaration EMOJI_ID)*)? ;

/*
    - functionCall : The call to a user-defined function
        - A function call is defined by the function name followed by parentheses containing zero or more arguments, each argument is a rightExpression
*/
functionCall : EMOJI_ID LPAREN (rightExpression (COMMA rightExpression)*)? RPAREN ;

// ==============================================================================
// DECLARATIONS
// ==============================================================================

/* 
    - typeDeclaration : Set of terminal symbols for variable, function, and function parameter declarations
*/
typeDeclaration : TYPE_INT
                | TYPE_CHAR
                | TYPE_STRING
                | TYPE_BOOL
                | TYPE_TUPLE LPAREN typeDeclaration RPAREN ;

/*
    - variableDeclaration : The declaration of a variable, with or without initialization
*/
variableDeclaration : typeDeclaration EMOJI_ID (ASSIGN rightExpression)? ;

/*
    - functionDeclaration : The declaration of a user-defined function
        - A function can have a return type (int, char, string, bool, tuple) or be void
        - A function may have several parameters or none at all, this is handled directly in the functionParameters
        - A function contains zero or more instruction
*/
functionDeclaration : typeDeclaration EMOJI_ID LPAREN functionParameters RPAREN LBRACE instruction* RETURN rightExpression SEMICOLON RBRACE
                    | VOID EMOJI_ID LPAREN functionParameters RPAREN LBRACE instruction* RETURN VOID SEMICOLON RBRACE;

// ==============================================================================
// INSTRUCTIONS
// ==============================================================================

/*
    - assignInstruction : The assignment of a value to a variable
*/
assignInstruction : leftExpression ASSIGN rightExpression ;

/*
    - conditionInstruction : The conditional instruction, with an optional else branch
        - The condition is defined by a rightExpression
        - The if branch and the else branch (if it exists) can contain zero or more instructions
*/
conditionInstruction : IF LPAREN rightExpression RPAREN LBRACE instruction* RBRACE elseInstruction ;

/*
    - elseInstruction : The else branch of a conditional instruction
*/
elseInstruction : ELSE LBRACE (instruction* | SKIP_INSTR SEMICOLON) RBRACE ;

/*
    - loopInstruction : The loop instruction, with a condition
        - The loop is defined by a rightExpression
        - The loop body can contain zero or more instructions
*/
loopInstruction : WHILE LPAREN rightExpression RPAREN LBRACE instruction* RBRACE
                | FOR LPAREN rightExpression RPAREN LBRACE instruction* RBRACE ;

/*
    - instruction : A single instruction in the program
        - variableDeclaration : The declaration of a variable
        - assignInstruction : The assignment of a value to a variable
        - functionCall : The call to a user-defined function
        - predefinedAction : The call to a predefined action
        - conditionInstruction : The conditional instruction
        - loopInstruction : The loop instruction
*/
instruction : variableDeclaration SEMICOLON
            | assignInstruction SEMICOLON
            | functionCall SEMICOLON
            | predefinedAction SEMICOLON
            | conditionInstruction
            | loopInstruction ;

// ==============================================================================
// EXPRESSIONS
// ==============================================================================

/*
    - leftExpression : Set of terminal symbols representing the left-hand side of an assignment instruction
        - A left expression can be a variable access or an access to a tuple element (index 0 or index 1)
*/
leftExpression : EMOJI_ID
               | EMOJI_ID INDEX_0
               | EMOJI_ID INDEX_1 ;

rightExpression : LPAREN rightExpression RPAREN                             // Parentheses
                | NOT rightExpression                                       // Negation
                | MINUS rightExpression                                     // Minus for negative numbers
                | rightExpression (MULT | DIV) rightExpression              // Multiplication and division 
                | rightExpression (PLUS | MINUS) rightExpression            // Addition and subtraction
                | rightExpression (LT | GT | LTE | GTE) rightExpression     // Strict and equivalent comparison
                | rightExpression (EQ | NEQ) rightExpression                // Equality and inequality 
                | rightExpression AND rightExpression                       // Logical AND comparison 
                | rightExpression OR rightExpression                        // Logical OR comparison
                | leftExpression                                            // Variable access
                | functionCall                                              // Function call
                | INT_VALUE                                                 // Integer
                | CHAR_VALUE                                                // Character
                | STRING_VALUE                                              // String
                | BOOL_TRUE                                                 // Boolean True
                | BOOL_FALSE                                                // Boolean False
                | LPAREN rightExpression COMMA rightExpression RPAREN ;     // Tuple

/*
    - arithmeticExpression : A mathematical expression
*/
arithmeticExpression : LPAREN arithmeticExpression RPAREN
                     | MINUS arithmeticExpression
                     | arithmeticExpression (MULT | DIV) arithmeticExpression
                     | arithmeticExpression (PLUS | MINUS) arithmeticExpression
                     | leftExpression
                     | functionCall
                     | INT_VALUE ;

// ==============================================================================
// PREDEFINED ACTIONS
// ==============================================================================

/* 
    - predefinedAction : The call to a predefined action
        - Stop a thief : ✋()
        - Turn Cutebot's sound on/off : 📻()
        - Turn the Cutebot's lights on/off : 🚨()
        - Move Cutebot up : ⬆️(x)
        - Move Cutebot down : ⬇️(x)
        - Move Cutebot to the right : ➡️(x)
        - Move Cutebot to the left : ⬅️(x)
        - Enable Crazy Mode : 🃏(x)
*/
predefinedAction : ACT_ARREST LPAREN RPAREN
                 | ACT_SOUND LPAREN RPAREN
                 | ACT_LIGHTS LPAREN RPAREN
                 | ACT_UP LPAREN arithmeticExpression RPAREN
                 | ACT_DOWN LPAREN arithmeticExpression RPAREN
                 | ACT_RIGHT LPAREN arithmeticExpression RPAREN
                 | ACT_LEFT LPAREN arithmeticExpression RPAREN
                 | ACT_CRAZY LPAREN arithmeticExpression RPAREN ;
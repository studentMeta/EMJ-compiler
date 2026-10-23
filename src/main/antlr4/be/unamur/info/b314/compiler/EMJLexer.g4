lexer grammar EMJLexer;

// ==============================================================================
// WHITESPACE & COMMENTS
// ==============================================================================

// Skip ignores WHITESPACE
WHITESPACE: (' ' | '\t' | '\r'? '\n')+ -> skip;

// Comment line (📢)
COMMENT_LINE : '\u{1F4E2}' ~[\r\n]* -> skip ;

// Comment block (🔊 ... 🔈)
COMMENT_BLOCK : '\u{1F50A}' .*? '\u{1F508}' -> skip ;

// ==============================================================================
// TYPES
// ==============================================================================

// Integer Type (🔢)
TYPE_INT    : '\u{1F522}';

// Character Type (🔣)
TYPE_CHAR   : '\u{1F523}';

// String Type (🔡)
TYPE_STRING : '\u{1F521}';

// Boolean Type (🔟)
TYPE_BOOL   : '\u{1F51F}';

// Tuple Type (👥)
TYPE_TUPLE  : '\u{1F465}';

// Void Type (🌀)
VOID        : '\u{1F300}';

// ==============================================================================
// TUPLE INDEX
// ==============================================================================

// Tuple Index 0 (0️⃣) 
INDEX_0     : '\u0030\uFE0F\u20E3';

// Tuple Index 1 (1️⃣)
INDEX_1     : '\u0031\uFE0F\u20E3';

// ==============================================================================
// BOOLEAN VALUES
// ==============================================================================

// True (✅)
BOOL_TRUE   : '\u2705';

// False (❌)
BOOL_FALSE  : '\u274C';

// ==============================================================================
// CONDITIONALS
// ==============================================================================

// IF Keyword (🤔)
IF          : '\u{1F914}';

// ELSE Keyword (🤨)
ELSE        : '\u{1F928}';

/*
    skip instruction (👇)
        - Used to indicate that the else branch of a conditional statement contains no statements
*/ 
SKIP_INSTR  : '\u{1F447}';

// ==============================================================================
// LOOPS
// ==============================================================================

// WHILE Keyword (♾️)
WHILE       : '\u267E\uFE0F';

// FOR Keyword (🔁)
FOR         : '\u{1F501}';

// ==============================================================================
// STRUCTURES
// ==============================================================================

// RETURN Keyword (↩️)
RETURN      : '\u21A9\uFE0F';

// MAIN Keyword (🏠)
MAIN        : '\u{1F3E0}';

// IMPORT Keyword (📦)
IMPORT      : '\u{1F4E6}';

// ==============================================================================
// OPERATORS & PUNCTUATION
// ==============================================================================

// Logical Operators

// AND Operator (🖇️) 
AND         : '\u{1F587}\uFE0F';

// OR Operator (📎) 
OR          : '\u{1F4CE}';

// NOT Operator (⛔))
NOT         : '\u26D4';

// Comparators
LTE         : '<=';
GTE         : '>=';
EQ          : '==';
NEQ         : '!=';
LT          : '<';
GT          : '>';

// Math & Assignment
ASSIGN      : '=';
PLUS        : '+';
MINUS       : '-';
MULT        : '*';
DIV         : '/';

// Punctuation
SEMICOLON   : ';';
COMMA       : ',';
LPAREN      : '(';
RPAREN      : ')';
LBRACE      : '{';
RBRACE      : '}';
LBRACKET    : '[';
RBRACKET    : ']';

// ==============================================================================
// VALUES & IDENTIFIERS
// ==============================================================================

// Definition of a digit
fragment DIGIT         : '0'..'9';

// Definition of a nonzero digit
fragment NONZERO_DIGIT : '1'..'9';

// Definition of a character content
fragment CHAR_CONTENT  : ('a'..'z' | 'A'..'Z' | '0'..'9' | ' ' | '.');

/*
    Integers

    - Reminder
    An integer containing exactly one 0 cannot be preceded by a minus sign (-) and cannot be followed by any other digits
 */ 
INT_VALUE    : (MINUS? NONZERO_DIGIT DIGIT* | '0');

// Character
CHAR_VALUE   : '\'' CHAR_CONTENT '\'';

// Strings
STRING_VALUE : '"' CHAR_CONTENT* '"';

/*
    Definition of an emoji
        - U+FE0F is the Unicode hexadecimal value of the Variant Selector-16 character
        - U+200D is the Unicode hexadecimal value of the Non-breaking Hyphen character
*/ 
fragment EMOJI : [\p{Emoji}\uFE0F\u200D];

//Variable names
EMOJI_ID       : LBRACKET EMOJI+ RBRACKET;

// ==============================================================================
// PREDEFINED ACTIONS 
// ==============================================================================

// Stop a thief (✋)
ACT_ARREST : '\u270B';

// Turn Cutebot's sound on/off (📻)
ACT_SOUND  : '\u{1F4FB}';

// Turn the Cutebot's lights on/off (🚨)
ACT_LIGHTS : '\u{1F6A8}';

// Move Cutebot up (⬆️)
ACT_UP     : '\u2B06\uFE0F';

// Move Cutebot down (⬇️)
ACT_DOWN   : '\u2B07\uFE0F';

// Move Cutebot to the right (➡️)
ACT_RIGHT  : '\u27A1\uFE0F';

// Move Cutebot to the left (⬅️)
ACT_LEFT   : '\u2B05\uFE0F';

// Enable Crazy Mode (🃏)
ACT_CRAZY  : '\u{1F0CF}';

// ==============================================================================
// MAP ELEMENTS
// ==============================================================================

/*
    The Police (🚔)
        - Case originally occupied by the Cutebot
*/
MAP_POLICE       : '\u{1F694}';

/* 
    The Road (🛣️)
        - Cases that can naturally be taken on by the Cutebot 
*/
MAP_ROAD         : '\u{1F6E3}\uFE0F';

/*
    Unsurpassable obstacles, the Cutebot can never move to these Cases
        - Volcano (🌋)
        - House (🏘️)
        - Construction (🚧)
        - Tractor (🚜)
        - Water-Wave (🌊)
*/
MAP_VOLCANO      : '\u{1F30B}';
MAP_HOUSE        : '\u{1F3D8}\uFE0F';
MAP_CONSTRUCTION : '\u{1F6A7}';
MAP_TRACTOR      : '\u{1F69C}';
MAP_WATER        : '\u{1F30A}';

/*
    The Thief(s) (🦹) 
        - The idea is to program the Cutebot's movements so that it can stop thieves
*/
MAP_THIEF        : '\u{1F9B9}';

/* 
    The Start (🗺)
        - First instruction in the map file
*/
MAP_START        : '\u{1F5FA}' '\uFE0F'?;

// Keyword for map information 
WITH             : 'with';



/*
 ==============================================================================
 FREE TO USE EMOJIS

 Not-reserved emojis that are useable for the unit testing.
 Reminder : emojis have to be betwen brackets [], eg : [🐶]
 ==============================================================================

🐶   🐱   🐼   🐢   🦄   🐸
🍔   🍎   🍕   ☕   🍣   🍺
💻   📱   ⏰   💡   🔑
☀️   🌙   ⚡   🌲   ⭐
🚀   🚁   🚲   🚂
😀   😎   🤯   👻

*/
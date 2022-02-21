/* Boogie 2 lexer */
package scanner;

%%

%class Lexer
%unicode
%line
%column

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

LineTerminator = \r|\n|\r\n;
InputCharacter = [^\r\n];
WhiteSpace = {LineTerminator} | [ \t\f];
Comment = {TraditionalComment} | {EndOfLineComment};
TraditionalComment = "/*" ~"*/";
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?;
BoogieLetter = [:letter:] | ['~#$\^_.?\\];
BoogieLetterDigit = {BoogieLetter} | [:digit:];
Identifier = {BoogieLetter} {BoogieLetterDigit}*;
DecIntegerLiteral = 0 | [1-9][0-9]*;
RealIntegerLiteral = {DecIntegerLiteral} "." [0-9]+;
BvIntegerLiteral = {DecIntegerLiteral} "bv" {DecIntegerLiteral};
BvType = "bv" {DecIntegerLiteral};

%state STRING

%%

<YYINITIAL>  {
    "type" { return symbol(Terminals.TYPE); }
    "const" { return symbol(Terminals.CONST); }
    "function" { return symbol(Terminals.FUNCTION); }
    "axiom" { return symbol(Terminals.AXIOM); }
    "var" { return symbol(Terminals.VAR); }
    "procedure" { return symbol(Terminals.PROCEDURE); }
    "implementation" { return symbol(Terminals.IMPLEMENTATION); }

    "async" { return symbol(Terminals.ASYNC); }
    "finite" { return symbol(Terminals.FINITE); }
    "unique" { return symbol(Terminals.UNIQUE); }
    "complete" { return symbol(Terminals.COMPLETE); }
    "returns" { return symbol(Terminals.RETURNS); }
    "where" { return symbol(Terminals.WHERE); }
    "free" { return symbol(Terminals.FREE); }
    "ensures" { return symbol(Terminals.ENSURES); }
    "requires" { return symbol(Terminals.REQUIRES); }
    "modifies" { return symbol(Terminals.MODIFIES); }
    "invariant" { return symbol(Terminals.INVARIANT); }

    "par" { return symbol(Terminals.PAR); }
    "assume" { return symbol(Terminals.ASSUME); }
    "assert" { return symbol(Terminals.ASSERT); }
    "havoc" { return symbol(Terminals.HAVOC); }
    "call" { return symbol(Terminals.CALL); }
    "if" { return symbol(Terminals.IF); }
    "then" { return symbol(Terminals.THEN); }
    "else" { return symbol(Terminals.ELSE); }
    "while" { return symbol(Terminals.WHILE); }
    "break" { return symbol(Terminals.BREAK); }
    "return" { return symbol(Terminals.RETURN); }
    "goto" { return symbol(Terminals.GOTO); }
    "yield" { return symbol(Terminals.YIELD); }

    "old" { return symbol(Terminals.OLD); }

    "forall" { return symbol(Terminals.FORALL); }
    "\u2200" { return symbol(Terminals.FORALL); }
    "exists" { return symbol(Terminals.EXISTS); }
    "\u2203" { return symbol(Terminals.EXISTS); }
    "bool" { return symbol(Terminals.BOOL); }
    "int" { return symbol(Terminals.INT); }
    "real" { return symbol(Terminals.REAL); }
    "false" { return symbol(Terminals.FALSE); }
    "true" { return symbol(Terminals.TRUE); }

    /* Other Symbols */
    "(" { return symbol(Terminals.LPAR); }
    ")" { return symbol(Terminals.RPAR); }
    "[" { return symbol(Terminals.LBKT); }
    "]" { return symbol(Terminals.RBKT); }
    "{" { return symbol(Terminals.LBRC); }
    "}" { return symbol(Terminals.RBRC); }
    "|" { return symbol(Terminals.VBAR); }
    "," { return symbol(Terminals.COMMA); }
    ":" { return symbol(Terminals.COLON); }
    ";" { return symbol(Terminals.SEMI); }
    ":=" { return symbol(Terminals.COLONEQUALS); }
    "\u2254" { return symbol(Terminals.COLONEQUALS); }
    "=" { return symbol(Terminals.EQUALS); }
    "<" { return symbol(Terminals.LESS); }
    ">" { return symbol(Terminals.GREATER); }
    "<=" { return symbol(Terminals.LTEQ); }
    "\u2264" { return symbol(Terminals.LTEQ); }
    ">=" { return symbol(Terminals.GTEQ); }
    "\u2265" { return symbol(Terminals.GTEQ); }
    "!=" { return symbol(Terminals.NEQ); }
    "\u2260" { return symbol(Terminals.NEQ); }
    "==" { return symbol(Terminals.EQ); }
    "<:" { return symbol(Terminals.PARTORDER); }
    "extends" { return symbol(Terminals.EXTENDS); }
    "+" { return symbol(Terminals.PLUS); }
    "-" { return symbol(Terminals.MINUS); }
    "*" { return symbol(Terminals.TIMES); }
    "/" { return symbol(Terminals.DIVIDE); }
    "div" { return symbol(Terminals.DIVIDE); }
    "%" { return symbol(Terminals.MOD); }
    "mod" { return symbol(Terminals.MOD); }
    "!" { return symbol(Terminals.NOT); }
    "\u00ac" { return symbol(Terminals.NOT); }
    "&&" { return symbol(Terminals.AND); }
    "\u2227" { return symbol(Terminals.AND); }
  
    "||" { return symbol(Terminals.OR); }
    "\u2228" { return symbol(Terminals.OR); }
    "==>" { return symbol(Terminals.IMPLIES); }
    "\u21d2" { return symbol(Terminals.IMPLIES); }
    "<==" { return symbol(Terminals.EXPLIES); }
    "\u21d0" { return symbol(Terminals.EXPLIES); }
    "<==>" { return symbol(Terminals.IFF); }
    "\u21d4" { return symbol(Terminals.IFF); }
    "::" { return symbol(Terminals.QSEP); }
    "\u2022" { return symbol(Terminals.QSEP); }
    "++" { return symbol(Terminals.CONCAT); }

    /* Numbers, Ids and Strings */
    {BvType} { return symbol(Terminals.BVTYPE, yytext().intern()); }

    /* identifiers */ 
    {Identifier} { return symbol(Terminals.ID, yytext().intern()); }
 
    /* literals */
    {BvIntegerLiteral} { return symbol(Terminals.BITVECTOR, yytext().intern()); }
    {DecIntegerLiteral} { return symbol(Terminals.NUMBER, yytext().intern()); }
    {RealIntegerLiteral} { return symbol(Terminals.REALNUMBER, yytext().intern()); }
    \" { string.setLength(0); yybegin(STRING); }

    /* comments */
    {Comment} { /* ignore */ }
 
    /* whitespace */
    {WhiteSpace} { /* ignore */ }
}

<STRING> {
    \"                             {
        yybegin(YYINITIAL);
        return symbol(Terminals.ATTR_STRING, string.toString().intern());
    }
    [^\n\r\"\\]+ { string.append( yytext() ); }
    \\t { string.append('\t'); }
    \\n { string.append('\n'); }

    \\r { string.append('\r'); }
    \\\" { string.append('\"'); }
    \\ { string.append('\\'); }
}

/* error fallback */
[^]|\n { return symbol(Terminals.error, yytext()); }
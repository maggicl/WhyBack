/* Boogie 2 lexer
 * Taken from martinschaef/boogieamp
 * https://github.com/martinschaef/boogieamp/blob/master/boogieamp/src/parser/Boogie.flex
 */

package byteback.frontend.boogie.scanner;
import byteback.frontend.boogie.parser.BoogieParser.Terminals;
import beaver.Symbol;
import beaver.Scanner;

/**
 * This is a autogenerated lexer for Boogie 2.
 * It is generated from Boogie.flex by JFlex.
 */

%%

%public
%final
%class BoogieLexer
%extends Scanner
%unicode
%function nextToken
%type Symbol
%yylexthrow Scanner.Exception
%line
%column
%char

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(short id) {
        return new Symbol(id, yyline + 1, yycolumn + 1, yylength());
    }

    private Symbol symbol(short id, String value) {
        return new Symbol(id, yyline + 1, yycolumn + 1, yylength(), value);
    }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment   = "/*" ~"*/" 
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
BoogieLetter = [:letter:] | ['~#$\^_.?\\]
BoogieLetterDigit = {BoogieLetter} | [:digit:]
Identifier = {BoogieLetter} {BoogieLetterDigit}*

DecIntegerLiteral = 0 | [1-9][0-9]*
RealIntegerLiteral = {DecIntegerLiteral} "." [0-9]+
BvIntegerLiteral = {DecIntegerLiteral} "bv" {DecIntegerLiteral}
BvType = "bv" {DecIntegerLiteral}

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

    "(" { return symbol(Terminals.LPAREN); }
    ")" { return symbol(Terminals.RPAREN); }
    "[" { return symbol(Terminals.LBRACKET); }
    "]" { return symbol(Terminals.RBRACKET); }
    "{" { return symbol(Terminals.LBRACE); }
    "}" { return symbol(Terminals.RBRACE); }
    "," { return symbol(Terminals.COMMA); }
    ":" { return symbol(Terminals.COLON); }
    ";" { return symbol(Terminals.SEMICOLON); }
    ":=" { return symbol(Terminals.COLONEQ); }
    "\u2254" { return symbol(Terminals.COLONEQ); }
    "=" { return symbol(Terminals.EQUALS); }
    "<" { return symbol(Terminals.LT); }
    ">" { return symbol(Terminals.GT); }
    "<=" { return symbol(Terminals.LTEQ); }
    "\u2264" { return symbol(Terminals.LTEQ); }
    ">=" { return symbol(Terminals.GTEQ); }
    "\u2265" { return symbol(Terminals.GTEQ); }
    "!=" { return symbol(Terminals.NEQ); }
    "\u2260" { return symbol(Terminals.NEQ); }
    "==" { return symbol(Terminals.EQUALS); }
    "<:" { return symbol(Terminals.PARTORDER); }
    "+" { return symbol(Terminals.PLUS); }
    "-" { return symbol(Terminals.MINUS); }
    "*" { return symbol(Terminals.TIMES); }
    "/" { return symbol(Terminals.DIVIDE); }
    "div" { return symbol(Terminals.DIVIDE); }
    "mod" { return symbol(Terminals.MOD); }
    "!" { return symbol(Terminals.NOT); }
    "\u00ac" { return symbol(Terminals.NOT); }
    "&&" { return symbol(Terminals.AND); }
    "\u2227" { return symbol(Terminals.AND); }
  
    "||" { return symbol(Terminals.OR); }
    "\u2228" { return symbol(Terminals.OR); }
    "==>" { return symbol(Terminals.IMPLIES); }
    "\u21d2" { return symbol(Terminals.IMPLIES); }
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
    {Comment} { }

    /* whitespace */
    {WhiteSpace} { }
}

<STRING> {
    \" {
        yybegin(YYINITIAL);
        // return symbol(Terminals.ATTR_STRING, string.toString().intern());
    }
    [^\n\r\"\\]+ { string.append( yytext() ); }
    \\t { string.append('\t'); }
    \\n { string.append('\n'); }

    \\r { string.append('\r'); }
    \\\" { string.append('\"'); }
    \\ { string.append('\\'); }
}

/* error fallback */
[^]|\n { throw new Scanner.Exception(yyline + 1, yycolumn + 1, "illegal character \"" + yytext() + "\""); }

<<EOF>> { return symbol(Terminals.EOF); }

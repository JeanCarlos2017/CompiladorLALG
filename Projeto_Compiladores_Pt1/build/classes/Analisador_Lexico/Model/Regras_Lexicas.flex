package Analisador_Lexico.Model;
import static Analisador_Lexico.Model.Token.*;
import java.util.ArrayList;
//import static Analisador_Lexico.Model.Simbolos;
%%

//%cup
%public
%char
%line
%column
%class Lexer
%type Simbolos


D = [0-9]
ID =[_|a-z|A-Z][a-z|A-Z|0-9]*

%{



private Simbolos add(Token token, String lexema) {
    
    Simbolos simbolo = new Simbolos (lexema, token, yyline, yycolumn, yycolumn + lexema.length() );
    simbolo.setDeslocamento(yychar);
    //Leitor_Fonte.simbolos.add(simbolo);
    return simbolo;
}

%}

O_COMMENT  = \{
C_COMMENT = \}
COMMENT_BODY   = [^}]
BODY_MULT_COMMENTS = {COMMENT_BODY}*
MULT_COMMENTS = {O_COMMENT}{BODY_MULT_COMMENTS}{C_COMMENT}

O_COMMENT_LINE = "\/\/"
COMMENT_LINE = {O_COMMENT_LINE}{LINE_NOT_CLOSE}*{COMMENT_BODY}{LINE_CLOSE}*
LINE_CLOSE = [\n]
LINE_NOT_CLOSE = [^\n]


%{
public String lexeme;
%}
%%


"\t" {lexeme=yytext(); return add(IGNORE, yytext());}
" " {lexeme=yytext(); return add(IGNORE, yytext());}
"\r" {lexeme=yytext();return add(IGNORE, yytext());}

"\n" {lexeme=yytext(); return add(QLINHA, yytext());}

":=" {lexeme=yytext(); return add(OPATRIBUICAO, yytext());}
"=" {lexeme=yytext(); return add(OPIGUALDADE, yytext());}
"+" {lexeme=yytext(); return add( OPSOMA, yytext());}
"*" {lexeme=yytext(); return add( OPMUL, yytext());}
"-" {lexeme=yytext(); return add( OPSUB, yytext());} 
"div" {lexeme=yytext(); return add( OPDIV, yytext());}
"<>" {lexeme=yytext(); return add( OPDIFERENCA, yytext());}
"<" {lexeme=yytext(); return add( OPMENOR, yytext());}
"<=" {lexeme=yytext(); return add( OPMENORIGUAL, yytext());}
">=" {lexeme=yytext(); return add( OPMAIORIGUAL, yytext());}
">" {lexeme=yytext(); return add( OPMAIOR, yytext());}
"(" {lexeme=yytext(); return add( AP, yytext());}
")" {lexeme=yytext(); return add( FP, yytext());}

"program" {lexeme=yytext(); return add( PROGRAM, yytext());} 
"procedure" {lexeme=yytext(); return add( PROCEDURE, yytext());}
"var" {lexeme=yytext(); return add( VAR, yytext());}
"int" {lexeme=yytext(); return add( INT, yytext());}
"boolean" {lexeme=yytext(); return add( BOOLEAN, yytext());}
"read" {lexeme=yytext(); return add(IDENTIFICADOR , yytext());}
"write" {lexeme=yytext(); return add( IDENTIFICADOR, yytext());}
"true" {lexeme=yytext(); return add( TRUE, yytext());}
"false" {lexeme=yytext(); return add( FALSE, yytext());}
"begin" {lexeme=yytext(); return add( BEGIN, yytext());}
"end" {lexeme=yytext(); return add( END, yytext());}
"end." {lexeme=yytext(); return add( ENDF, yytext());}
"if" {lexeme=yytext(); return add( IF, yytext());}
"then" {lexeme=yytext(); return add( THEN, yytext());}
"else" {lexeme=yytext(); return add( ELSE, yytext());}
"while"  {lexeme=yytext(); return add( WHILE, yytext());}
"do" {lexeme=yytext(); return add( DO, yytext());}
"or" {lexeme=yytext(); return add( OR, yytext());}
"and" {lexeme=yytext(); return add( AND, yytext());}
"not" {lexeme=yytext(); return add( NOT, yytext());}

":" {lexeme=yytext(); return add( doisPontos, yytext());}
";" {lexeme=yytext(); return add( pontoVirgula, yytext());}
"," {lexeme=yytext(); return add( virgula, yytext());}

{COMMENT_LINE} {lexeme=yytext(); return add( COMMENT_LINE, yytext());}
{MULT_COMMENTS} {lexeme=yytext(); return add( COMMENTS, yytext());}
{ID} {lexeme=yytext(); return add( IDENTIFICADOR, yytext());}
("."{D}+)| ({D}+"."{D}*) |{D}+ {lexeme=yytext(); return add( NUM, yytext());}

. {lexeme=yytext(); return add( ERROR, yytext());}
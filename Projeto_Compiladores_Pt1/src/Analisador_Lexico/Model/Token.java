

package Analisador_Lexico.Model;

public enum Token {
    //categorias "já tem procedure como uma palavra reservada"
    variavel,
    parametro,
    //tipo de erro 
     chamadaProc,
     variavelRedeclarada,
     tabelaNaoEncontrada,
     Aceito,
    //operadores 
    OPATRIBUICAO, 
    OPSOMA, 
    OPMUL,
    OPSUB,
    OPDIV,
    OPIGUALDADE, 
    OPDIFERENCA, 
    OPMENORIGUAL, 
    OPMAIOR, 
    OPMENOR, 
    OPMAIORIGUAL, 
    //terminais como pontuação 
    AP,
    FP, 
    pontoVirgula,
    virgula,
    doisPontos, 
    //tipo 
    TIPO,
    INT,
    BOOLEAN,
    //outros 
    NUM, ID, ERROR, IGNORE, QLINHA, COMMENTS, COMMENT_LINE,
    reservedWords,    IDENTIFICADOR, 
    EOF,
    //palavras reservadas
    PROGRAM,
    PROCEDURE,
    VAR,
    READ,
    TRUE,
    FALSE,
    BEGIN,
    END,
    ENDF, //EOF???
    IF,
    THEN,
    ELSE,
    WHILE,
    DO,
    OR,
    AND,
    NOT,
    WRITE,
    
    

    
}


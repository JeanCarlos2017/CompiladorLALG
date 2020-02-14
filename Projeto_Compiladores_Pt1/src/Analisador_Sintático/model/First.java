
package Analisador_Sintático.model;

import Analisador_Lexico.Model.Token;
import java.util.ArrayList;


public class First {
    private  ArrayList<Token> firstProgram = new ArrayList<Token>();
    private  ArrayList<Token> firstBloco = new ArrayList<Token>();
    private  ArrayList<Token> firstIdentLoop = new ArrayList<Token>();
    private  ArrayList<Token> firstSecParamForm = new ArrayList<Token>();
    private  ArrayList<Token> firstComando = new ArrayList<Token>();
    private  ArrayList<Token> firstCmdLoop = new ArrayList<Token>();
    private  ArrayList<Token> firstAtribuicao = new ArrayList<Token>();
    private  ArrayList<Token> firstExpressao = new ArrayList<Token>();
    private ArrayList<Token> palavrasReservadas = new ArrayList<>();
    private ArrayList<Token> operadores = new ArrayList<>();
    
    public First(){
        this.iniciaFirst();
    }
    
    private void iniciaFirst(){
        //firstBloco
        firstBloco.add(Token.INT);
        firstBloco.add(Token.BOOLEAN);
        firstBloco.add(Token.PROCEDURE);
        firstBloco.add(Token.BEGIN);
        
        firstIdentLoop.add(Token.virgula);
        //first secao Parametros
        firstSecParamForm.add(Token.AP);
        firstSecParamForm.add(Token.VAR);
        firstSecParamForm.add(Token.IDENTIFICADOR);
        firstSecParamForm.add(Token.doisPontos);
        
        //firstComando
        firstComando.add(Token.IDENTIFICADOR);
        firstComando.add(Token.BEGIN);
        firstComando.add(Token.IF);
        firstComando.add(Token.WHILE);
        
        firstCmdLoop.add(Token.pontoVirgula);
        
        firstAtribuicao.add(Token.OPATRIBUICAO);
        firstAtribuicao.add(Token.OPSOMA);
        firstAtribuicao.add(Token.OPSUB);
        firstAtribuicao.add(Token.IDENTIFICADOR);
        firstAtribuicao.add(Token.NUM);
        firstAtribuicao.add(Token.AP);
        firstAtribuicao.add(Token.NOT);
        
        firstExpressao.add(Token.OPSOMA);
        firstExpressao.add(Token.OPSUB);
        firstExpressao.add(Token.IDENTIFICADOR);
        firstExpressao.add(Token.NUM);
        firstExpressao.add(Token.AP);
        firstExpressao.add(Token.NOT);
        
        //para coloração do texto
        palavrasReservadas.add(Token.AND);
        palavrasReservadas.add(Token.BEGIN);
        palavrasReservadas.add(Token.BOOLEAN);
        palavrasReservadas.add(Token.DO);
        palavrasReservadas.add(Token.ELSE);
        palavrasReservadas.add(Token.END);
        palavrasReservadas.add(Token.ENDF);
        palavrasReservadas.add(Token.FALSE);
        palavrasReservadas.add(Token.virgula);
        palavrasReservadas.add(Token.pontoVirgula);
        palavrasReservadas.add(Token.IF);
        palavrasReservadas.add(Token.INT);
        palavrasReservadas.add(Token.NOT);
        palavrasReservadas.add(Token.OR);
        palavrasReservadas.add(Token.PROCEDURE);
        palavrasReservadas.add(Token.PROGRAM);
        palavrasReservadas.add(Token.READ);
        palavrasReservadas.add(Token.THEN);
        palavrasReservadas.add(Token.TRUE);
        palavrasReservadas.add(Token.VAR);
        palavrasReservadas.add(Token.WHILE);
        palavrasReservadas.add(Token.WRITE);
       
        
        operadores.add(Token.OPATRIBUICAO);
        operadores.add(Token.OPDIFERENCA);
        operadores.add(Token.OPDIV);
        operadores.add(Token.OPIGUALDADE);
        operadores.add(Token.OPMAIOR);
        operadores.add(Token.OPMAIORIGUAL);
        operadores.add(Token.OPMUL);
        operadores.add(Token.OPSUB);
        operadores.add(Token.OPSOMA);
        operadores.add(Token.OPMENORIGUAL);
        operadores.add(Token.AP);
        operadores.add(Token.FP);
        operadores.add(Token.doisPontos);
        operadores.add(Token.virgula);
        operadores.add(Token.pontoVirgula);
        
    }

    public  ArrayList<Token> getFirstProgram() {
        return firstProgram;
    }

    public  ArrayList<Token> getFirstBloco() {
        return firstBloco;
    }

    public  ArrayList<Token> getFirstIdentLoop() {
        return firstIdentLoop;
    }

    public  ArrayList<Token> getFirstComando() {
        return firstComando;
    }

    public  ArrayList<Token> getFirstExpressao() {
        return firstExpressao;
    }

    public ArrayList<Token> getFirstSecParamForm() {
        return firstSecParamForm;
    }

    public ArrayList<Token> getFirstAtribuicao() {
        return firstAtribuicao;
    }

    public ArrayList<Token> getPalavrasReservadas() {
        return palavrasReservadas;
    }
    
    
    public ArrayList<Token> getOperadores() {
        return operadores;
    }
    
    
    
}


package Analisador_Semantico.model;

import Analisador_Lexico.Model.Simbolos;
import Analisador_Lexico.Model.Token;
/*
    simbolos para as variáveis e procedimentos 
*/

public class SimboloSemantico{
    private String lexema;
    private Token categoria; //variável, parametro ou procedure 
    private Token tipo; //boolean ou inteiro 
    //há dois tipo de variáveis, então há dois tipos de valores
    private float valorNumerico;
    private boolean valorBoleano;
    private String escopo; //onde eu estou na análise
    private String nomeSimbolo;
    private boolean utilizado; //informa se a variável ja foi usada
    private int linha; //para impressao de simbolo nao usado
    
   
    public SimboloSemantico(String lexema, Token categoria, Token tipo, float valorNumerico, boolean valorBoleano, String escopo, String nomeSimbolo, int linha){ 
       this.lexema = lexema;
       this.categoria = categoria;
       this.tipo = tipo;
       this.valorNumerico = valorNumerico;
       this.valorBoleano = valorBoleano;
       this.escopo = escopo;
       this.nomeSimbolo = nomeSimbolo;
       this.utilizado = false;
       this.linha = linha;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getLinha() {
        return linha;
    }
    
    
    public Token getCategoria() {
        return categoria;
    }

    public void setCategoria(Token categoria) {
        this.categoria = categoria;
    }

    public Token getTipo() {
        return tipo;
    }

    public void setTipo(Token tipo) {
        this.tipo = tipo;
    }

    public float getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(float valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public boolean isValorBoleano() {
        return valorBoleano;
    }

    public void setValorBoleano(boolean valorBoleano) {
        this.valorBoleano = valorBoleano;
    }

    public String getEscopo() {
        return escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public String getNomeSimbolo() {
        return nomeSimbolo;
    }

    public void setNomeSimbolo(String nomeSimbolo) {
        this.nomeSimbolo = nomeSimbolo;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }
    
    
}

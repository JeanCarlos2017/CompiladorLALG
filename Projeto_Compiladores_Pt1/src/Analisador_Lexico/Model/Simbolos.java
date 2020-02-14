
package Analisador_Lexico.Model;

public class Simbolos {
    private String lexema;
    private Token token;
    private int linha;
    private int colunaInicial;
    private int colunaFinal;
    private int deslocamento;
    private boolean visitado;

    public Simbolos(String lexema, Token token, int linha, int colunaInicial, int colunaFinal) {
        this.lexema = lexema;
        this.token = token;
        this.linha = linha;
        this.colunaInicial = colunaInicial;
        this.colunaFinal = colunaFinal;
        this.visitado = false;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public int getDeslocamento() {
        return deslocamento;
    }
    
    
    public void setDeslocamento(int deslocamento) {
        this.deslocamento = deslocamento;
    }

    
    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColunaInicial() {
        return colunaInicial;
    }

    public void setColunaInicial(int colunaInicial) {
        this.colunaInicial = colunaInicial;
    }

    public int getColunaFinal() {
        return colunaFinal;
    }

    public void setColunaFinal(int colunaFinal) {
        this.colunaFinal = colunaFinal;
    }
    
    @Override
    public String toString(){
        String desc = "Lexema: "+this.lexema+"\n"+
                      "Token: "+this.token+"\n"+
                      "Linha: "+this.linha+"\n"+
                      "Coluna Incial: "+this.colunaInicial+"\n"+
                      "Coluna Final: "+this.colunaFinal+"\n";
        //System.out.println(desc);
        return desc;
    }
    
}

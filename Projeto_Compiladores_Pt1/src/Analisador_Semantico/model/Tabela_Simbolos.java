
package Analisador_Semantico.model;


import Analisador_Lexico.Model.Token;
import java.util.HashMap;
import java.util.Map;

public class Tabela_Simbolos {
     Map<String,SimboloSemantico> tabela;
     //chave é a concatenação da categoria mais o seu identificador, ex: procedureprogram

    public Tabela_Simbolos() {
        this.tabela = new HashMap<>();
    }

    public Map<String, SimboloSemantico> getTabela() {
        return tabela;
    }
    
    public void addSimbolo(String chave, SimboloSemantico s1){
        this.tabela.put(chave, s1);
    }
    
    public boolean existeSimbolo(String chave){
         return tabela.containsKey(chave);
    }
    
    public SimboloSemantico retornaSimbolo (String chave){
        if (tabela.containsKey(chave)){
            return tabela.get(chave);
        }else return null;
    }
    
    public boolean insereSimbolo(SimboloSemantico s1){
        String chave = String.valueOf(s1.getCategoria()) + s1.getLexema();
        if (!existeSimbolo(chave)){
            //ainda não existe esse simbolo na tabela
            tabela.put(chave, s1);
            return true;
        }else {
            return false;
        }
        
    }
    
    public boolean informaQueFoiUtilizado(String chave, String chaveEscopo){
        SimboloSemantico aux;
        if (chaveEscopo.contains(String.valueOf(Token.PROCEDURE))){
            //verificar se eu nao estou chamando um parametro
            String chaveParam = chave.replace(String.valueOf(Token.variavel), String.valueOf(Token.parametro));
             aux = this.retornaSimbolo(chaveParam);
            if (aux != null){
                aux.setUtilizado(true);
                return true;
            }
        }
        aux = this.retornaSimbolo(chave);
        if (aux != null){
            aux.setUtilizado(true);
            return true;
        }
        return false;
    }
    
}

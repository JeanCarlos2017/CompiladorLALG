/*
 armazena o nome da procedure e os tipos dos parametros esperados 
  ex: procedure proc(var a1, a4 : int; var a2, a3 : boolean);
      proc (int, int, boolean boolean)  
 */
package Analisador_Semantico.model;

import Analisador_Lexico.Model.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AssinaturaProcedure {
     private Map<String,Assinatura> tabela;
     private Assinatura ass;
    public AssinaturaProcedure() {
        tabela = new HashMap<>();
    }
     
     public class Assinatura{
            private String nome;
            private ArrayList<Token> parametros;

        public Assinatura(String nome) {
            this.nome = nome;
            this.parametros = new ArrayList<>();
        }

        public Assinatura(String nome, ArrayList<Token> parametros) {
            this.nome = nome;
            this.parametros = parametros;
        }
            
            
            public void addParam(Token tipo){
                this.parametros.add(tipo);
            }

        public ArrayList<Token> getParametros() {
            return parametros;
        }
            
            
    }

    public Assinatura getNovaAss(String nome) {
        return ass = new Assinatura(nome);
    }
    
     public void insereAssinatura (String chaveProcedure){
         //chaveProcedure = procProcecure
         Assinatura ass = new Assinatura(chaveProcedure);
         this.tabela.put(chaveProcedure, ass);
     }
     
     public boolean addParamAss( String chave, Token param){
         if (tabela.containsKey(chave)){
             tabela.get(chave).addParam(param);
             return true;
         }
         return false;
     }
     
     public ArrayList<Token> getParametrosProcedure (String chave){
         if (tabela.containsKey(chave)){
             return tabela.get(chave).getParametros();
         }else return null;
     }
     
     public boolean verificaChamadaProcedimento(String chave, ArrayList<Token> chamadaProc){
          //chamadaProc contém a quantidade de variáveis e os seus respectivos tipos em ordem
          //pego a ordem e os tipos dos parametros da procedure 
         ArrayList<Token> assProc = this.getParametrosProcedure(chave);
         int tamChamadaProc = chamadaProc.size();
         int tamAssProc = assProc.size();
         System.out.println("tamanho arrays: "+tamAssProc+ " "+tamChamadaProc+"\n");
         if (tamAssProc != tamChamadaProc){
             //tamanhos diferentes então não são iguais
             return false;
         }
         for (int i = 0; i < tamAssProc; i++){
             if (chamadaProc.get(i) != assProc.get(i))
                 return false;
         }
         return true;
        
     }
}

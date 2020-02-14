
package Analisador_Semantico.model;

import Analisador_Lexico.Model.Token;
import Analisador_Semantico.model.AssinaturaProcedure.Assinatura;
import View.IUAnalisadorSintatico1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
tabelasAnalisadorSemantico:
 uma tabela para a program, que contém todas as variáveis globais além das declarações de todas as procedures 
 uma tabela para cada procedure que vai conter as variáveis globais e seus parâmetros 
*/
public class AnalisadorSemantico {
    private  Map<String,Tabela_Simbolos> tabelasAnalisadorSemantico;
    private AssinaturaProcedure assProcedures;
    public AnalisadorSemantico() {
        this.tabelasAnalisadorSemantico = new HashMap<>();
        this.assProcedures = new AssinaturaProcedure();
        
    }

    public Map<String, Tabela_Simbolos> getTabelasAnalisadorSemantico() {
        return tabelasAnalisadorSemantico;
    }
    
    public String geraChave(String nome, Token t){
        //nome é o nome do program, procedure, variável ou parametro
        //t é a categoria
       return String.valueOf(t) + nome; 
    }
    
    public void addTabela(String chaveEscopo){
        Tabela_Simbolos tabela = new Tabela_Simbolos();
        this.tabelasAnalisadorSemantico.put(chaveEscopo, tabela);
        
    }
    
    public Tabela_Simbolos buscaTabela(String chave){
        //busca uma tabela, tanto a program como a de uma procedure qualquer 
        if (tabelasAnalisadorSemantico.containsKey(chave)) return tabelasAnalisadorSemantico.get(chave);
        else return null;
    }
    
    public SimboloSemantico buscaSimboloGlobal(String chaveSimbolo, String chaveProgram){
        //busca o simbolo na tabela program (global)
        Tabela_Simbolos global = this.buscaTabela(chaveProgram);
        if (global == null) return null;
        else return global.retornaSimbolo(chaveSimbolo);
    }
    
    public boolean existeSimboloGlobal(String chaveSimbolo, String chaveProgram){
        //busca o simbolo na tabela program (global)
        Tabela_Simbolos global = this.buscaTabela(chaveProgram);
        if (global == null) return false;
        else return global.existeSimbolo(chaveSimbolo);
    }
    
    public boolean  existeSimboloProcedure(String chaveProcedure, String chaveSimbolo){
        //busca o simbolo em uma procedure
        Tabela_Simbolos procedure = this.buscaTabela(chaveProcedure);
        if (procedure == null) return false;
        else return procedure.existeSimbolo(chaveSimbolo);
    }
    
    public SimboloSemantico buscaSimboloProcedure(String chaveProcedure, String chaveSimbolo){
        //busca o simbolo em uma procedure
        Tabela_Simbolos procedure = this.buscaTabela(chaveProcedure);
        if (procedure == null) return null;
        else return procedure.retornaSimbolo(chaveSimbolo);
    }
    
    public Token buscaSimboloEmTodasTabelas(String chaveProgram, String chaveProcedure, String nomeVariavel){
        String chaveSimbolo = this.geraChave(nomeVariavel, Token.variavel);
        String chaveSimboloParam = this.geraChave(nomeVariavel, Token.parametro);
        SimboloSemantico achouGlobal = this.buscaSimboloGlobal(chaveSimbolo, chaveProgram);
        SimboloSemantico achouProc = this.buscaSimboloProcedure(chaveProcedure, chaveSimbolo);
        SimboloSemantico achouParam = this.buscaSimboloProcedure(chaveProcedure, chaveSimboloParam);
        if ((achouProc != null) && (achouParam != null))
            return Token.ERROR; //não pode ter parametros e variáveis locais de uma procedure com o mesmo nome
        if (achouProc != null)
            return achouProc.getTipo(); //preferencia a variáveis locais de uma procedure
        if (achouParam != null)
            return achouParam.getTipo();
        if (achouGlobal != null)
            return achouGlobal.getTipo();
        return null; //não encontrou a variável em nenhuma tabela
        
    }
    
    public SimboloSemantico retornaSimbolo(String chaveProcedure, String chaveSimbolo){
        //busca o simbolo em uma procedure
        Tabela_Simbolos procedure = this.buscaTabela(chaveProcedure);
        if (procedure == null) return null;
        else return procedure.getTabela().get(chaveSimbolo);
    }
    
    public boolean alteraSimbolo(String chaveProcedure, String chaveProgram, String chaveSimbolo, SimboloSemantico v){
        //verifico se o simbolo existe, do contrário não altera e retorna falso 
        boolean achou = this.existeSimboloProcedure(chaveProcedure, chaveSimbolo); //busco na tabela da procedure
        if (achou){
            //se encontrou eu altero o simbolo na tabela da procedure 
             Tabela_Simbolos procedure = this.buscaTabela(chaveProcedure);
             procedure.getTabela().replace(chaveSimbolo, v);
             return true;
        }else {
            //não encontrou na tabela da procedure, então eu verifico na tabela de program
            achou = this.existeSimboloProcedure(chaveProgram, chaveSimbolo);
            if (achou){
                //encontrou na tabela de program entã faço a alteração lá
                Tabela_Simbolos program = this.buscaTabela(chaveProgram);
                program.getTabela().replace(chaveSimbolo, v);
                return true;
            }
        }
        return false; //simbolo não foi devidadamente declarado
    }
    
    public Token insereSimboloNaTabela(String chaveSimbolo, String chaveEscopo, SimboloSemantico s1){
        /*busca a tabela que deseja inserir, chaveEscopo é a chave da tabela da procedure que deseja se inserir 
          verifica se o simbolo já é existente nela
          por fim se não existir o simbolo é inserido
        */
        Tabela_Simbolos tabela;
        String chaveSimboloParam = null;
        if (chaveEscopo.contains("PROCEDURE")){
            chaveSimboloParam = chaveSimbolo.replace(String.valueOf(Token.variavel), String.valueOf(Token.parametro));
        }
        //busco a tabela da procedure ou program
        if (this.tabelasAnalisadorSemantico.containsKey(chaveEscopo)){
            tabela = this.tabelasAnalisadorSemantico.get(chaveEscopo);
            //tratamento para que parametro e variável local de uma procedure não tenham o mesmo nome
            if ((chaveSimboloParam != null) && (tabela.existeSimbolo(chaveSimboloParam))){
                //já existe um parametro com o mesmo nome declarado
                return Token.variavelRedeclarada;
           }
           if (tabela.insereSimbolo(s1)) return Token.Aceito;
            else return Token.variavelRedeclarada;
        }
        System.out.println("TABELA NÃO ENCONTRADA \n\n\n\n");
        return Token.tabelaNaoEncontrada;
       
    }
    
    public void informaQueSimboloFoiUtilizado(String chaveEscopo, String chaveProgram, String chaveSimbolo){
         Tabela_Simbolos tabela = this.buscaTabela(chaveEscopo);       
         if (tabela != null){
             if (tabela.informaQueFoiUtilizado(chaveSimbolo, chaveEscopo)) return;
         }
         //não estou no escopo global
         if (!chaveEscopo.equals(chaveProgram)){
             //procuro na tabela global
             tabela = this.buscaTabela(chaveProgram); 
             if (tabela != null){
                 if (tabela.informaQueFoiUtilizado(chaveSimbolo, chaveProgram)) return;
             } 
         }
         
    }
    
    public ArrayList<SimboloSemantico> returnVariaveisNaoUsadas(Analisador_Sintático.model.AnalisadorSintatico AnSintatico){
        ArrayList<SimboloSemantico> variaveisNaoUsadas = new ArrayList<>();
        Map<String, Tabela_Simbolos> tabelaSimbolos = null;
        
        try {
            tabelaSimbolos = AnSintatico.getAnalisadorSemantico().getTabelasAnalisadorSemantico();
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String key: tabelaSimbolos.keySet()){
            Tabela_Simbolos t = tabelaSimbolos.get(key);
            Map<String, SimboloSemantico> tab = t.getTabela();
            for (String key_2: tab.keySet()){
                SimboloSemantico s1 = tab.get(key_2);
                if (!s1.isUtilizado()) variaveisNaoUsadas.add(s1);
            }
        }
        return variaveisNaoUsadas;
    }
    public String mensagemVariaveisNaoUsadas(Analisador_Sintático.model.AnalisadorSintatico AnSintatico){
         ArrayList<SimboloSemantico> variaveisNaoUsadas = this.returnVariaveisNaoUsadas(AnSintatico);
         String mensagem = null;
         if (variaveisNaoUsadas.size() > 0 ) mensagem ="Aviso! \n";
         for (SimboloSemantico s1: variaveisNaoUsadas){
             mensagem+= "Variável "+s1.getLexema()+" declarada na linha "+s1.getLinha()+" não foi utilizada! \n";
         }
         
         return mensagem;
    }
    //operações relativo a assinatura de procedures 
    public Assinatura getAssinatura(String nomeProc){
        return this.assProcedures.getNovaAss(nomeProc);
    }
    
    public void insereAssinatura (String chaveProcedure){
         this.assProcedures.insereAssinatura(chaveProcedure);
     }
     
     public void addParamAss( String chaveProc, Token param){
         this.assProcedures.addParamAss(chaveProc, param);
     }
     
     public ArrayList<Token> getParametrosProcedure (String chaveProc){
        return this.assProcedures.getParametrosProcedure(chaveProc);
     }
     
     public boolean verificaChamadaProcedimento(String chave, ArrayList<Token> chamadaProc){
         return this.assProcedures.verificaChamadaProcedimento(chave, chamadaProc);
     }
}


package Analisador_Sintático.model;

import Analisador_Lexico.Model.Simbolos;
import Analisador_Lexico.Model.Token;
import Analisador_Semantico.model.AnalisadorSemantico;
import Analisador_Semantico.model.AssinaturaProcedure;
import Analisador_Semantico.model.SimboloSemantico;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private ArrayList<Simbolos> simbolos; // = Analisador_Lexico.Model.Analisador_Lexico.returnSimbolos();
    private ArrayList<Token> tokensEsperado; // tokens usados para o tratamento de erro
    //first e follow
    private Follow follow = new Follow();
    private First first = new First();
    //mensagem de erro
    private String msg = "";
    private String msgSemantica;
    private int linha = 0;
    //quando nao encontra id de procdures ou program
    private int qntProcedureSemNome; //para as procedures sem nome ficaram com nome semNome0, semNome1, semNome2..., semNomeN
    private static String SemNome = "SemNome"; //para procedures e program sem nome;
    private boolean encontrouErro; //se encontrar erro não executa a análise semantica?
    //analise semantica
    private Analisador_Semantico.model.AnalisadorSemantico AnSemantico;
    //chaveProgram 
    private String chaveProgram;
    
    public Parser(){
      this.AnSemantico  = new AnalisadorSemantico(); 
      this.encontrouErro = false;
      this.qntProcedureSemNome = 1;
      this.msg="";
      this.msgSemantica="";
    }

    public AnalisadorSemantico getAnSemantico() {
        return AnSemantico;
    }
    
    public void inicializaTokensEsperado(){
        tokensEsperado = new ArrayList<>();
    }
    
    public String getMsg(){
        return this.msg;
    }

    public String getMsgSemantica() {
        return msgSemantica;
    }
    
   public void addErroSemantico(Token tipoErro, String msg, Token tipoEsperado, Token tipoEncontrado, ArrayList<Token> tokensAssProc, ArrayList<Token> tokensChamProc ){
        int linha = this.simbolos.get(0).getLinha();
        
        if (tipoErro == Token.variavel){
            //variável não declarada
            //msg aqui é o nome da variável não encontrada
            msgSemantica+= "Variável "+msg+" não declarada!" +"Erro ocorrido na linha "+linha+"\n";
            this.encontrouErro = true;
        }
        
        if (tipoErro == Token.variavelRedeclarada){
            //variável redeclarada
            //msg aqui é o nome da variável redeclarada
            msgSemantica+= "Variável "+msg+" já foi declarada!" +"Erro ocorrido na linha "+linha+"\n";
            this.encontrouErro = true;
        }
        if (tipoErro == Token.PROCEDURE){
            //procedure não declarada
            //msg aqui é o nome da Procedure não encontrada
            msgSemantica+= "Procedure "+msg+" não declarada!" +" ocorrido na linha "+linha+"\n";
            this.encontrouErro = true;
        }
        if (tipoErro == Token.TIPO){
            //erro de tipo
            msgSemantica+= "Erro! "+tipoEsperado+" está recebendo "+tipoEncontrado+" ocorrido na linha"+linha+"\n";
            this.encontrouErro = true;
        }
        
        if (tipoErro == Token.chamadaProc){
            //erro na chamada de procedimento
            //msg nome Procedimento que foi chamado erroneamente 
            String assProc = "["; 
            String  chamadaProc ="[";
            assProc = tokensAssProc.stream().map((t) -> String.valueOf(t)+", ").reduce(assProc, String::concat);
            assProc = assProc.substring(0, assProc.length()-2)+"]";
            chamadaProc = tokensChamProc.stream().map((t) -> String.valueOf(t)+", ").reduce(chamadaProc, String::concat);
            chamadaProc = chamadaProc.substring(0, chamadaProc.length()-2)+"]";
            msgSemantica+= "Erro na chamada do procedimento "+msg+"! Era esperado a sequência "+
                    assProc+" e foi encontrada a sequência "+chamadaProc+
                    " na linha "+linha;
            this.encontrouErro = true;
        }
    }
   
    public void addSimboloNaTabela(String chaveEscopo, String nomeSimbolo, Token escopo, Token categoria, Token tipo, float valorNumerico, boolean valorBoleano){
        String chaveSimbolo;
        boolean usado = false;
        Token t;
        if (nomeSimbolo.equals(SemNome)){
            nomeSimbolo += qntProcedureSemNome;
            qntProcedureSemNome++;
        }
        if (categoria == Token.PROGRAM){
           usado = true;   
        }
        chaveSimbolo = this.AnSemantico.geraChave(nomeSimbolo, categoria);
        SimboloSemantico s1 = new SimboloSemantico(nomeSimbolo, categoria, tipo, valorNumerico, valorBoleano, chaveEscopo, chaveSimbolo, this.simbolos.get(0).getLinha());
        s1.setUtilizado(usado);
        t = this.AnSemantico.insereSimboloNaTabela(chaveSimbolo, chaveEscopo, s1);
        if (t == Token.variavelRedeclarada){
            String nome = nomeSimbolo.replace(String.valueOf(Token.PROCEDURE), "");
            nome = nomeSimbolo.replace(String.valueOf(Token.PROGRAM), "");
            nome = nomeSimbolo.replace(String.valueOf(Token.variavel), "");
            nome = nomeSimbolo.replace(String.valueOf(Token.parametro), "");
            this.addErroSemantico(t, nome, null, null, null, null);
        }
    }
   
    private void  imprimeSimbolos(){
        simbolos.forEach((s) -> {
            System.out.println(s.toString());
        });
    }
    
    private  void recriaArraySimbolos(ArrayList<Simbolos> aux){
        this.simbolos = new ArrayList<>();
        simbolos.addAll(aux);
    }
    
    private void preparaSimbolos(){
        //remove os simbolos Qlinha e Ignore 
         ArrayList<Simbolos> aux = new ArrayList<>();
        aux.addAll(this.simbolos);
        simbolos.stream().filter((s) -> ((s.getToken() == Token.QLINHA) || (s.getToken() == Token.IGNORE))).forEachOrdered((s) -> {
            aux.remove(s);
        });
        recriaArraySimbolos(aux);
    }
    
    private void tratamento_de_Erro(){
        //função que ignora tudo até encontrar um terminal pré-determinado
        int tamanhoSimbolos = this.simbolos.size();
        int tamanhoTokensEsp = tokensEsperado.size();
        Simbolos s;

        while(!this.simbolos.isEmpty()){
            s = this.simbolos.get(0);
            for (int j = 0; j < tamanhoTokensEsp; j++){
                if (s.getToken() == tokensEsperado.get(j)){
                    System.out.println(s.getToken()+" == "+ tokensEsperado.get(j));
                    return;
                }
            }
            this.simbolos.remove(s);
            
        }
//         this.recriaArraySimbolos(aux);
        tokensEsperado = new ArrayList<>();
    }
    
    public void analise_Sintatica() throws IOException{
        //método responsável por fazer a análise sintática 
        this.simbolos = Analisador_Lexico.Model.Analisador_Lexico.returnSimbolos();
        tokensEsperado = new ArrayList<>();
        msg = "";
        this.preparaSimbolos();
        this.program();
        
    }
    
    private void imprimeERRO(Token naoEncontrado){
        //imprime o erro e trata ele 
        this.encontrouErro = true;
        Token token;
        if (!simbolos.isEmpty()){
            token = this.simbolos.get(0).getToken();
            this.linha = this.simbolos.get(0).getLinha();
            this.msg += "Erro, "+token+" encontrado! É esperado o "+ naoEncontrado +" na linha "+this.linha+"\n";
            System.out.println("Erro, "+token+" encontrado! É esperado o "+ naoEncontrado +" na linha "+this.linha+"\n");
            //this.tokensEsperado.add(tokenEsperado);
            tratamento_de_Erro();
        }else{
            this.msg += "Erro! "+ naoEncontrado +" esperado na última linha \n"
                    + "Fim de Arquivo!\n";
            System.out.println("Erro! "+ naoEncontrado +" esperado na última linha \n"
                    + "Fim de Arquivo!\n");
        }
        
    }
    
    private void imprimeERROv2(ArrayList<Token> tokensNaoEncontrado){
        //imprime o erro e trata ele 
        String mensagem; 
        mensagem = "Erro, encontrado! [";
        mensagem = tokensNaoEncontrado.stream().map((t) -> t+", ").reduce(mensagem, String::concat);
        mensagem = mensagem.substring(0, mensagem.length()-2)+"] ";
        if (!simbolos.isEmpty()){
            this.linha = this.simbolos.get(0).getLinha();
            mensagem+= "esperado na linha "+this.linha+"\n";
            this.msg+=mensagem;
            //this.tokensEsperado.add(tokenEsperado);
            tratamento_de_Erro();
        }else{
            this.msg += mensagem +"esperado na última linha \n"
                    + "Fim de Arquivo!";
            System.out.println(mensagem);
        }
    }
  
    private void program(){
        System.out.println("Program \n");
        //trata a regra a regra program
        //regra: "program <identificador>;"
        String chaveEscopo; //nome da tabela que vai passando para os métodos que usem a tabela program
        if ((this.simbolos == null) || this.simbolos.isEmpty()){
            System.out.println("Simbolos é nulo ou vazio!");
            this.msg = "Não há fonte para ser analisado!";
            return;
        }
        this.inicializaTokensEsperado();
        //encontrando o program
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.PROGRAM)){
                //encontrei o token program e o removo
                this.simbolos.remove(0);
        }else {
               this.tokensEsperado.addAll(follow.getFollowProgram());
               this.tokensEsperado.addAll(first.getFirstBloco());   
               imprimeERRO(Token.PROGRAM);
        
        }
        //encontrando o identificador (nome do programa)
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR)){
            //adiciona uma tabela para program  
            chaveEscopo = this.AnSemantico.geraChave(this.simbolos.get(0).getLexema(), Token.PROGRAM);
            this.chaveProgram = chaveEscopo;
            this.AnSemantico.addTabela(chaveEscopo);
            
            //cria o simbolo program e adiciona o simbolo na tabela program
            this.addSimboloNaTabela(chaveEscopo, this.simbolos.get(0).getLexema(), Token.PROGRAM, Token.PROGRAM, Token.PROGRAM, 0, false);
            this.simbolos.remove(0);
        
        }else{
            //adiciona uma tabela para program 
            chaveEscopo = Parser.SemNome+qntProcedureSemNome;
            this.AnSemantico.addTabela(chaveEscopo);
            qntProcedureSemNome++;
            
            //cria o simbolo program e adiciona o simbolo na tabela program
            this.addSimboloNaTabela(chaveEscopo,Parser.SemNome+qntProcedureSemNome, Token.PROGRAM, Token.PROGRAM, Token.PROGRAM, 0, false);
            
            //não encontrou o identificador
            this.tokensEsperado.addAll(follow.getFollowProgram());
            this.tokensEsperado.addAll(first.getFirstBloco());
            imprimeERRO(Token.IDENTIFICADOR);
        
        }
        
        //encontrado o ponto virgula
        if (!this.simbolos.isEmpty() && (this.simbolos.get(0).getToken() == Token.pontoVirgula)){
                this.simbolos.remove(0);
        }else{
            //não encontrado o ponto e vírgula
            this.tokensEsperado.addAll(first.getFirstBloco());   
            imprimeERRO(Token.pontoVirgula);
        }
        bloco(chaveEscopo, Token.PROGRAM);
        
        
        System.out.println("Fim Program \n");
    }
    
    private void declaracaoDeVariaveis(String chaveEscopo, Token tipoEscopo){
        System.out.println("Declaração de variáveis \n");
        this.inicializaTokensEsperado();
        //analisando a declação de variável 
//         boolean encontrouID = false;
        Token tipo = null; 
        ArrayList<String> identificadres = new ArrayList<>();
         if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.INT) || (this.simbolos.get(0).getToken() == Token.BOOLEAN))){ 
            // removendo o token tipo 
            tipo = this.simbolos.get(0).getToken();
            this.simbolos.remove(0);
         }else{
             this.tokensEsperado.addAll(follow.getFollowDeclVar());
             this.tokensEsperado.addAll(first.getFirstBloco());
             imprimeERRO(Token.TIPO);
         }
         
         this.listaDeIdentificadores(tokensEsperado, identificadres);
         this.addIdentificadoresNaTabela(identificadres, chaveEscopo, tipoEscopo, Token.variavel, tipo);
         
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.pontoVirgula)){
             //encontrou qualquer outra coisa, ou seja terminou a declaração de variáveis então deve ter um ponto e vírgula
              this.simbolos.remove(0);
         }else{
              //não encontrou o ponto e vírgula 
              this.tokensEsperado.addAll(follow.getFollowDeclVar());
              this.tokensEsperado.addAll(first.getFirstBloco());
              imprimeERRO(Token.pontoVirgula);
            }
         

        if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.INT) || (this.simbolos.get(0).getToken() == Token.BOOLEAN))){
            this.declaracaoDeVariaveis(chaveEscopo, tipoEscopo);
        }
        
        System.out.println("Fim Declaração de variáveis \n");
    }
    
    private void listaDeIdentificadores(ArrayList<Token> tokensEsperadoProcedure,ArrayList<String> identificadores){
        System.out.println("Lista de Identificadores");
        this.inicializaTokensEsperado();
        if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR))){
           //this.addSimboloNaTabela(chaveEscopo, this.simbolos.get(0).getLexema(), tipoEscopo, categoria, tipoVariavel, 0, false);
           identificadores.add(this.simbolos.get(0).getLexema());
           this.simbolos.remove(0);
        }else {
                tokensEsperado.addAll(tokensEsperadoProcedure);
                imprimeERRO(Token.IDENTIFICADOR);
         }
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.virgula)){
            this.simbolos.remove(0);
            this.listaDeIdentificadores(tokensEsperadoProcedure, identificadores);
        }
        System.out.println("Fim Lista de Identificadores");
    }
    
    private void addIdentificadoresNaTabela(ArrayList<String> identificadores, String chaveEscopo, Token tipoEscopo, Token categoria, Token tipoVariavel){
       identificadores.forEach((s) -> {
            this.addSimboloNaTabela(chaveEscopo, s, tipoEscopo, categoria, tipoVariavel, 0, false);
        });
    }
    
    private void parametrosFromais(ArrayList<Token> tokensEsperadoProcedure, String chaveEscopo){
        this.inicializaTokensEsperado();
        this.listaDeParametros(tokensEsperadoProcedure, chaveEscopo);
        
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.FP)){
             this.simbolos.remove(0);
             }else{
                  tokensEsperado.addAll(first.getFirstBloco());
                  tokensEsperado.addAll(first.getFirstComando());
                  tokensEsperado.add(Token.pontoVirgula);
                  imprimeERRO(Token.FP);
        }
         
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.pontoVirgula)){
            this.simbolos.remove(0);
        }else{
              tokensEsperado.addAll(first.getFirstBloco());
              tokensEsperado.addAll(first.getFirstComando());
              tokensEsperado.add(Token.pontoVirgula);
            }
        
          System.out.println("Fim Lista de parametros \n");
         
    }
    
    private void listaDeParametros(ArrayList<Token> tokensEsperadoProcedure, String chaveEscopo){
        //[var] <lista de identificadores> : <identificador> 
        ArrayList<String> identificadres = new ArrayList<>();
        this.inicializaTokensEsperado();
        System.out.println("Lista de parametros \n");
        Token tipo = null;
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.VAR)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(tokensEsperadoProcedure);
            imprimeERRO(Token.VAR);
        }
        tokensEsperadoProcedure.remove(Token.VAR);
        //já encontrou o var, agora busca os identificadores 
        this.listaDeIdentificadores(tokensEsperadoProcedure, identificadres);
        
         //buscando os dois pontos 
         if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.doisPontos))){
           this.simbolos.remove(0);
         }else {
                tokensEsperado.addAll(tokensEsperadoProcedure);
                imprimeERRO(Token.doisPontos);
         }
         
         //buscando o tipo 
         if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.INT) || (this.simbolos.get(0).getToken() == Token.BOOLEAN))){
             tipo = this.simbolos.get(0).getToken();
             this.simbolos.remove(0);
         }else {
                tokensEsperado.addAll(tokensEsperadoProcedure);
                imprimeERRO(Token.TIPO);
         }
         //adicionando os parametros na tabela da procedure
         this.addIdentificadoresNaTabela(identificadres, chaveEscopo, Token.PROCEDURE, Token.parametro, tipo);
         for (String e: identificadres){
             this.AnSemantico.addParamAss(chaveEscopo, tipo);
             
         }
         //verificando se há mais parametros 
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.pontoVirgula)){
             this.simbolos.remove(0);
//             if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.VAR))
                this.listaDeParametros(tokensEsperadoProcedure, chaveEscopo);
        }
         
        
    }
    
    //[<parte de declarações de sub-rotinas>] 
    private void declaracaoSubRotina(String chaveEscopo){
        this.inicializaTokensEsperado();
        procedure(chaveEscopo);
        if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.PROCEDURE))){
           this.procedure(chaveEscopo);
        }
        
    }
  
    private void procedure(String chaveEscopo){
        //chaveEscopo é a procedure de quem a chamou,  ou seja program
        String chaveEscopoProcedure;
        this.inicializaTokensEsperado();
        System.out.println("Procedure \n");
           //tokens esperado em procedure
          ArrayList<Token> tokensEsperadoProcedure = new ArrayList<>();
          tokensEsperado.addAll(first.getFirstSecParamForm());
          tokensEsperado.addAll(follow.getFollowSecParamForm());
          tokensEsperado.addAll(first.getFirstComando());
          tokensEsperado.addAll(follow.getFollowCmdComposto());
          tokensEsperadoProcedure.addAll(tokensEsperado);
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.PROCEDURE)){
         // removendo o token procedure
          this.simbolos.remove(0);
         }else{
            tokensEsperado.addAll(tokensEsperadoProcedure);
            imprimeERRO(Token.PROCEDURE);
         }
         
        //nome da procedure 
        if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR))){
            //criando a tabela para essa procedure 
             chaveEscopoProcedure = this.AnSemantico.geraChave(this.simbolos.get(0).getLexema(), Token.PROCEDURE);
             this.AnSemantico.addTabela(chaveEscopoProcedure);
            //aqui eu insiro a procecure na tabela program
            this.addSimboloNaTabela(chaveEscopo, this.simbolos.get(0).getLexema(), Token.PROGRAM, Token.PROCEDURE, Token.PROCEDURE, 0, false);
            //crio uma assinatura para a procedure 
            this.AnSemantico.insereAssinatura(chaveEscopoProcedure);
            this.simbolos.remove(0);
        }else {
            //criando a tabela para essa procedure 
             chaveEscopoProcedure = this.AnSemantico.geraChave(Parser.SemNome+this.qntProcedureSemNome, Token.PROCEDURE);
             this.qntProcedureSemNome++;
             this.AnSemantico.addTabela(chaveEscopoProcedure);
             //crio uma assinatura para a procedure 
            this.AnSemantico.insereAssinatura(chaveEscopoProcedure);
            //aqui eu insiro a procecure na tabela program
            this.addSimboloNaTabela(chaveEscopo, Parser.SemNome+this.qntProcedureSemNome, Token.PROGRAM, Token.PROCEDURE, Token.PROCEDURE, 0, false);
            //modo panico
            tokensEsperado.addAll(tokensEsperadoProcedure);
            imprimeERRO(Token.IDENTIFICADOR);
        }
        //abre parenteses 
         if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.AP))){
            //obtendo o nome do parametro
            this.simbolos.remove(0);
        }else {
            tokensEsperado.addAll(tokensEsperadoProcedure);
            imprimeERRO(Token.AP);
        }
         //lista de parametros 
         this.parametrosFromais(tokensEsperadoProcedure, chaveEscopoProcedure);
         

        
        //chama bloco
        this.bloco(chaveEscopoProcedure, Token.PROCEDURE);
        if (((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.pontoVirgula))){
            // {declaração de procedimento> ;} 
            this.simbolos.remove(0);
        }
        System.out.println("FIm Procedure \n");
    }
    
    private void comandoComposto(String chaveEscopo){
        System.out.println("Comando Composto \n");
        this.inicializaTokensEsperado();
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.BEGIN)){
             this.simbolos.remove(0);
             this.comando(chaveEscopo);
         }else{
             tokensEsperado.addAll(first.getFirstComando());
             imprimeERRO(Token.BEGIN);
             this.comando(chaveEscopo);
        }
       
       //enquanto encontrar ponto e virgula aqui eu repito o comando
       while ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.pontoVirgula)){
           this.simbolos.remove(0);
           this.comando(chaveEscopo);
       }
        
       
        if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.END) || (this.simbolos.get(0).getToken() == Token.ENDF))){
            this.simbolos.remove(0);
        }else {
            this.tokensEsperado.add(Token.END);
            this.tokensEsperado.add(Token.ENDF);
            this.tokensEsperado.addAll(first.getFirstBloco());
            imprimeERRO(Token.END);
        }
         System.out.println("Fim Comando Composto \n");
    } 
    
    private void listaDeExpressoes(String chaveEscopo, ArrayList<Token> chamadaDeProc){
         //chamadaProc contém a quantidade de variáveis e os seus respectivos tipos em ordem
        this.inicializaTokensEsperado();
        System.out.println("Lista de Expressões");
        Token tipoExpressao;
        tipoExpressao = this.expressao(chaveEscopo);
        if (chamadaDeProc != null)
          chamadaDeProc.add(tipoExpressao);
        while ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.virgula)){
            this.simbolos.remove(0);
            tipoExpressao = this.expressao(chaveEscopo);
            chamadaDeProc.add(tipoExpressao);
        }
        System.out.println("Fim Lista de Expressões");
    }
    
    private void chamadaProcedimento(String chaveEscopo){
        boolean achou;
        String chaveSimbolo = null, nomeProcedimento = null;
        ArrayList<Token> paramChamProcedure = new ArrayList<>(); //parametros da chamada da procedure
        System.out.println("Chamada de Procedimento");
        this.inicializaTokensEsperado();
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR)){
            nomeProcedimento = this.simbolos.get(0).getLexema();
            if (nomeProcedimento.equals("read") || nomeProcedimento.equals("write") ){
               //para geração de código?
            }else{
                chaveSimbolo = this.AnSemantico.geraChave(this.simbolos.get(0).getLexema(), Token.PROCEDURE);
                achou = this.AnSemantico.existeSimboloGlobal(chaveSimbolo, chaveProgram);
                if (!achou){
                    addErroSemantico(Token.PROCEDURE, this.simbolos.get(0).getLexema(), null, null, null, null);
                }else{
                    //informo que eu utilizei o procedimento 
                    this.AnSemantico.informaQueSimboloFoiUtilizado(chaveEscopo, chaveProgram, chaveSimbolo);
                }
            }
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstComando());
            imprimeERRO(Token.IDENTIFICADOR);
        }
        
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.AP)){
            this.simbolos.remove(0);
       }else {
           tokensEsperado.addAll(first.getFirstComando());
           tokensEsperado.addAll(first.getFirstAtribuicao());
           imprimeERRO(Token.AP);
       }   
       listaDeExpressoes(chaveEscopo, paramChamProcedure);
       if (chaveSimbolo != null){
            if (!this.AnSemantico.verificaChamadaProcedimento(chaveSimbolo, paramChamProcedure)){
                //erro na chamada de procedimento 
                ArrayList<Token> assProc = this.AnSemantico.getParametrosProcedure(chaveSimbolo);
                this.addErroSemantico(Token.chamadaProc, nomeProcedimento, null,null, assProc, paramChamProcedure);
            }
       }
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.FP)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstComando());
            imprimeERRO(Token.FP);
        }
       
        System.out.println("Fim chamada procedimento");
    }
   
    private void atribuicao(String chaveEscopo){
        System.out.println("Atribuição \n");
        this.inicializaTokensEsperado();
        Token tipoExpressao; 
        Token tipoExpressaoAux;
        tipoExpressao = this.variável(chaveEscopo);
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.OPATRIBUICAO)){
            this.simbolos.remove(0);
        }else {
            tokensEsperado.addAll(first.getFirstAtribuicao());
            tokensEsperado.add(Token.pontoVirgula);
            imprimeERRO(Token.OPATRIBUICAO);
        }
        tipoExpressaoAux = this.expressao(chaveEscopo);
        if (tipoExpressao != tipoExpressaoAux){
            this.addErroSemantico(Token.TIPO,null, tipoExpressao, tipoExpressaoAux, null, null);
        }
        
         System.out.println("Fim Atribuição \n");
    }
    
    private Token expressao(String chaveEscopo){
        System.out.println("Expressão \n");
        Token tipoExpressao;
        Token tipoExpressaoAux;
        this.inicializaTokensEsperado();
        tipoExpressao = this.expressaoSimples(chaveEscopo);
         while ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.OPDIFERENCA) || 
                 (this.simbolos.get(0).getToken() == Token.OPMENOR) || 
                 (this.simbolos.get(0).getToken() == Token.OPMENORIGUAL) ||
                 (this.simbolos.get(0).getToken() == Token.OPMAIOR) || 
                 (this.simbolos.get(0).getToken() == Token.OPMAIORIGUAL)
                 )){
             //<relacao><expressaoSimples>
             linha = this.simbolos.get(0).getLinha();
             this.simbolos.remove(0);
             tipoExpressaoAux = this.expressaoSimples(chaveEscopo);
             if (tipoExpressao != tipoExpressaoAux){
                 this.addErroSemantico(Token.TIPO,null, tipoExpressao, tipoExpressaoAux, null, null);
             }
         }
         
          System.out.println("Fim Expressão \n");
          return tipoExpressao;
    }
  
    private Token expressaoSimples(String chaveEscopo){
        System.out.println("Expressão Simples \n");
        this.inicializaTokensEsperado();
        Token tipoExpressao;
        Token tipoExpressaoAux;
       
//        ArrayList<Token> tokemImprimeErrov2 = new ArrayList<>();
        //removendo os operadores soma e subtração
        if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.OPSOMA) || 
                 (this.simbolos.get(0).getToken() == Token.OPSUB) )){
            this.simbolos.remove(0);
        }
        //invoca o termo()
        tipoExpressao = this.termo(chaveEscopo);
        
        while ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.OPSOMA) || 
                 (this.simbolos.get(0).getToken() == Token.OPSUB) ||
                 (this.simbolos.get(0).getToken() == Token.OR))){
            linha = this.simbolos.get(0).getLinha();
           tipoExpressaoAux = this.termo(chaveEscopo);
           if (tipoExpressao != tipoExpressaoAux){
             this.addErroSemantico(Token.TIPO,null, tipoExpressao, tipoExpressaoAux, null, null);
           }
        }
        System.out.println("Fim Expressão Simples \n");
        return tipoExpressao;
    }
    
    private Token termo(String chaveEscopo){
        System.out.println("Termo \n");
        Token tipoExpressao;
        Token tipoExpressaoAux;
        
        this.inicializaTokensEsperado();
        tipoExpressao = fator(chaveEscopo);
        while ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.OPMUL) || 
                 (this.simbolos.get(0).getToken() == Token.OPDIV) ||
                 (this.simbolos.get(0).getToken() == Token.AND))){
            this.simbolos.remove(0);
            tipoExpressaoAux = this.fator(chaveEscopo);
            if (tipoExpressao != tipoExpressaoAux){
               this.addErroSemantico(Token.TIPO,null, tipoExpressao, tipoExpressaoAux, null, null);
             }
            
        }
        
        System.out.println("Fim termo \n");
        return tipoExpressao;
    }
    
    private Token fator(String chaveEscopo){
        this.inicializaTokensEsperado();
        System.out.println("Fator:  \n");
        Token tipoExpressao = null;
        Token t = null;
        if (!this.simbolos.isEmpty())
               t = this.simbolos.get(0).getToken();
        
        if (t == Token.IDENTIFICADOR){
            return this.variável(chaveEscopo);
            
        }
        if ((t == Token.TRUE) || (t == Token.FALSE)){
            this.simbolos.remove(0);
            return Token.BOOLEAN;
        }
        if (t == Token.NUM){
            this.simbolos.remove(0);
            return Token.INT;
        }
        if (t == Token.AP){
            this.simbolos.remove(0);
            tipoExpressao = this.expressao(chaveEscopo);
            if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.FP)){
                this.simbolos.remove(0);
            }else{
                this.msg+="Erro! FP esperado";
                System.out.println("Erro! FP esperado");
 
            }
            return tipoExpressao;
        }
        
         if (t == Token.NOT){
             this.simbolos.remove(0);
             return this.fator(chaveEscopo);
         }
         System.out.println("Fim Fator \n");
         return tipoExpressao;
    }
   
    private Token variável(String chaveEscopo){
         System.out.println("Variável \n");
         this.inicializaTokensEsperado();
         Token tipoExpressao = null; 
         Token tipoExpressaoAux;
         String chaveSimbolo;
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR)){
            tipoExpressao = this.AnSemantico.buscaSimboloEmTodasTabelas(chaveProgram, chaveEscopo, this.simbolos.get(0).getLexema());
            if (tipoExpressao == null){
                addErroSemantico(Token.variavel, this.simbolos.get(0).getLexema(), null, null,null, null);
                this.encontrouErro = true;
            }
            if (tipoExpressao == Token.ERROR){
                addErroSemantico(Token.variavelRedeclarada, this.simbolos.get(0).getLexema(), null, null,null, null);
                this.encontrouErro = true;
            }
            //não se trata de uma variável não encontrada e nem de uma variável redeclarada então eu informo que já a utilizei
            chaveSimbolo = this.AnSemantico.geraChave(this.simbolos.get(0).getLexema(), Token.variavel);
            this.AnSemantico.informaQueSimboloFoiUtilizado(chaveEscopo, chaveProgram, chaveSimbolo);
            this.simbolos.remove(0);
        }else {
            tokensEsperado.addAll(follow.getFollowVariavel());
            imprimeERRO(Token.IDENTIFICADOR);
        }
        if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.OPSOMA) || 
                 (this.simbolos.get(0).getToken() == Token.OPSUB) )){
            this.simbolos.remove(0);
            tipoExpressaoAux = this.expressao(chaveEscopo);
             if (tipoExpressao != tipoExpressaoAux){
                 this.addErroSemantico(Token.TIPO,null, tipoExpressao, tipoExpressaoAux, null, null);
             }
        }
        
        System.out.println("Fim Variável \n");
        return tipoExpressao;
    }
    
    private void bloco(String chaveEscopo, Token escopo) {
        //escopo: tipo do escopo program ou procedure 
        //chave escopo nome do program ou procedure 
      
        System.out.println("Bloco: ");
        this.inicializaTokensEsperado();
        if ((!this.simbolos.isEmpty()) && ((this.simbolos.get(0).getToken() == Token.INT) || (this.simbolos.get(0).getToken() == Token.BOOLEAN))){
            //analisando a parte de declaração de variáveis
            this.declaracaoDeVariaveis(chaveEscopo, escopo);
        }
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.PROCEDURE)){
            declaracaoSubRotina(chaveEscopo);
        }
//        while ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.PROCEDURE)){
//            // <parte de declarações de subrotinas> ::= {declaração de procedimento> ;}
//            //analisando a parte de procedure
//            this.procedure();
//        }
        this.comandoComposto(chaveEscopo);

        System.out.println("Fim Bloco: ");
    } 
    
    private void comandoCondicional(String chaveEscopo){
        System.out.println("Comando condicional: ");
        this.inicializaTokensEsperado();
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IF)){
             this.simbolos.remove(0);
         }else{
             tokensEsperado.addAll(first.getFirstExpressao());
             imprimeERRO(Token.IF);
         }
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.AP)){
             this.simbolos.remove(0);
         }else{
             tokensEsperado.addAll(first.getFirstExpressao());
             imprimeERRO(Token.AP);
         }
         this.expressao(chaveEscopo);
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.FP)){
             this.simbolos.remove(0);
         }else{
             tokensEsperado.addAll(first.getFirstExpressao());
             imprimeERRO(Token.FP);
         }
         
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.THEN)){
             this.simbolos.remove(0);
         }else{
             tokensEsperado.addAll(first.getFirstComando());
             tokensEsperado.addAll(follow.getFollowCmdComposto());
             imprimeERRO(Token.THEN);
         }
         
         this.comando(chaveEscopo);
         if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.ELSE)){
             this.simbolos.remove(0);
             this.comando(chaveEscopo);
         }
         System.out.println("fim comando condicional");
    }
   
    private void comandoRepetitivo(String chaveEscopo){
        System.out.println("Comando repetitivo");
        this.inicializaTokensEsperado();
        
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.WHILE)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstExpressao());
            tokensEsperado.add(Token.AP);
            tokensEsperado.add(Token.FP);
            tokensEsperado.add(Token.DO);
            tokensEsperado.addAll(first.getFirstComando());
            imprimeERRO(Token.WHILE);
        }
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.AP)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstExpressao());
            tokensEsperado.add(Token.FP);
            tokensEsperado.add(Token.DO);
            tokensEsperado.addAll(first.getFirstComando());
            imprimeERRO(Token.AP);
        }
        this.expressao(chaveEscopo);
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.FP)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstComando());
            tokensEsperado.add(Token.DO);
            imprimeERRO(Token.FP);
        }
        
        if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.DO)){
            this.simbolos.remove(0);
        }else{
            tokensEsperado.addAll(first.getFirstComando());
            imprimeERRO(Token.DO);
        }
        
        this.comando(chaveEscopo);
        System.out.println("fim comando repetitivo");
    }

    private void repeteComando(){
        if ((!this.simbolos.isEmpty()) && 
                ((this.simbolos.get(0).getToken() == Token.IDENTIFICADOR)|| 
                 (this.simbolos.get(0).getToken() == Token.BEGIN) ||
                 (this.simbolos.get(0).getToken() == Token.IF) ||
                ( this.simbolos.get(0).getToken() == Token.WHILE))){
            //então repete o comando
           // this.comando(chaveP);
        }else{
            tokensEsperado.addAll(first.getFirstBloco());
        }
           
    }
    
    private void comando(String chaveEscopo){
     this.inicializaTokensEsperado();
     String mensagem ="";
     System.out.println("Comando");
     if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IDENTIFICADOR)){
           //verifica qual é o próximo token para decidir qual método chamar 
             if ((!this.simbolos.isEmpty()) && (this.simbolos.get(1).getToken() == Token.OPATRIBUICAO)){
                this.atribuicao(chaveEscopo);
                return;
             }
             if ((!this.simbolos.isEmpty()) && (this.simbolos.get(1).getToken() == Token.AP)){
                 this.chamadaProcedimento(chaveEscopo);
                 return;
             }
            this.simbolos.remove(0);
            tokensEsperado.addAll(first.getFirstComando());
            ArrayList<Token> t = new ArrayList<>();
            t.add(Token.AP);
            t.add(Token.OPATRIBUICAO);
            this.imprimeERROv2(t);
            this.comando(chaveEscopo);
            //msg+= this.simbolos.get(0).getLexema()+" na linha: "+this.simbolos.get(0).getLinha()+"\n";
            
            
       }
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.BEGIN)){
           this.comandoComposto(chaveEscopo);
           return;
       }
       
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.IF)){
           this.comandoCondicional(chaveEscopo);
           return;
       }
       
       if ((!this.simbolos.isEmpty()) && (this.simbolos.get(0).getToken() == Token.WHILE)){
           this.comandoRepetitivo(chaveEscopo);
           return;
       }
       System.out.println("Fim Comando");
    }
  
}

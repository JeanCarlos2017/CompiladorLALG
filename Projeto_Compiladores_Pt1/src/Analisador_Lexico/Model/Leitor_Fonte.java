
package Analisador_Lexico.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Leitor_Fonte {
    static String fonte;
    static String caminho;
    static ArrayList<Simbolos> simbolos;
    static ArrayList<Simbolos> simbolosErrados;
    public static String relatorio = "";
    private static int  qntERRO= 0;

    public static int getQntERRO() {
        return qntERRO;
    }
    
    
    public static void imprimeSimbolos(){
        System.out.println("Imprimindo simbolos: "+Leitor_Fonte.simbolos.size());
        Leitor_Fonte.simbolos.forEach((_item) -> {
            System.out.println(Leitor_Fonte.simbolos.toString());
        });
        System.out.println("Fim da impressão de simbolos");
    }
  
    public static void lerArquivo(String nome) throws FileNotFoundException{
        //utilizada para exibir na tela
        Leitor_Fonte.fonte = "";
        int num = 0;
        try{
            //String nome = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Arquivos\\Arquivo_Fonte.txt";
            FileReader arq = new FileReader(nome);
            Scanner leitura = new Scanner(arq);
            
            while(leitura.hasNextLine()){

                //this.linhas.add(leitura.nextLine());
                Leitor_Fonte.fonte += leitura.nextLine() + /*"\n"*/ System.lineSeparator();
                num++;
            }
                System.out.println("Número de linhas: " + num);
        } catch (IOException e) {
              System.err.printf("Erro na abertura do arquivo: %s.\n",
                e.getMessage());
            }
        System.out.println("Número de linhas: " + num);
        
    }
    
     

  
    public static String leitor_fonte_analise_lexica() throws FileNotFoundException, IOException{
            //faz a análise léxica direto do fonte
            Leitor_Fonte.simbolos = new ArrayList<>();
            Leitor_Fonte.simbolosErrados = new ArrayList<>();
            Leitor_Fonte.qntERRO = 0;
            //String nomeArq = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Arquivos\\Arquivo_Temp.txt";
            //Leitor_Fonte.caminho = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Arquivos\\Arquivo_Temp.txt";
            Leitor_Fonte.caminho =  new File("../Projeto_Compiladores_Pt1/src/Analisador_Lexico/Arquivos/Arquivo_Temp.txt").getCanonicalPath();
            System.out.println(Leitor_Fonte.caminho);
            Reader reader = new BufferedReader(new FileReader(caminho));
            Lexer lexer = new Lexer (reader);
            String resultado= "";
            parser(lexer);
            //Leitor_Fonte.imprimeSimbolos();
            return resultado;
    }
    
    public static void parser(Lexer lexer) throws IOException{
        while (true) {

            Simbolos s;

            try {
                s = lexer.yylex();
                if (s != null){
                    if (s.getToken() != Token.IGNORE){
                         Leitor_Fonte.simbolos.add(s);
                    }
                   if (s.getToken() == Token.ERROR){
                       Leitor_Fonte.qntERRO++;
                       Leitor_Fonte.simbolosErrados.add(s);
                   }
                }else{
                    return;
                }
            }catch (IOException ex) {
                Logger.getLogger(Analisador_Lexico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
 
    public static void salvar_arquivoTemp(String resposta_Lexica) throws IOException{
        //para permitir a insersão de novos tokens 
        File arq;
        //arq = new File("C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Arquivos\\Arquivo_Temp.txt");
        String caminho = new File("../Projeto_Compiladores_Pt1/src/Analisador_Lexico/Arquivos/Arquivo_Temp.txt").getCanonicalPath();
        arq = new File(caminho);
        PrintWriter writer;
        writer = new PrintWriter(arq);
        writer.print(resposta_Lexica);
        writer.close();
        
        
        
    }
  
    public static void salvar_arquivo(String resposta_Lexica) throws IOException{
        //para permitir a insersão de novos tokens 
        File arq;
        arq = new File("C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Resposta_Lexica.txt");
        PrintWriter writer;
        writer = new PrintWriter(arq);
        writer.print(resposta_Lexica);
        writer.close();
        
        
    }
}

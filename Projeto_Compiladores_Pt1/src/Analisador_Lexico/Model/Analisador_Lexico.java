
package Analisador_Lexico.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author Ruth
 */
public class Analisador_Lexico {
  
    public static String analise_lexica() throws IOException{
        return Leitor_Fonte.leitor_fonte_analise_lexica();
    }
    
    public static ArrayList<Simbolos> returnSimbolosErrados () throws IOException{
        Leitor_Fonte.leitor_fonte_analise_lexica();
        return Leitor_Fonte.simbolosErrados;
    }
    public static void salvar_arquivoTemp(String resposta_Lexica) throws IOException{
        Leitor_Fonte.salvar_arquivoTemp(resposta_Lexica);
    }
    public static String returnFonte(String caminho) throws FileNotFoundException{
        Leitor_Fonte.lerArquivo(caminho);
        return Leitor_Fonte.fonte;
    }
    
    public static ArrayList<Simbolos> returnSimbolos() throws IOException{
        //faz a aanálise léxica e retorna todos os simbolos em um arraylist
        Analisador_Lexico.analise_lexica();
        return Leitor_Fonte.simbolos;
    }
    
    public static boolean contemErro() throws IOException{
        int qntErro = Leitor_Fonte.getQntERRO();
        return qntErro > 0;
    }
}

/*
 a ideia é ter um arquivo texto com os simbolos reservador tais como: operadores, palavra reservadas, símbolos especiais...
 */
package Analisador_Lexico.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Ruth
 */
public class Leitor_Palavras_Reservadas {
    public static void leitor_arquivo(ArrayList <String> palavrasReservadas){
         try {
             String nome = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Arquivos\\Palavras_Reservadas.txt";
             FileReader arq = new FileReader(nome);
             BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); // lê a primeira linha
            // a variável "linha" recebe o valor "null" quando o processo
            // de repetição atingir o final do arquivo texto
            while (linha != null) {
              System.out.printf("%s\n", linha);
              palavrasReservadas.add(linha);
              linha = lerArq.readLine(); // lê da segunda até a última linha
            }

            arq.close();
          } catch (IOException e) {
              System.err.printf("Erro na abertura do arquivo: %s.\n",
                e.getMessage());
    }
        
    }
    
    public static void salvar_arquivo(ArrayList <String> palavrasReservadas) throws IOException{
        //para permitir a insersão de novos tokens 
        FileWriter arq;
        arq = new FileWriter("C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Palavras_Reservadas.txt");
        PrintWriter gravarArq = new PrintWriter(arq);

        
        palavrasReservadas.forEach((s) -> {
            gravarArq.printf("%s", s);
        });
        gravarArq.printf("+-------------+%n");

        arq.close();

        
    }
    
}

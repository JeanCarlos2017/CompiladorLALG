
package Analisador_Sintático.model;

import Analisador_Semantico.model.AnalisadorSemantico;
import java.io.IOException;

/**
 *
 * @author Ruth
 */
public class AnalisadorSintatico {
    private Parser parser;
    public AnalisadorSintatico(){
        this.parser = new Parser();
    }
    public void analiseSintatica() throws IOException{
        parser.analise_Sintatica();
    }
    
    public String getMensagem(){
        return parser.getMsg();
    }
    public String getMensagemSemantica(){
        return parser.getMsgSemantica();
    }
    public AnalisadorSemantico getAnalisadorSemantico() throws IOException{
//        parser.analise_Sintatica();
        return parser.getAnSemantico();
    }
}


package projeto_compiladores_pt1;

import View.IUAnalisadorSintatico1;
import javax.swing.JFrame;

/**
 *
 * @author Ruth
 */
public class Projeto_Compiladores_Pt1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IUAnalisadorSintatico1 lex = new IUAnalisadorSintatico1();
        lex.setVisible(true);
        lex.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
}

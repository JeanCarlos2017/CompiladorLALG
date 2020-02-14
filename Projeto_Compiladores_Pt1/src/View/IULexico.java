/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Analisador_Lexico.Model.Leitor_Fonte;
import Analisador_Lexico.Model.Simbolos;
import Analisador_Lexico.Model.Token;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Ruth
 */
public class IULexico extends javax.swing.JFrame {
    private static int linhaTabela = 0;
    
    /**
     * Creates new form IULexico
     */
    public IULexico() {
        initComponents();
        linhaTabela = 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAnalise = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableAnalise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Lexema", "Token", "Linha", "Coluna Inicial", "Coluna Final"
            }
        ));
        jScrollPane2.setViewportView(jTableAnalise);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void addToken(Simbolos s){
      //Define o estilo vermelho
      Color erro = Color.RED;
        MutableAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, Color.red);
        StyleConstants.setBold(attributes, true);

        jTableAnalise.setValueAt(s.getLexema(), linhaTabela, 0);
        jTableAnalise.setValueAt(s.getToken(), linhaTabela, 1);
        jTableAnalise.setValueAt(s.getLinha(), linhaTabela, 2);
        jTableAnalise.setValueAt(s.getColunaInicial(), linhaTabela, 3);
        jTableAnalise.setValueAt(s.getColunaFinal(), linhaTabela, 4);
     // }
      //addErro();
      DefaultTableModel modelo = (DefaultTableModel) jTableAnalise.getModel(); 
      modelo.addRow(new String[]{"", ""});
      
      IULexico.linhaTabela++;
    }
    
    public void addErro(){
        //Colori o erro
             this.jTableAnalise.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            //Aqui ele vai dar um tratamento em cada componente
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                //pega os atributos da classe pai
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                //Pega o valor da coluna lexema
                Token status =  (Token) table.getModel().getValueAt(row, 1);
                System.out.println(status+ "status");
                //Se for igual a nossa STRING de quando ocorre erro
                if (status == Token.ERROR) {
                    //Pinta de vermelho
                    setBackground(Color.RED);
                    setForeground(Color.WHITE);
                } else {
                    //Se não segue os valores default
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IULexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IULexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IULexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IULexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IULexico().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableAnalise;
    // End of variables declaration//GEN-END:variables
}

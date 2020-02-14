
package View;

import Analisador_Lexico.Model.Simbolos;
import Analisador_Lexico.Model.Token;
import Analisador_Semantico.model.AnalisadorSemantico;
import Analisador_Semantico.model.SimboloSemantico;
import Analisador_Semantico.model.Tabela_Simbolos;
import Analisador_Sintático.model.AnalisadorSintatico;
import Analisador_Sintático.view.BordaNumerica;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import Analisador_Sintático.model.First;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Ruth
 */
public class IUAnalisadorSintatico1 extends javax.swing.JFrame {

    
    private First first = new First();
    private Analisador_Sintático.model.AnalisadorSintatico AnSintatico;
    private  IULexico iuLexico;
    private Analisador_Semantico.model.AnalisadorSemantico anSemantico;
    public IUAnalisadorSintatico1() {
        initComponents();
        gerarLexer();
        //this.decoracao = new TextoDecoracao(jTextAreaAnalise);
        ImageIcon logo = new ImageIcon("image/icone 1.png");
        setIconImage(logo.getImage());
        this.jTextPaneAnalise.setBorder(new BordaNumerica());
        anSemantico = new AnalisadorSemantico();
    }
    private boolean isContained(Token tokenVerificado, ArrayList<Token> listaToken){
        if (listaToken.stream().anyMatch((t) -> (t == tokenVerificado))) {
            return true;
        }
        return false;
    }
   
    public void colorirJpainel(Color color, JTextPane jtext, int posInicial){
        StyledDocument styledDocument = jtext.getStyledDocument();
        StyleContext style = StyleContext.getDefaultStyleContext();
        AttributeSet corTexto = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, color);
        if (posInicial > 0){
            //ajusta para a coloração do aviso de variaveis declaradas nao usadas
            AttributeSet corAviso = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.orange);
             styledDocument.setCharacterAttributes(0, posInicial, corAviso, true);
        }
        styledDocument.setCharacterAttributes(posInicial, jtext.getText().length(), corTexto, true);
    }
   
    public void colorirTexto() throws IOException, BadLocationException{
        StyledDocument styledDocument = this.jTextPaneAnalise.getStyledDocument();
        StyleContext style = StyleContext.getDefaultStyleContext();
        
        // definindo os estilos 
//        Style negrito, italico, normal;
//        normal = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
//        negrito = this.jTextPaneAnalise.getStyledDocument().addStyle("program", normal);
//        StyleConstants.setBold(negrito, true);
//        italico = this.jTextPaneAnalise.getStyledDocument().addStyle("italic", normal);
//        StyleConstants.setItalic(italico, true);
//        
        //definindo as cores 
        Color numeroColor = new Color(0, 178, 120);
        Color operadoresColor = new Color (255,89,0);
        Color palavrasReservadasColor = new Color (127,69,38);
        
        //definindo os estilos 
        AttributeSet operadores = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, operadoresColor);
        AttributeSet id = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
        AttributeSet palavrasReservadas = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, palavrasReservadasColor);
        AttributeSet numeros = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, numeroColor);
        AttributeSet restante = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        AttributeSet comentarios = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.GRAY);
        
        //obtendo os simbolos 
        ArrayList<Simbolos> simbolosFonte =  Analisador_Lexico.Model.Analisador_Lexico.returnSimbolos();
        
        //Limpando estilo existente
        styledDocument.setCharacterAttributes(0, jTextPaneAnalise.getText().length(), restante, true);
        int linha;
        int comprimento;
        for (Simbolos s: simbolosFonte){
            linha = s.getLinha();
            comprimento = s.getColunaFinal() - s.getColunaInicial();
           if (isContained(s.getToken(), first.getPalavrasReservadas())){
               styledDocument.setCharacterAttributes(s.getDeslocamento()-linha, comprimento, palavrasReservadas, false);
           }
           if (isContained(s.getToken(), first.getOperadores())){
               styledDocument.setCharacterAttributes(s.getDeslocamento()-linha, comprimento, operadores, false);
           }
           if (s.getToken() == Token.IDENTIFICADOR){
               styledDocument.setCharacterAttributes(s.getDeslocamento()-linha, comprimento, id, false);
           }
           if (s.getToken() == Token.NUM){
               styledDocument.setCharacterAttributes(s.getDeslocamento()-linha, comprimento, numeros, false);
           }
           if ((s.getToken() == Token.COMMENTS) || (s.getToken() == Token.COMMENT_LINE)){
                styledDocument.setCharacterAttributes(s.getDeslocamento()-linha, comprimento, comentarios, false);
                
           }
        }
    }
    
    public  ArrayList<Simbolos> setTextoAnaliseLexica(){
        ArrayList<Simbolos> simbolos = null;
        ArrayList<Simbolos> simbolosErrados = null;
        try {
            Analisador_Lexico.Model.Analisador_Lexico.salvar_arquivoTemp(jTextPaneAnalise.getText());
            simbolos = Analisador_Lexico.Model.Analisador_Lexico.returnSimbolos();
            boolean analiseLexicaErro = Analisador_Lexico.Model.Analisador_Lexico.contemErro();
            if (analiseLexicaErro){
                //setando a mensagem 
                simbolosErrados = Analisador_Lexico.Model.Analisador_Lexico.returnSimbolosErrados();
                String msg = "A análise léxica encontrou erros, para mais informações verifique a tabela (Ctrl+Shift+L) \n";
                for (Simbolos s: simbolosErrados){
                    msg+= "Erro! Lexema "+s.getLexema()+" encontrado na linha "+s.getLinha()+"\n";
                }
                this.jTextPaneAnLex.setText(msg);
                this.colorirJpainel(Color.RED, jTextPaneAnLex, 0);
            }else{
                //setando a mensagem 
                String msg = "A análise léxica não encontrou erros!";
                this.jTextPaneAnLex.setText(msg);
                this.colorirJpainel(Color.GREEN, jTextPaneAnLex, 0);
            }
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return simbolos;
        
    }
    
    public void setTextoAnaliseSintatica(){
        AnSintatico = new AnalisadorSintatico();
        try {
            // TODO add your handling code here:
            Analisador_Lexico.Model.Analisador_Lexico.salvar_arquivoTemp(jTextPaneAnalise.getText());
            //alterações referentes ao analisador léxico
           
            if (Analisador_Lexico.Model.Analisador_Lexico.contemErro()){
              this.jTextPaneAnSint.setText("Há erros léxicos!\nAnálise Sintática não executada!"); 
              this.jTextPaneAnSem.setText("Há erros léxicos!\nAnálise Semântica não executada!");
              this.colorirJpainel(Color.red, jTextPaneAnSint, 0);
            }else{
                AnSintatico.analiseSintatica();
                String msg = AnSintatico.getMensagem();
                if (msg.equals("")){
                    msg = "Análise Sintática não encontrou erros!\n";
                    this.jTextPaneAnSint.setText(msg);
                    this.colorirJpainel(Color.green, jTextPaneAnSint, 0);
                }else{
                    this.jTextPaneAnSint.setText(msg);
                    this.colorirJpainel(Color.RED, jTextPaneAnSint, 0);
                }
                
                //aviso sobre variaveis decladas não usadas 
                 String mensagemAviso = anSemantico.mensagemVariaveisNaoUsadas(AnSintatico);
                int tamanhoAviso = mensagemAviso.length();
                this.jTextPaneAnSem.setText(mensagemAviso);
                this.colorirJpainel(Color.orange, jTextPaneAnSem, 0);
                //referente a análise semântica
                msg = AnSintatico.getMensagemSemantica();
                if (msg.equals("")){
                    msg = mensagemAviso + "Análise Semântica não encontrou erros!";
                    this.jTextPaneAnSem.setText(msg);
                    this.colorirJpainel(Color.green, jTextPaneAnSem, tamanhoAviso);
                }else{
                    this.jTextPaneAnSem.setText(mensagemAviso+msg);
                    this.colorirJpainel(Color.red, jTextPaneAnSem, tamanhoAviso);
                }
               
            }
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void gerarLexer(){
        String path;
        //C:\Users\Jean\Desktop\Projetos\Java\Compiladores\Projeto_Compiladores_Pt1\src\Analisador_Lexico\Model
        try {
            path = new File("../Projeto_Compiladores_Pt1/src/Analisador_Lexico/Model/Regras_Lexicas.flex").getCanonicalPath();
            File file=new File(path);
            JFlex.Main.generate(file);
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String path = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Projeto_Compiladores_Pt1\\src\\Analisador_Lexico\\Model\\Regras_Lexicas.flex";
       
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonLimparTexto = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneAnalise = new javax.swing.JTextPane();
        jButtonColorir = new javax.swing.JButton();
        jButtonLimparTextoAnalise = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextPaneAnLex = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextPaneAnSint = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPaneAnSem = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPaneGerCod = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemAbrir = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemLexico = new javax.swing.JMenuItem();
        jMenuItemAnaliseSintatica = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemTabLexica = new javax.swing.JMenuItem();
        jMenuItemTabSemantica = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(0, 0));

        jButtonLimparTexto.setText("Limpar Fonte");
        jButtonLimparTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparTextoActionPerformed(evt);
            }
        });

        jLabel1.setText("Fonte: ");

        jScrollPane2.setViewportView(jTextPaneAnalise);

        jButtonColorir.setText("Colorir");
        jButtonColorir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonColorirActionPerformed(evt);
            }
        });

        jButtonLimparTextoAnalise.setText("Limpar Relatório Análise");
        jButtonLimparTextoAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparTextoAnaliseActionPerformed(evt);
            }
        });

        jScrollPane9.setViewportView(jTextPaneAnLex);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Análise Léxica", jPanel1);

        jScrollPane8.setViewportView(jTextPaneAnSint);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Análise Sintática", jPanel2);

        jScrollPane7.setViewportView(jTextPaneAnSem);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Análise Semântica", jPanel3);

        jScrollPane6.setViewportView(jTextPaneGerCod);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Geração de Código", jPanel4);

        jMenu1.setText("File");

        jMenuItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAbrir.setText("Abrir Fonte");
        jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAbrir);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Salvar Fonte");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemSair.setText("Sair");
        jMenu1.add(jMenuItemSair);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Análise");

        jMenuItemLexico.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemLexico.setText("Análise Léxica");
        jMenuItemLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLexicoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemLexico);

        jMenuItemAnaliseSintatica.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAnaliseSintatica.setText("Análise Sintática");
        jMenuItemAnaliseSintatica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnaliseSintaticaActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemAnaliseSintatica);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Tabelas");

        jMenuItemTabLexica.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemTabLexica.setText("Tabela Léxica");
        jMenuItemTabLexica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTabLexicaActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemTabLexica);

        jMenuItemTabSemantica.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemTabSemantica.setText("Tabela Semântica");
        jMenuItemTabSemantica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTabSemanticaActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemTabSemantica);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButtonLimparTexto)
                        .addGap(98, 98, 98)
                        .addComponent(jButtonLimparTextoAnalise)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonColorir))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLimparTexto)
                    .addComponent(jButtonColorir)
                    .addComponent(jButtonLimparTextoAnalise))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirActionPerformed
        // TODO add your handling code here:
        
       String fonte;
        try {
            String caminho;
            JFileChooser abrirImagem = new JFileChooser("basicos");
        //abrirImagem.setCurrentDirectory(new java.io.File("C:\\Users\\Admin\\Documents\\9 semestre\\Teoria\\exercicios jflap\\10.04\\testeMore.jff"));

        if (abrirImagem.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File F = abrirImagem.getSelectedFile();
           caminho = F.getAbsolutePath();
            abrirImagem.setCurrentDirectory(new java.io.File(caminho));
            fonte = Analisador_Lexico.Model.Analisador_Lexico.returnFonte(caminho);
            jTextPaneAnalise.setText(fonte);
            this.jButtonColorirActionPerformed(evt);
        }
        } catch (FileNotFoundException ex) {
             JOptionPane.showMessageDialog(this, ERROR_MESSAGE, "Não foi possível abrir o arquivo", OK_CANCEL_OPTION);
                        
        }
        
    }//GEN-LAST:event_jMenuItemAbrirActionPerformed

    private void jButtonLimparTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparTextoActionPerformed
        // TODO add your handling code here:
        jTextPaneAnalise.setText("");
    }//GEN-LAST:event_jButtonLimparTextoActionPerformed

    private void jMenuItemLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLexicoActionPerformed
        // TODO add your handling code here:
       this.limparTextoRelatorios();
       this.setTextoAnaliseLexica();
       
    }//GEN-LAST:event_jMenuItemLexicoActionPerformed

    private void jMenuItemAnaliseSintaticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnaliseSintaticaActionPerformed
        this.limparTextoRelatorios();
        this.setTextoAnaliseLexica();
        this.setTextoAnaliseSintatica();
    }//GEN-LAST:event_jMenuItemAnaliseSintaticaActionPerformed

    private void jButtonColorirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonColorirActionPerformed
        try {
            // TODO add your handling code here:
            this.colorirTexto();
            this.jTextPaneAnalise.repaint();
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_jButtonColorirActionPerformed

    private void jButtonLimparTextoAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparTextoAnaliseActionPerformed
        // TODO add your handling code here:
        this.limparTextoRelatorios();
    }//GEN-LAST:event_jButtonLimparTextoAnaliseActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(jMenu1, "Ainda não suportado!");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemTabSemanticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTabSemanticaActionPerformed
        // TODO add your handling code here:
        IUTabelaSemantica iuTabelaSemantica = new IUTabelaSemantica();
        Map<String, Tabela_Simbolos> tabelaSimbolos = null;
        this.jMenuItemAnaliseSintaticaActionPerformed(evt);
        this.AnSintatico = new AnalisadorSintatico();
        try {
            this.AnSintatico.analiseSintatica();
        } catch (IOException ex) {
            Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                iuTabelaSemantica.addSimboloSemantico(s1);
            }
        }
        iuTabelaSemantica.setVisible(true);
        iuTabelaSemantica.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItemTabSemanticaActionPerformed

    private void jMenuItemTabLexicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTabLexicaActionPerformed
        // TODO add your handling code here:
       this.limparTextoRelatorios();
       iuLexico = new IULexico();
       ArrayList<Simbolos> simbolos = this.setTextoAnaliseLexica();
       int tamanho = 0;
       if (simbolos != null) tamanho = simbolos.size();
       for (int i= 0; i < tamanho; i++){
           iuLexico.addToken(simbolos.get(i));
       }
       iuLexico.setVisible(true);
       iuLexico.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItemTabLexicaActionPerformed

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
            java.util.logging.Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IUAnalisadorSintatico1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IUAnalisadorSintatico1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonColorir;
    private javax.swing.JButton jButtonLimparTexto;
    private javax.swing.JButton jButtonLimparTextoAnalise;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemAbrir;
    private javax.swing.JMenuItem jMenuItemAnaliseSintatica;
    private javax.swing.JMenuItem jMenuItemLexico;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemTabLexica;
    private javax.swing.JMenuItem jMenuItemTabSemantica;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPaneAnLex;
    private javax.swing.JTextPane jTextPaneAnSem;
    private javax.swing.JTextPane jTextPaneAnSint;
    private javax.swing.JTextPane jTextPaneAnalise;
    private javax.swing.JTextPane jTextPaneGerCod;
    // End of variables declaration//GEN-END:variables

    private void limparTextoRelatorios() {
        this.jTextPaneAnLex.setText("");
        this.jTextPaneAnSem.setText("");
        this.jTextPaneAnSint.setText("");
        this.jTextPaneGerCod.setText("");
    }
}

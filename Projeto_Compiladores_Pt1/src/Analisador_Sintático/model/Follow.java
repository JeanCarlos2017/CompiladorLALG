
package Analisador_Sintático.model;

import Analisador_Lexico.Model.Token;
import java.util.ArrayList;

public class Follow {
    private  ArrayList<Token> followProgram = new ArrayList<Token>();
    private  ArrayList<Token> followBloco = new ArrayList<Token>();
    private  ArrayList<Token> followProgramPontoEVirgula = new   ArrayList<Token>();
    private  ArrayList<Token> followTipo = new   ArrayList<Token>();
    private  ArrayList<Token> followParteDeclVar = new   ArrayList<Token>();
    private  ArrayList<Token> followDeclVarLoop = new   ArrayList<Token>();
    private  ArrayList<Token> followDeclVar = new   ArrayList<Token>();
    private  ArrayList<Token> followListaIdent = new   ArrayList<Token>();
    private  ArrayList<Token> followIdentLoop = new   ArrayList<Token>();
    private  ArrayList<Token> followDeclProc = new   ArrayList<Token>();
    private  ArrayList<Token> followParamForm = new   ArrayList<Token>();
    private  ArrayList<Token> followSecParamFormLoop = new   ArrayList<Token>();
    private  ArrayList<Token> followSecParamForm = new   ArrayList<Token>();
    private  ArrayList<Token> followCmdComposto = new   ArrayList<Token>();
    private  ArrayList<Token> followCmdLoop = new   ArrayList<Token>();

    private  ArrayList<Token> followStartIdent = new   ArrayList<Token>();
    private  ArrayList<Token> followOptIdent = new   ArrayList<Token>();
    private  ArrayList<Token> followOptListaExpr = new   ArrayList<Token>();
    private  ArrayList<Token> followCmdCond = new   ArrayList<Token>();
    private  ArrayList<Token> followElseCmdOpt = new   ArrayList<Token>();
    private  ArrayList<Token> followCmdRep = new   ArrayList<Token>();
    private  ArrayList<Token> followExpressao = new   ArrayList<Token>();
    private  ArrayList<Token> followRelaOpt = new   ArrayList<Token>();
    private  ArrayList<Token> followRelacao = new   ArrayList<Token>();
    private  ArrayList<Token> followExprSimpl = new   ArrayList<Token>();
    private  ArrayList<Token> followSinalNOpt = new   ArrayList<Token>();
    private  ArrayList<Token> followSinalOpt = new   ArrayList<Token>();
    private  ArrayList<Token> followTermoLoop = new   ArrayList<Token>();
    private  ArrayList<Token> followTermo = new   ArrayList<Token>();
    private  ArrayList<Token> followFatorLoop = new   ArrayList<Token>();
    private  ArrayList<Token> followVariavel = new   ArrayList<Token>();
    private  ArrayList<Token> followFator = new   ArrayList<Token>();
    private  ArrayList<Token> followExprOpt = new   ArrayList<Token>();
    private  ArrayList<Token> followListaExpr = new   ArrayList<Token>();
    private  ArrayList<Token> followExprLoop = new   ArrayList<Token>();

    public Follow() {
        this.iniciaFollows();
    }
    
    
    public  void iniciaFollows() {
        //Program
        followProgram.add(Token.PROGRAM);
        followProgram.add(Token.IDENTIFICADOR);
        followProgram.add(Token.pontoVirgula);
        
        //Bloco
        followBloco.add(Token.ENDF);
        followBloco.add(Token.pontoVirgula);

        //Tipo
        followTipo.add(Token.IDENTIFICADOR);

        //procedure
        followParteDeclVar.add(Token.PROCEDURE);
        followParteDeclVar.add(Token.BEGIN);
        
        //DeclVar
        followDeclVar.add(Token.INT);
        followDeclVar.add(Token.BOOLEAN);
        followDeclVar.add(Token.IDENTIFICADOR);
        followDeclVar.add(Token.pontoVirgula);
        
         //SecParamForm
        followSecParamForm.add(Token.pontoVirgula);
        followSecParamForm.add(Token.FP);
        
        //CmdComposto
        followCmdComposto.add(Token.pontoVirgula);
        followCmdComposto.add(Token.ENDF);
        followCmdComposto.add(Token.END);
        followCmdComposto.add(Token.IF);
        followCmdComposto.add(Token.WHILE);
        
        //variavel
        followVariavel.add(Token. pontoVirgula);
        followVariavel.add(Token.END);
        followVariavel.add(Token.OPATRIBUICAO);
        followVariavel.add(Token.OPSOMA);
        followVariavel.add(Token.OPSUB);
        followVariavel.add(Token.virgula);
        followVariavel.add(Token.AP);
        followVariavel.add(Token.NUM);
         followVariavel.add(Token.NOT);
        //-------------------------------------------------------------------------------------------
        //Declaracao Variavel loop
        followDeclVarLoop.add(Token.pontoVirgula);

        //ListaIdent
        followListaIdent.add(Token.pontoVirgula);
        
        //IdentLoop
        followIdentLoop.add(Token.pontoVirgula);
        followIdentLoop.add(Token.doisPontos);

        //DeclProc
        followDeclProc.add(Token.BEGIN);

        
        //SecParamFormLoop
        followSecParamFormLoop.add(Token.FP);
        

        

        followCmdLoop.add(Token.ENDF);


        //StartIdent
        followStartIdent.add(Token.pontoVirgula);
        followStartIdent.add(Token.ENDF);
        followStartIdent.add(Token.IF);

        followOptIdent.add(Token.pontoVirgula);
        followOptIdent.add(Token.ENDF);
        followOptIdent.add(Token.IF);

        followOptListaExpr.add(Token.pontoVirgula);
        followOptListaExpr.add(Token.ENDF);
        followOptListaExpr.add(Token.IF);

        followCmdCond.add(Token. pontoVirgula);
        followCmdCond.add(Token.END);
        followCmdCond.add(Token.IF);

        followElseCmdOpt.add(Token. pontoVirgula);
        followElseCmdOpt.add(Token.END);
        followElseCmdOpt.add(Token.IF);

        followCmdRep.add(Token. pontoVirgula);
        followCmdRep.add(Token.END);
        followCmdRep.add(Token.IF);

        followExpressao.add(Token. pontoVirgula);
        followExpressao.add(Token.END);
        followExpressao.add(Token.IF);
        followExpressao.add(Token.OPMUL);
        followExpressao.add(Token.OPDIV);
        followExpressao.add(Token.AND);
        followExpressao.add(Token.OPIGUALDADE);
        followExpressao.add(Token.OPMAIOR);
        followExpressao.add(Token.OPMENOR);
        followExpressao.add(Token.virgula);
        followExpressao.add(Token.FP);
        followExpressao.add(Token.IF);
        followExpressao.add(Token.DO);

        followRelaOpt.add(Token. pontoVirgula);
        followRelaOpt.add(Token.END);
        followRelaOpt.add(Token.IF);
        followRelaOpt.add(Token.OPMUL);
        followRelaOpt.add(Token.OPDIV);
        followRelaOpt.add(Token.AND);
        followRelaOpt.add(Token.OPIGUALDADE);
        followRelaOpt.add(Token.OPMAIOR);
        followRelaOpt.add(Token.OPMENOR);
        followRelaOpt.add(Token.virgula);
        followRelaOpt.add(Token.FP);
        followRelaOpt.add(Token.IF);
        followRelaOpt.add(Token.DO);

        followRelacao.add(Token.OPSOMA);
        followRelacao.add(Token.OPSUB);
        followRelacao.add(Token.IDENTIFICADOR);
        followRelacao.add(Token.NUM);
        followRelacao.add(Token.NUM);
        followRelacao.add(Token.AP);
        followRelacao.add(Token.NOT);

        followExprSimpl.add(Token. pontoVirgula);
        followExprSimpl.add(Token.END);
        followExprSimpl.add(Token.IF);
        followExprSimpl.add(Token.OPMUL);
        followExprSimpl.add(Token.OPDIV);
        followExprSimpl.add(Token.AND);
        followExprSimpl.add(Token.OPIGUALDADE);
        followExprSimpl.add(Token.OPMAIOR);
        followExprSimpl.add(Token.OPMENOR);
        followExprSimpl.add(Token.virgula);
        followExprSimpl.add(Token.FP);
        followExprSimpl.add(Token.IF);
        followExprSimpl.add(Token.DO);

        followSinalNOpt.add(Token.IDENTIFICADOR);
        followSinalNOpt.add(Token.NUM);
        followSinalNOpt.add(Token.NUM);
        followSinalNOpt.add(Token.AP);
        followSinalNOpt.add(Token.NOT);

        followSinalOpt.add(Token.IDENTIFICADOR);
        followSinalOpt.add(Token.NUM);
        followSinalOpt.add(Token.NUM);
        followSinalOpt.add(Token.AP);
        followSinalOpt.add(Token.NOT);

        followTermoLoop.add(Token. pontoVirgula);
        followTermoLoop.add(Token.END);
        followTermoLoop.add(Token.IF);
        followTermoLoop.add(Token.OPMUL);
        followTermoLoop.add(Token.OPDIV);
        followTermoLoop.add(Token.AND);
        followTermoLoop.add(Token.OPIGUALDADE);
        followTermoLoop.add(Token.OPMAIOR);
        followTermoLoop.add(Token.OPMENOR);
        followTermoLoop.add(Token.virgula);
        followTermoLoop.add(Token.FP);
        followTermoLoop.add(Token.IF);
        followTermoLoop.add(Token.DO);

        followTermo.add(Token. pontoVirgula);
        followTermo.add(Token.END);
        followTermo.add(Token.IF);
        followTermo.add(Token.OPMUL);
        followTermo.add(Token.OPDIV);
        followTermo.add(Token.AND);
        followTermo.add(Token.OPIGUALDADE);
        followTermo.add(Token.OPMAIOR);
        followTermo.add(Token.OPMENOR);
        followTermo.add(Token.virgula);
        followTermo.add(Token.FP);
        followTermo.add(Token.IF);
        followTermo.add(Token.DO);

        followFatorLoop.add(Token. pontoVirgula);
        followFatorLoop.add(Token.END);
        followFatorLoop.add(Token.IF);
        followFatorLoop.add(Token.OPMUL);
        followFatorLoop.add(Token.OPDIV);
        followFatorLoop.add(Token.AND);
        followFatorLoop.add(Token.OPIGUALDADE);
        followFatorLoop.add(Token.OPMAIOR);
        followFatorLoop.add(Token.OPMENOR);
        followFatorLoop.add(Token.virgula);
        followFatorLoop.add(Token.FP);
        followFatorLoop.add(Token.IF);
        followFatorLoop.add(Token.DO);

      

        followFator.add(Token. pontoVirgula);
        followFator.add(Token.END);
        followFator.add(Token.IF);
        followFator.add(Token.OPMUL);
        followFator.add(Token.OPDIV);
        followFator.add(Token.AND);
        followFator.add(Token.OPIGUALDADE);
        followFator.add(Token.OPMAIOR);
        followFator.add(Token.OPMENOR);
        followFator.add(Token.virgula);
        followFator.add(Token.FP);
        followFator.add(Token.IF);
        followFator.add(Token.DO);
        
        followExprOpt.add(Token. pontoVirgula);
        followExprOpt.add(Token.END);
        followExprOpt.add(Token.IF);
        followExprOpt.add(Token.OPMUL);
        followExprOpt.add(Token.OPDIV);
        followExprOpt.add(Token.AND);
        followExprOpt.add(Token.OPIGUALDADE);
        followExprOpt.add(Token.OPMAIOR);
        followExprOpt.add(Token.OPMENOR);
        followExprOpt.add(Token.virgula);
        followExprOpt.add(Token.FP);
        followExprOpt.add(Token.IF);
        followExprOpt.add(Token.DO);
        
        followListaExpr.add(Token.FP);
        
        followExprLoop.add(Token.FP);
        
       
    }

    public  ArrayList<Token> getFollowProgram() {
        return followProgram;
    }

    public  ArrayList<Token> getFollowBloco() {
        return followBloco;
    }

    public  ArrayList<Token> getFollowDeclVar() {
        return followDeclVar;
    }

    public ArrayList<Token> getFollowSecParamForm() {
        return followSecParamForm;
    }

    public ArrayList<Token> getFollowCmdComposto() {
        return followCmdComposto;
    }

    public ArrayList<Token> getFollowVariavel() {
        return followVariavel;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package respostas;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gabriel
 */
public class Mensagem {
    private String operacao;
    private Status status;
    
    Map<String,Object> parametro;
    
    public Mensagem(String operacao){
        this.operacao = operacao;
        parametro = new HashMap<>();        
    }
    public void setStatus(Status status){
        this.status = status;
    }
    
    public String getOperacao(){
        return operacao;
    }
    public void setParametro(String chave, Object valor){
        parametro.put(chave, valor);
    }
    public Object getParametro(String chave){
        return parametro.get(chave);
    }
    
    //Requisições
    public static final int autenticacao = 110;
    public static final int jogar = 120;
    public static final int posicionar = 130;
    public static final int prontoJogar = 140;
    public static final int coordenadas = 150;
    public static final int sair = 160;
    public static final int desconectar = 161;
    //Respostas
    public static final int autenticacaoFalha = 210;
    public static final int autenticacaoSucesso = 211;
    public static final int jogarFalha = 220;
    public static final int jogarSucesso = 221;
    public static final int jogarAguardar = 222;
    public static final int posicionarFalha = 230;
    public static final int posicionarSucesso = 231;
    public static final int posicionarErro = 232;
    public static final int prontoJogarFalha = 240;
    public static final int prontoJogarSucesso = 241;
    public static final int prontoJogarErro = 242;
    public static final int prontoJOgarAguarde = 243;
    public static final int coordenadasFalha = 250;
    public static final int coordenadasSucesso = 251;
    public static final int coordenadasErro = 252;
    public static final int coordendasVencedor = 253;
    public static final int coordendasTempo = 254;
    public static final int sairFalha = 260;
    public static final int sairSucesso = 261;
   
    
}

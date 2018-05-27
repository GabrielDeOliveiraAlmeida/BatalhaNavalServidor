/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import respostas.Mensagem;
import respostas.Status;
import servidor.Servidor;

/**
 *
 * @author gabriel
 */
public class TrataCliente implements Runnable {
    private Socket socket;

    private Almirante cliente;
    private Servidor servidor;
    int tiros;
    

    public TrataCliente(Socket socket, Almirante cliente, Servidor servidor){
        this.socket = socket;
        this.cliente = cliente;
        this.servidor = servidor;
    }
        
    @Override
    public void run(){
        System.out.println("Nova conexao com o cliente: " + this.socket.getInetAddress().getHostAddress());
        
        Integer code = 0;
      //  Scanner scan = null;
        while ( Mensagem.desconectar != code ) {
            code = cliente.readInt();
            interpretador(code);
            //code = null;
       
         }
      //  desconectar();
        
    }
        
    private void fila(){
        int code;
        this.cliente.setStatus(Status.FILA);
        cliente.writeInt(Mensagem.jogarAguardar);
        Almirante cliente;
        cliente= servidor.fila(this.cliente, Status.FILA);
        if(cliente != null){
            cliente.setStatus(Status.POSICIONAR);
            this.cliente.setStatus(Status.POSICIONAR);
            cliente.writeInt(Mensagem.jogarSucesso);
            this.cliente.writeInt(Mensagem.jogarSucesso);
            servidor.addSala(this.cliente, cliente, "sala1");
            this.cliente.setSala("sala1");
            cliente.setSala("sala1");
        }
       
    }
   
    private void interpretador(int codigo) {
        switch (codigo) {
            case Mensagem.autenticacao:
                System.out.println("Mensagem de AUTENTICACAO recebida de: " + this.socket.getInetAddress().getHostAddress());
                break;
            case Mensagem.jogar:
                System.out.println("Mensagem para JOGAR recebida de: " + this.socket.getInetAddress().getHostAddress());
                fila();
                break;
            case Mensagem.desconectar:
                System.out.println("Mensagem Para Desconectar recebida de: " + this.socket.getInetAddress().getHostAddress());
                desconectar();
                break;
            case Mensagem.posicionar:
                System.out.println("Mensagem Para Posicionar recebida de: " + this.socket.getInetAddress().getHostAddress());
                apontar();
                break;
            case Mensagem.prontoJogar:
                System.out.println("Mensagem Tô Pronto recebida de: " + this.socket.getInetAddress().getHostAddress());
                toPronto();
                break;
            case Mensagem.coordenadas:
                System.out.println("Mensagem Para Coordenadas recebida de: " + this.socket.getInetAddress().getHostAddress());
                fogo();
                break;
            case Mensagem.jogarAguardar:
                verificarVez();
            default:
                System.out.println(codigo);
                
        }
    }
        
    private void desconectar(){
        try {
            cliente.writeInt(Mensagem.sairSucesso);

        } catch (Exception ex) {

            try {
                cliente.writeInt(Mensagem.sairFalha);
            } catch (Exception ex1) {
                System.out.println("ERRO");
            }
        }
        cliente = null;
    }
    
    private void apontar(){
        int tipo = cliente.readInt();
        System.out.println("Tipo = " + tipo);
        //cliente.writeInt(Mensagem.coordenadasSucesso)
        int x = cliente.readInt();
        System.out.print(" x = " + x);
        //cliente.writeInt(Mensagem.coordenadasSucesso);
        int y = cliente.readInt();
        System.out.print(" y = "+y);
        cliente.writeInt(Mensagem.posicionarSucesso);
        Sala sala = servidor.procurarSala(cliente.getSala());
        if(sala != null)
        sala.receberPosicao(this.cliente, tipo,x,y);
        
    }
    
    private void toPronto(){
        cliente.writeInt(Mensagem.prontoJogarSucesso);
        cliente.setStatus(Status.PRONTO);
        Almirante cliente;
        cliente= servidor.fila(this.cliente, Status.PRONTO);
        if(cliente != null){
            //System.out.println("ACHEI");
            cliente.setStatus(Status.MINHAVEZ);
            this.cliente.setStatus(Status.JOGAR);
            cliente.writeInt(Mensagem.jogar);
            this.cliente.writeInt(Mensagem.jogar);
        }
    }
    
    
    private void verificarVez(){
        if(cliente.getStatus() != Status.MINHAVEZ){
            cliente.writeInt(Mensagem.jogarAguardar);
        }else{
            cliente.writeInt(Mensagem.jogar);
        }
    }
    private void fogo(){
        
        int x = cliente.readInt();
        int y = cliente.readInt();
        Sala sala = servidor.procurarSala(cliente.getSala());
        if(sala == null) System.out.println("sala é NULL");
        if(sala.fogo(cliente, x, y)){
            if(tiros==2){
                cliente.writeInt(Mensagem.coordendasVencedor);
            }else{
                tiros++;
                cliente.writeInt(Mensagem.coordenadasSucesso);
            }
        }else{
            cliente.writeInt(Mensagem.coordenadasFalha);
        }
    }
    
}

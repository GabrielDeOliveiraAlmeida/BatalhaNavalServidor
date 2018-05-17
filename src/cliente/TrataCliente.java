/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
        while ( Mensagem.desconectar != code ) {//Melhorar esse while

            code = cliente.readInt();
            if(code != null){
                interpretador(code);
           }else{
               break;
            }
            code = null;
        }
        desconectar();
        
    }
        
    private void fila(){
        int code;
        this.cliente.setStatus(Status.FILA);
        cliente.writeInt(Mensagem.jogarAguardar);
        Almirante cliente;
            cliente= servidor.fila(this.cliente);
            if(cliente != null){
                cliente.setStatus(Status.POSICIONAR);
                this.cliente.setStatus(Status.POSICIONAR);
                this.cliente.writeInt(Mensagem.jogarSucesso);
                cliente.writeInt(Mensagem.jogarSucesso);
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
}

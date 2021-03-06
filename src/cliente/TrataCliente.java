/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.net.Socket;
import respostas.Mensagem;
import respostas.Status;
import servidor.Servidor;

/**
 *
 * @author gabriel
 */
public class TrataCliente implements Runnable{
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
        while (true) {
            try{
            code = cliente.readInt();
            if(code.equals(Mensagem.desconectar)){
                desconectar();
                break;
            }
            interpretador(code);
            }catch(Exception e){
                break;
            }
         }
        
        
    }
        
    private void fila(){
        int code;
        String idSala;
        this.cliente.setStatus(Status.FILA);
        //cliente.writeInt(Mensagem.fila);
        Almirante cliente;
        
        cliente= servidor.fila(this.cliente, Status.FILA);
        if(cliente != null){
            cliente.setStatus(Status.POSICIONAR);
            this.cliente.setStatus(Status.POSICIONAR);
            cliente.writeInt(Mensagem.filaSucesso);
            this.cliente.writeInt(Mensagem.filaSucesso);
            idSala = cliente.getNome() + cliente.getNome();
            Sala s = new Sala(this.cliente,cliente, idSala);
            this.cliente.setSala(s);
            cliente.setSala(s);
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
            case Mensagem.verificarTurno:
                verificarVez();
                break;
            case Mensagem.sair:
                sair();
                break;
            case Mensagem.abandonar:
                System.out.println("Mensagem Para Abandonar Partida recebida de: " + this.socket.getInetAddress().getHostAddress());
                abandonarPartida();
                break;
            default:
                System.out.println(codigo);
                
        }
    }
        
    private void sair(){
        //sair da fila.
        cliente.setStatus(Status.CONECTADO);
        cliente.setSala(null);
    }
    
    private void abandonarPartida(){
        cliente.setStatus(Status.CONECTADO);
        cliente.getSala().abandonarPartida(cliente);
    }
    private void desconectar(){
        try {
            //fechar o jogo
            cliente.desconectar();
            servidor.removerCliente(cliente);

        } catch (Exception ex) {

            try {
            } catch (Exception ex1) {
                System.out.println("ERRO");
            }
        }
        cliente = null;
    }
    
    private void apontar(){
        int tipo = cliente.readInt();
        System.out.println("Tipo = " + tipo);
        int x = cliente.readInt();
        System.out.print(" x = " + x);
        int y = cliente.readInt();
        System.out.print(" y = "+y);
        cliente.getSala().receberPosicao(this.cliente, tipo,x,y);
        
    }
    
    private void toPronto(){
        cliente.setStatus(Status.PRONTO);
        Almirante cliente= servidor.fila(this.cliente, Status.PRONTO);
        if(cliente != null){
            //System.out.println("ACHEI");
            cliente.setStatus(Status.MINHAVEZ);
             this.cliente.setStatus(Status.JOGAR);
            cliente.writeInt(Mensagem.prontoJogarSucesso);
            this.cliente.writeInt(Mensagem.prontoJogarSucesso);
        }
    }
    
    
    private void verificarVez(){
        if(cliente.getStatus() != Status.MINHAVEZ){
            cliente.writeInt(Mensagem.jogarAguardar);
        }else{
            cliente.writeInt(Mensagem.jogarSeuTurno);
        }
    }
    private void fogo(){
        
        int x = cliente.readInt();
        int y = cliente.readInt();
        System.out.println(x +" , " + y);
        if(cliente.getSala().fogo(cliente, x, y)){
            cliente.getSala().incTiros(this.cliente);
            if(cliente.getSala().getTiros(this.cliente)==Mensagem.NUMEROPOSTOTAL){
                cliente.getSala().getCliente2(this.cliente).writeInt(Mensagem.fracassado);
                cliente.writeInt(Mensagem.vencedor);
            }else{
                cliente.getSala().getCliente2(this.cliente).writeInt(Mensagem.atingido);
                cliente.writeInt(Mensagem.coordenadasSucesso);
            }
        }
    }
    
}

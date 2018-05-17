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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import respostas.Status;

/**
 *
 * @author gabriel
 */
public class Almirante {
    private Socket cliente;
    private String ip;
    private String nome;
    private Status status;
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    
    public Almirante(Socket socket){
        cliente = socket;
        status = Status.CONECTADO;
        ip = socket.getInetAddress().getHostAddress();
        nome = "jogador " + String.valueOf( Math.random());
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            saida = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Almirante.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public int readInt(){
        try {
            return entrada.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Almirante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
//    public Object readObject(){
//        try {
//            return entrada.rreadObject();
//        } catch (ClassNotFoundException | IOException ex) {
//                Logger.getLogger(Almirante.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    public void writeInt(int m){
        try {
            saida.writeInt(m);
        } catch (IOException ex) {
            Logger.getLogger(Almirante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean desconectar(){
        
        try{
            entrada.close();
            saida.close();
            return true;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        
    }
    public int hasObject(Scanner scan){
        scan = new Scanner(entrada);
        if(scan.hasNext()){
            if(scan.hasNextInt()) return 1;
            else return 2;
        }
        else return 0;
    } 
    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}

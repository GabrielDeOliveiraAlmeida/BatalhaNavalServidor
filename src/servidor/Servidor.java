/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;


import cliente.Almirante;
import cliente.TrataCliente;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import respostas.Status;

/**
 *
 * @author gabriel
 */
public class Servidor{
    /**
     * @param args the command line arguments
     */
        private int porta;
        
        private ArrayList<Almirante> jog;
        
        
    public Servidor(int socket){
        porta = socket;
        jog = new ArrayList<>();
    }
    
   

    public void executar(){
      try{

        ServerSocket serverSocket = new ServerSocket(porta);
        while(true){   
            System.out.println("Aguardando conexão...");
            Socket socket  = serverSocket.accept();

            Almirante almirante = new Almirante(socket);
            jog.add(almirante);
            TrataCliente s = new TrataCliente(socket, almirante, this);
            Thread t = new Thread(s);
            t.start();
            
           
        }


        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    
    public Almirante fila(Almirante mim){
        int tam=jog.size();
        for(int i=0; i<tam; i++)
        {
            if(jog.get(i).getStatus() == Status.FILA && jog.get(i) != mim){
                return jog.get(i);
            }
        }
        return null;
    }
    
    public static void main(String[] args)  {
        // TODO code application logic here
        new Servidor(12345).executar();
    }
    
}

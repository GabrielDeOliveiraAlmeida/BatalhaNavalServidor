/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.util.ArrayList;
import respostas.Mensagem;
import respostas.Status;

/**
 *
 * @author gabriel
 */
public class Sala {
    private Almirante cliente1, cliente2;
    //private int[][] matriz1, matriz2;
    private String id;
    private ArrayList<Navio> posicao1;
    private ArrayList<Navio> posicao2;

    public String getId() {
        return id;
    }
    
    
    public Sala(Almirante cliente1, Almirante cliente2,String id){
        this.cliente1 = cliente1;
        this.cliente2 = cliente2;
        this.id = id;
        posicao1 = new ArrayList<>();
        posicao2 = new ArrayList<>();
    }
    
    
    public void receberPosicao(Almirante cliente, int tipo, int x, int y){
        Navio barco = new Navio(x, y, tipo);
        if(cliente.equals(cliente1)){
            posicao1.add(barco);
        }else{
            posicao2.add(barco);
        }
    }
    
    public Boolean fogo(Almirante cliente, int x, int y){
        Boolean aux;
        if(cliente.equals(cliente1)){
            aux = tratarBarcos(posicao2, x,y);
            if(!aux){
                cliente1.setStatus(Status.JOGAR);
                cliente2.setStatus(Status.MINHAVEZ);
            }
        }else{
            aux = tratarBarcos(posicao1, x,y);
            if(!aux){
                cliente2.setStatus(Status.JOGAR);
                cliente1.setStatus(Status.MINHAVEZ);
            }
        }
        return aux;
    }
    
    private Navio procurarNavio(Navio barco,ArrayList<Navio> array){
        for(Navio n : array){
            if(barco.equals(n)){
                return n;
            }
        }
        return null;
        
    }
    
    private Boolean tratarBarcos(ArrayList<Navio> array, int x, int y){
        for(Navio n : array){
            switch(n.getTipo()){
                case 0:
                    if(x == n.getX() && y == n.getY()){
                        return true;
                    }
                    break;
                case 1:
                    for(int i=0; i<2; i++){
                        if(x == n.getX() && y == n.getY()+i){
                            return true;
                        }    
                    }
                    break;
                case 2:
                    for(int i=0; i<3; i++){
                        if(x == n.getX() && y == n.getY()+i){
                            return true;
                        }    
                    }
                    break;
                case 3:
                    for(int i=0; i<4; i++){
                        if(x == n.getX() && y == n.getY()+i){
                            return true;
                        }    
                    }
                    break;
            }
        }
        return false;
    }
    
    
}

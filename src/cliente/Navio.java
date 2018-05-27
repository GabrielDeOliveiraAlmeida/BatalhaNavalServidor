/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author gabriel
 */
public class Navio {
    private int x, y, tipo;

    public Navio(int x, int y, int tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
    
}

package Tablero2;


import java.awt.*;
import javax.swing.*;
public class Casillas {
    
    JLabel label;
    Fantasmas personajeActual;
    int row;
    int column;
    boolean selected = false;
    
    
   
    public Casillas(int row, int column, Fantasmas personajeActual) {        
        this.label = new JLabel();
        this.row = row;
        this.column = column;
        this.personajeActual = personajeActual;
        
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
       
    public void highlightMove(boolean activar) {
        if (activar) {
            label.setBackground(Color.green);
            label.setOpaque(true);
        } else label.setOpaque(false);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    public void setPersonaje(Fantasmas personaje) {
        personajeActual = personaje;
        
        if (personaje == null) {
            label.setText("");
            label.setIcon(null);
        } else {            
             if (personajeActual.icono != null) {
                label.setIcon(personajeActual.icono);
            }
             else {
                label.setText(personajeActual.TipoFantasma);
            }
        }        
    }
    
    public void esconderCasilla(boolean esconder) {
        if (esconder) {
            if (personajeActual.iconoEscondido != null) {
                label.setIcon(personajeActual.iconoEscondido);
                label.repaint();
            } else {
                label.setIcon(null);
                label.setText("???");
            }  
        } else {
            if (personajeActual.icono != null) {
                label.setIcon(personajeActual.icono);
            }
            else
                label.setText(personajeActual.TipoFantasma);
        }
    } 
    
   
    
    
    
}


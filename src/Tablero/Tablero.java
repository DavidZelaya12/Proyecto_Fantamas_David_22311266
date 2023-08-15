/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tablero;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;
import java.lang.Math;

public class Tablero extends JFrame {

   
    final int ROWS = 6;
    final int COLUMNS = 6;
    int numerosColocados = 0;

    Casilla tablero[][];
    JPanel panelTablero;

    public Tablero() {
    }
    
    public static void main(String[] args) {
        new Tablero();
    }
}

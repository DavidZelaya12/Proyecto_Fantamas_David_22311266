package Proyecto;

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;

public class Funciones {

    private Player[] players;
    private int tamaño;

    public Funciones() {
        players = new Player[100];
        tamaño = 0;
    }

    public Player buscarplayer(String usuario, int i) {
        Player jugador = players[i];

        if (i > tamaño) {
            return null;
        }

        if (players[i] != null) {

            if (jugador.getUser().equals(usuario)) {
                return jugador;
            }
        }

        return buscarplayer(usuario, i + 1);

    }

    public boolean Agregarpersona(Player player) {
        Player valido = buscarplayer(player.getUser(), 0);

        if (valido == null) {
            players[tamaño] = player;
            tamaño++;
            return true;
        }

        return false;
    }

    public boolean BuscarOponente(String usuario, int i) {
        Player jugador = players[i];

        if (i > tamaño) {
            return false;
        }

        if (players[i] != null) {
            if (jugador.getUser().equals(usuario)) {
                return true;
            }

        }

        return BuscarOponente(usuario, i + 1);
    }

    public String[] ranking() {
        String[] codigos = new String[tamaño];

        for (int i = 0; i < tamaño; i++) {
            if (!players[i].getUser().equals("                 ")) {
                codigos[i] = players[i].getUser() + "                                                   " + players[i].getPuntos() + " puntos";
            }
        }
        for (int i = 0; i < tamaño - 1; i++) {
            for (int j = 0; j < tamaño - i - 1; j++) {
                if (players[j].getPuntos() < players[j + 1].getPuntos()) {
                    String temp = codigos[j];
                    codigos[j] = codigos[j + 1];
                    codigos[j + 1] = temp;
                    Player tempPlayer = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = tempPlayer;
                }
            }
        }

        return codigos;
    }

    public void puntos(int i, String Ganador) {

        Player jugador = players[i];

        if (players[i] != null) {

            if (jugador.getUser().equals(Ganador)) {
                jugador.setpuntos(3);

            }
        }
        if (i < tamaño) {
            puntos(i + 1, Ganador);
        }
    }
    
      public void fechas(int i, String p1,String p2,String partida1,String partida2) {

        Player jugador = players[i];

        if (players[i] != null) {
            if (jugador.getUser().equals(p1)) {
                jugador.agregarFecha(partida1);
            }   
            if(jugador.getUser().equals(p2)){
                jugador.agregarFecha(partida2);
            }
            
        }
        if (i < tamaño) {
            fechas(i + 1, p1,p2,partida1,partida2);
        }
    }  

}

//fin


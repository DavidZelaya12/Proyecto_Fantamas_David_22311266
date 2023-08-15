package Proyecto;

public class Player {
    
    private String username;
    private String contra;
    private int puntos;
    private String[] partidas;
    private String oponente;
    private static int dificultad=2;
    private static boolean random=true;
    
public Player(String user,String password){
    this.username=user;
    this.contra=password;
    this.partidas= new String[0];
    this.puntos=0;
}

public void setModo(boolean nose){
    this.random=nose;
}


public static boolean getrandom(){
    return random;
}

public static int getdificultad(){
    return dificultad;
}

public  void setDificultad(int num){
    this.dificultad=num;
}

public void setNombre(String name){
    this.username=name;
}

public void setpuntos(int points){
    this.puntos+=points;
}

public void setOponente(String p2){
    oponente=p2;
}
public String getOponente(){
    return oponente;
}

public void setpasswod(String newpass){
    this.contra=newpass;
}

public String getUser(){
    return username;
}

public String getContra(){
    return contra;
}

public int getPuntos(){
    return puntos;
}

    public void agregarFecha(String batalla) {
        String[] nuevoArreglo = new String[partidas.length + 1];
        System.arraycopy(partidas, 0, nuevoArreglo, 0, partidas.length);
        nuevoArreglo[partidas.length] = batalla;
        partidas = nuevoArreglo;
    }
    
public String[] getPartidas() {
    String[] reversedPartidas = new String[partidas.length];
    for (int i = 0; i < partidas.length; i++) {
        reversedPartidas[i] = partidas[partidas.length - i - 1];
    }
    return reversedPartidas;
}
}

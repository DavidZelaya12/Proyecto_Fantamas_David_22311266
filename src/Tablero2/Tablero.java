package Tablero2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Proyecto.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Tablero extends JPanel {

    //Atributos
    Player player;
    Main main;
    Juego juego;

    ArrayList<Fantasmas> FantasmasP1 = Fantasmas.FantasmasP1();
    ArrayList<Fantasmas> FantasmasP2 = Fantasmas.FantasmasP2();
    private JTextArea txtAreaEliminados;
    private JLabel Turnos;
    private boolean SeguirJugando = true;
    private boolean SeSeleccionoCasilla = false;
    private Casillas casillaSeleccionada;
    private Casillas[][] casillas;
    private boolean turnoHeroes = true;
    private int FantasmasBuenosP1;
    private int FantasmasBuenosP2;
    private int FantasmasMalosP2;
    private int FantasmasMalosP1;
    private int comidosp1 = 0;
    private int comidosp2 = 0;
    private int ConfirmarEsHeroe;
    private boolean HayGanador = false;
    public static boolean P1;
    private boolean sacar = false;
    private int victoria = 0;
    private boolean invalido = false;
    private Image tablero;
    private int sacados = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar la imagen de fondo
        g.drawImage(tablero, 0, 0, getWidth(), getHeight(), this);
    }

    public Tablero(Player player, Main main, JTextArea txtAreaEliminados, JLabel Turnos, Juego juego) {

//        ImageIcon fondo = new ImageIcon("src/imagenes/tablero.png");
        FantasmasBuenosP1 = Player.getdificultad() / 2;
        FantasmasBuenosP2 = Player.getdificultad() / 2;

        FantasmasMalosP1 = Player.getdificultad() / 2;
        FantasmasMalosP2 = Player.getdificultad() / 2;

        this.player = player;
        this.main = main;
        this.txtAreaEliminados = txtAreaEliminados;
        this.Turnos = Turnos;
        this.juego = juego;
        setLayout(new GridLayout(6, 6));

        casillas = new Casillas[6][6];
        for (int filas = 0; filas < 6; filas++) {
            for (int columnas = 0; columnas < 6; columnas++) {
                Casillas ficha = new Casillas(filas, columnas, null);
                casillas[filas][columnas] = ficha;
                add(casillas[filas][columnas].label);
            }
        }

        //Agregar evento para aceptar los clics del mouse en el tablero
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                int row = -1, column = -1;  //valores negativos en caso de que no se seleccione la casilla correcta

                //Si se selecciona una casilla con un personaje
                if (SeSeleccionoCasilla == false) {
                    //Se obtienen las coordenadas de la casilla
                    for (int i = 0; i < 6; i++) {
                        for (int j = 0; j < 6; j++) {
                            // La casilla seleccionada es de la casilla actual
                            if (casillas[i][j].label == label) {
                                casillaSeleccionada = casillas[i][j];
                                ////Comprobar que la casilla contnga un personaje y sea el turno correcto
                                if (casillaSeleccionada.personajeActual != null && casillaSeleccionada.personajeActual.p1 == turnoHeroes) {
                                    SeSeleccionoCasilla = true;
                                    //resaltarZonasProhibidas();
                                    break;
                                } else {
                                    casillaSeleccionada = null;
                                    SeSeleccionoCasilla = false;
                                    break;
                                }
                            }
                        }
                    }
                    //si ya se selecciono una casilla y se quiere mover a otro puesto
                } else {
                    for (int filas = 0; filas < 6; filas++) {
                        for (int columnas = 0; columnas < 6; columnas++) {
                            if (casillas[filas][columnas].label == label) {
                                // Si se selecciona una  ficha del mismo bando se cambia a esa ficha 
                                if (casillas[filas][columnas].personajeActual != null) {
                                    if (casillas[filas][columnas].personajeActual.p1 == turnoHeroes) {
                                        // Actualizar casillas  
                                        //borrarResaltadoMovimientos();
                                        casillaSeleccionada = casillas[filas][columnas];
                                        //resaltarZonasProhibidas();
                                        break;
                                    }
                                }
                                //si se comprobo que el movimiento es valido se mueve a esa casilla seleccionada
                                if (ComprobarMovimientoValido(filas, columnas) == true) {
                                    moverPersonaje(filas, columnas);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error: Movimiento invalido");
                                }
                            }

                        }
                    }
                }
            }
        };

        // Agregar el MouseListener a las casillas
        for (int filas = 0; filas < 6; filas++) {
            for (int columnas = 0; columnas < 6; columnas++) {
                casillas[filas][columnas].label.addMouseListener(mouseAdapter);
            }
        }

    }

    private boolean ComprobarMovimientoValido(int fila, int columna) {
        int FilaActual = casillaSeleccionada.row;
        int ColumnaActual = casillaSeleccionada.column;

        //si se selecciona una bomba o la tierra no se pueden mover
        if (casillaSeleccionada.personajeActual.lvlfantasma == 0) {
            return false;
        }

        if (casillaSeleccionada.personajeActual.lvlfantasma == 1 || casillaSeleccionada.personajeActual.lvlfantasma == 3 || casillaSeleccionada.personajeActual.lvlfantasma == 2) {
            // Verificar si el movimiento es ortogonal
            boolean EsOrtogonal = (fila == FilaActual && Math.abs(columna - ColumnaActual) == 1)
                    || (columna == ColumnaActual && Math.abs(fila - FilaActual) == 1);

            boolean FichaEstaOcupada = (casillas[fila][columna].personajeActual != null);
            
            if (FichaEstaOcupada == true) {
                FichaEstaOcupada = (casillas[fila][columna].personajeActual.p1 == casillaSeleccionada.personajeActual.p1);
            }            
            return (EsOrtogonal == true) && (FichaEstaOcupada == false);
        }
        return false;
    }

    private void esconderPersonajesBandoOpuesto() {
        for (int filas = 0; filas < 6; filas++) {
            for (int columnas = 0; columnas < 6; columnas++) {
                if (casillas[filas][columnas].personajeActual != null && casillas[filas][columnas].personajeActual.lvlfantasma != -1 && casillas[filas][columnas].personajeActual.lvlfantasma != -2
                        && casillas[filas][columnas].personajeActual.lvlfantasma != -4 && casillas[filas][columnas].personajeActual.lvlfantasma != -5) {
                    casillas[filas][columnas].esconderCasilla(turnoHeroes != casillas[filas][columnas].personajeActual.p1);
                }
            }
        }
    }

    private void moverPersonaje(int filanueva, int columananueva) {
        String TurnoDeUsuario = Turnos.getText();
    
        if (casillas[filanueva][columananueva].personajeActual != null) {

            String comido = casillas[filanueva][columananueva].personajeActual.toString();
            Fantasmas ganador = EmpezarBatalla(casillaSeleccionada.personajeActual, casillas[filanueva][columananueva].personajeActual, comido);

            if (ganador == null) {
                casillaSeleccionada.setPersonaje(null);
                casillas[filanueva][columananueva].setPersonaje(ganador);
            } else if (casillaSeleccionada.personajeActual == ganador) {
                casillaSeleccionada.setPersonaje(null);
                casillas[filanueva][columananueva].setPersonaje(ganador);
            } else {
                casillaSeleccionada.setPersonaje(null);
            }
            CambiarTurno();

            return;

        }
        Fantasmas personaje = casillaSeleccionada.personajeActual;
        casillaSeleccionada.setPersonaje(null);
        casillas[filanueva][columananueva].setPersonaje(personaje);
        CambiarTurno();

    }

    public Fantasmas EmpezarBatalla(Fantasmas atacante, Fantasmas defensor, String comido) {

        //Fantasmas buenos y malos se comen entre si
        if (atacante.lvlfantasma == 1 && defensor.lvlfantasma == 1) {
            if (defensor.p1 == true) {
                if (comido.equals("FantasmaBueno")) {
                    FantasmasBuenosP1--;
                } else {
                    FantasmasMalosP1--;
                    //comidosp1++;
                }
            }
            if (defensor.p1 == false) {
                if (comido.equals("FantasmaBueno")) {
                    FantasmasBuenosP2--;
                } else {
                    FantasmasMalosP2--;
                    //comidosp2++;
                }
            }
            JOptionPane.showMessageDialog(null, "Tu " + atacante.TipoFantasma + " se ha comido un " + defensor.TipoFantasma);
            return atacante;
        }

        //Fantasmas falsos intentan comer reales
        if (atacante.lvlfantasma == 3 && defensor.lvlfantasma == 1) {
            JOptionPane.showMessageDialog(null, "Tu " + atacante.TipoFantasma + " intento comerse un " + defensor.TipoFantasma);
            return defensor;
        }

        //Fantasmas Reales se comen un falso
        if (atacante.lvlfantasma == 1 && defensor.lvlfantasma == 3) {
            JOptionPane.showMessageDialog(null, "Tu " + atacante.TipoFantasma + " se ha comido un " + defensor.TipoFantasma);
            return atacante;
        }

        //Fantasmas falsos se comen entre si
        if (atacante.lvlfantasma == 3 && defensor.lvlfantasma == 3) {
            JOptionPane.showMessageDialog(null, "Tu " + atacante.TipoFantasma + " se ha comido un " + defensor.TipoFantasma);
            return atacante;
        }

        //Fantasmas buenos salen del castillo
        if (atacante.TipoFantasma.equals("FantasmaBueno") && comido.equals("castillop1")) {
            sacar = true;
            victoria = 2;
            return defensor;
        }
        if (atacante.TipoFantasma.equals("FantasmaBueno") && comido.equals("castillop2")) {
            sacar = true;
            victoria = 1;
            return defensor;
        }

        //Fantasmas falsos y malos salen del castillo
        if (atacante.TipoFantasma.equals("FantasmaFalso") || atacante.TipoFantasma.equals("FantasmaMalo")
                && comido.equals("castillop2") || comido.equals("castillop1")) {

            if (atacante.TipoFantasma.equals("FantasmaMalo") && atacante.p1 == true) {
                FantasmasMalosP1--;
            }
            if (atacante.TipoFantasma.equals("FantasmaMalo") && atacante.p1 == false) {
                FantasmasMalosP2--;
            }

            JOptionPane.showMessageDialog(null, "Sacaste un " + atacante.TipoFantasma + " del castillo");
            sacar = false;
            return defensor;
        } else {
            return defensor;
        }
    }

    public void CambiarTurno() {
        int confirmacion;
        casillaSeleccionada = null;
        SeSeleccionoCasilla = false;
        turnoHeroes = !turnoHeroes;
        setVisible(false);
        esconderPersonajesBandoOpuesto();

        txtAreaEliminados.setText("");

        if (sacar == true || FantasmasBuenosP1 == 0 || FantasmasBuenosP2 == 0 || FantasmasMalosP1 == 0 || FantasmasMalosP2 == 0) {
            Win(false);
        } else {
            if (SeguirJugando == true) {
                if (turnoHeroes == true) {
                    if (ConfirmarEsHeroe == JOptionPane.YES_OPTION) {
                        Turnos.setText("Turno de: " + player.getUser());
                        JOptionPane.showMessageDialog(null, "Turno de: " + player.getUser());
                    } else {
                        Turnos.setText("Turno de: " + player.getOponente());
                        JOptionPane.showMessageDialog(null, "Turno de: " + player.getOponente());
                    }
                } else {
                    if (ConfirmarEsHeroe == JOptionPane.NO_OPTION) {
                        Turnos.setText("Turno de: " + player.getUser());
                        JOptionPane.showMessageDialog(null, "Turno de: " + player.getUser());
                    } else {
                        Turnos.setText("Turno de: " + player.getOponente());
                        JOptionPane.showMessageDialog(null, "Turno de: " + player.getOponente());
                    }
                }
            }

            setVisible(true);
            mostrarPersonajesEliminados();
        }
    }

    public void mostrarPersonajesEliminados() {
        String mensaje = "";
        
            mensaje = "\n"+player.getUser()
                    + "\nFantasmas buenos restantes: " + FantasmasBuenosP1
                    + "\nFantasmas Malos Restantes: " + FantasmasMalosP1
                    + "\n "
                    + "\n"+player.getOponente()
                    + "\nFantasmas buenos restantes: " + FantasmasBuenosP2
                    + "\nFantasmas Malos Restantes: " + FantasmasMalosP2;

        txtAreaEliminados.setText(mensaje);
    }

    public void ColocarEsquinas() {

        for (int i = 0; i < FantasmasP1.toArray().length; i++) {

            Fantasmas personajeActual = FantasmasP1.get(i);
            if (personajeActual.posicionado) {
                continue;
            }

            // Posicionar 
            if (personajeActual.lvlfantasma == -1) {
                casillas[5][0].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }

            if (personajeActual.lvlfantasma == -2) {
                casillas[5][5].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }

        }
        for (int i = 0; i < FantasmasP2.toArray().length; i++) {
            Fantasmas personajeActual = FantasmasP2.get(i);

            if (personajeActual.posicionado) {
                continue;
            }

            if (personajeActual.lvlfantasma == -4) {
                casillas[0][0].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }

            if (personajeActual.lvlfantasma == -5) {
                casillas[0][5].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }

        }
    }

    public void TerminardeCargar() {
        ColocarEsquinas();
        setVisible(true);
        mostrarPersonajesEliminados();
        Turnos.setText("Turno de: " + player.getUser());
    }

    public void PosicionarFantasmas() {
        Random random = new Random();

        if (Player.getrandom() == true) {
            for (int i = 0; i < FantasmasP1.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP1.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 1) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 4;
                    filas[1] = 5;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);
                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }

            for (int i = 0; i < FantasmasP2.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP2.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 1) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 0;
                    filas[1] = 1;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);

                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }
        }

        if (Player.getrandom() == false) {

            //posicionar p1
            for (int i = 0; i < FantasmasP1.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP1.get(i);
                int columna = 0;
                int fila = 0;
                if (personajeActual.lvlfantasma == 1) {

                    do {
                        do {
                            try {
                                columna = Integer.parseInt(JOptionPane.showInputDialog(
                                        "Ingrese la columna en la que desea colocar un " + personajeActual.TipoFantasma + " (0-5)"));

                                if (columna < 0 || columna > 5) {
                                    JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                }

                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                columna = -1;
                            }
                        } while (columna < 0 || columna > 5);

                        do {
                            try {
                                fila = Integer.parseInt(JOptionPane.showInputDialog(
                                        "Ingrese la fila en la que desea colocar un " + personajeActual.TipoFantasma + " (4-5)"));

                                if (fila < 0 || fila > 5) {
                                    JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                }

                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                fila = -1;
                            }

                        } while (fila < 4 || fila > 5);

                        if (casillas[fila][columna].personajeActual != null) {
                            JOptionPane.showMessageDialog(null, "Error: esta casilla ya se encuentra ocupada");
                        }

                    } while (casillas[fila][columna].personajeActual != null);
                    JOptionPane.showMessageDialog(null, "Fantasma posicionado exitosamente!");
                    casillas[fila][columna].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }

            CambiarTurno();
            //posicionar p2
            for (int i = 0; i < FantasmasP2.toArray().length; i++) {
                int columna = 0;
                int fila = 0;
                Fantasmas personajeActual = FantasmasP2.get(i);

                if (personajeActual.lvlfantasma == 1) {

                    do {
                        do {
                           try {
                                columna = Integer.parseInt(JOptionPane.showInputDialog(
                                        "Ingrese la columna en la que desea colocar un " + personajeActual.TipoFantasma + " (0-5)"));

                                if (columna < 0 || columna > 5) {
                                    JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                }

                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                columna = -1;
                            } } while (columna < 0 || columna > 5);

                        do {
                            try {
                                fila = Integer.parseInt(JOptionPane.showInputDialog(
                                        "Ingrese la fila en la que desea colocar un " + personajeActual.TipoFantasma + " (4-5)"));

                                if (fila < 0 || fila > 5) {
                                    JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                }

                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Ingresa un valor numérico válido.");
                                fila = -1;
                            }

                        } while (fila < 0 || fila > 2);

                        if (casillas[fila][columna].personajeActual != null) {
                            JOptionPane.showMessageDialog(null, "Error: esta casilla ya se encuentra ocupada");
                        }

                    } while (casillas[fila][columna].personajeActual != null);
                    JOptionPane.showMessageDialog(null, "Fantasma posicionado exitosamente!");
                    casillas[fila][columna].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }
            CambiarTurno();
        }

        //Posiciona fantasmas falsos si esta en Genius
        if (Player.getdificultad() == 2) {
            for (int i = 0; i < FantasmasP1.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP1.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 3) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 4;
                    filas[1] = 5;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);
                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }

            for (int i = 0; i < FantasmasP2.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP2.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 3) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 0;
                    filas[1] = 1;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);

                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }
        }

        //Posiciona fantasmas falsos si esta en Genius
        if (Player.getdificultad() == 2) {
            for (int i = 0; i < FantasmasP1.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP1.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 3) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 4;
                    filas[1] = 5;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);
                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }

            for (int i = 0; i < FantasmasP2.toArray().length; i++) {
                Fantasmas personajeActual = FantasmasP2.get(i);
                int columnaAleatoria;
                int filaAleatoria;

                if (personajeActual.lvlfantasma == 3) {
                    // Elegir entre fila 6 y 7

                    int filas[] = new int[2];
                    filas[0] = 0;
                    filas[1] = 1;

                    // Elegir columna aleatoria hasta que este libre ese espacio
                    do {
                        filaAleatoria = filas[random.nextInt(0, 2)];
                        columnaAleatoria = random.nextInt(0, 6);
                    } while (casillas[filaAleatoria][columnaAleatoria].personajeActual != null);

                    casillas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                    personajeActual.posicionado = true;
                }
            }
        }

        esconderPersonajesBandoOpuesto();
    }

    public void Win(boolean rendirse) {
        String ganador = "";
        String batalla1 = "";
        String batalla2 = "";
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        String fecha = dia + "/" + mes + "/" + año;
        if(rendirse==false){
        if (sacar == false) {
            if (FantasmasBuenosP2 == 0) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getUser()
                        + " has ganado porque te has comido todos los fantasmas buenos de " + player.getOponente());
                victoria = 1;
            }
            if (FantasmasMalosP2 == 0) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getUser()
                        + " has ganado porque te has comido todos los fantasmas malos de " + player.getOponente());
                victoria = 1;
            }

            if (FantasmasBuenosP1 == 0) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getOponente()
                        + " has ganado porque te has comido todos los fantasmas buenos de " + player.getUser());
                victoria = 2;
            }
            if (FantasmasMalosP1 == 0) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getOponente()
                        + " has ganado porque te has comido todos los fantasmas malos de " + player.getUser());
                victoria = 2;
            }
        } else {
            if (victoria == 1) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getUser()
                        + " has ganado porque lograste sacar un fantasma bueno");
                victoria = 1;
            }
            if (victoria == 2) {
                JOptionPane.showMessageDialog(null, "Felicidades " + player.getOponente()
                        + " has ganado porque lograste sacar un fantasma bueno");
                victoria = 2;
            }
        }

        if (victoria == 1) {
            batalla1 = player.getUser() + " vs " + player.getOponente() + " Ganador:  " + player.getUser() + " (+ 3 Puntos) " + fecha;
            batalla2 = player.getOponente() + " vs " + player.getUser() + " Ganador:  " + player.getUser() + " (+0 Puntos) " + fecha;
            ganador = player.getUser();
        }
        if (victoria == 2) {
            batalla1 = player.getUser() + " vs " + player.getOponente() + " Ganador: " + player.getOponente() + " (+ 0 Puntos) " + fecha;
            batalla2 = player.getOponente() + " vs " + player.getUser() + " Ganador: " + player.getOponente() + " (+ 3 Puntos) " + fecha;
            ganador = player.getOponente();
        }
        }else{
            if(P1==true){
            batalla1=player.getUser() + " vs " + player.getOponente() + " Ganador: " + player.getOponente() + " (+ 0 Puntos) " + fecha;
            batalla2 = player.getOponente() + " vs " + player.getUser() + " Ganador: " + player.getOponente() + " (+ 3 Puntos) " + fecha;
            ganador = player.getOponente();
            JOptionPane.showMessageDialog(null, "Felicidades " + player.getOponente()
                        + " has ganado porque "+player.getUser()+" se ha rendido");
            }else{
            batalla1 = player.getUser() + " vs " + player.getOponente() + " Ganador:  " + player.getUser() + " (+ 3 Puntos) " + fecha;
            batalla2 = player.getOponente() + " vs " + player.getUser() + " Ganador:  " + player.getUser() + " (+0 Puntos) " + fecha;
            JOptionPane.showMessageDialog(null, "Felicidades " + player.getUser()
                        + " has ganado porque "+player.getOponente()+" se ha rendido");
            ganador = player.getUser();
                
            }
            
        }
        
        main.getfunciones().fechas(0, player.getUser(), player.getOponente(), batalla1, batalla2);
        main.getfunciones().puntos(0, ganador);

        main.llamarmenu(player, main);
        juego.dispose();

    }

    public void posicionarTodo() {
        ColocarEsquinas();
        PosicionarFantasmas();
    }

}

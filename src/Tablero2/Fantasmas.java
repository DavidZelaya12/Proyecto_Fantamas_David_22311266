package Tablero2;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import Proyecto.*;

public class Fantasmas {

    private int currentRow;
    private int currentColumn;
    String TipoFantasma;
    int lvlfantasma;
    boolean p1;
    boolean posicionado = false;
    ImageIcon icono;
    ImageIcon iconoEscondido;
    private int dificultad;
    Player player;

    //Contstructor
    public Fantasmas(String nombrePersonaje, int lvlFantasma, boolean p1, String path) {
        this.TipoFantasma = nombrePersonaje;
        this.lvlfantasma = lvlFantasma;
        this.p1 = p1;
        this.dificultad = 8;

        try {
            Image resizedImg = resizeImage(ImageIO.read(new File("src/img/interrogacion.jpg")), 85, 85);
            iconoEscondido = new ImageIcon(resizedImg);
        } catch (Exception e) {
            iconoEscondido = null;
        }

        try {
            Image newImg = resizeImage(ImageIO.read(new File(path)), 55, 55);
            icono = new ImageIcon(newImg);
        } catch (Exception e) {
            icono = null;
        }

        loadIcon();
    }

    public void setDificultad(int num) {
        this.dificultad = num;
    }

    private void loadIcon() {
        String NombreFoto;
        if (Player.getrandom() == true) {
            NombreFoto = "src/random/" + TipoFantasma.replace(" ", "") + ".jpg";
        } else {
            NombreFoto = "src/img/" + TipoFantasma.replace(" ", "") + ".jpg";
        }

        try {

            Image newImg = resizeImage(ImageIO.read(new File(NombreFoto)), 80, 72);
            icono = new ImageIcon(newImg);
        } catch (Exception e) {
            icono = null;
        }
    }

    private Image resizeImage(Image img, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static ArrayList<Fantasmas> FantasmasP1() {
        ArrayList<Fantasmas> personajes = new ArrayList<Fantasmas>();

        personajes.add(new Fantasmas("castillop1", -1, true, null));
        personajes.add(new Fantasmas("castillop1", -2, true, null));
        if (Player.getdificultad() == 2) {
            for (int i = 0; i < 2; i++) {
                personajes.add(new Fantasmas("FantasmaFalso", 3, true, null));
            }
        }

        for (int i = 0; i < Player.getdificultad() / 2; i++) {
            personajes.add(new Fantasmas("FantasmaBueno", 1, true, null));
            System.out.println("Buenos 1 ");
        }

        for (int i = 0; i < Player.getdificultad() / 2; i++) {
            personajes.add(new Fantasmas("FantasmaMalo", 1, true, null));
            System.out.println("Malos 1");
        }

        return personajes;
    }

    public static ArrayList<Fantasmas> FantasmasP2() {
        ArrayList<Fantasmas> personajes = new ArrayList<Fantasmas>();

        personajes.add(new Fantasmas("castillop2", -4, false, null));
        personajes.add(new Fantasmas("castillop2", -5, false, null));
        
        if(Player.getdificultad()==2){
            for(int i=0;i<2;i++){
            personajes.add(new Fantasmas("FantasmaFalso", 3, false, null));
            }
        }
        
        
        for (int i = 0; i < Player.getdificultad() / 2; i++) {
            personajes.add(new Fantasmas("FantasmaBueno", 1, false, null));
            System.out.println("Buenos 2 ");
        }

        for (int i = 0; i < Player.getdificultad() / 2; i++) {
            personajes.add(new Fantasmas("FantasmaMalo", 1, false, null));
            System.out.println("Malos 2 ");
        }

        return personajes;
    }

    public String toString() {
        String nombreSinEtiquetas = TipoFantasma.replaceAll("\\<.*?\\>", "");
        return nombreSinEtiquetas;
    }

}

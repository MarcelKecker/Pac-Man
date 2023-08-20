import javax.swing.*;
import java.util.Objects;

public class Spieler extends JLabel {
    ImageIcon spielerRechts;
    ImageIcon spielerLinks;
    ImageIcon spielerOben;
    ImageIcon spielerUnten;
    Pacman spielfeld;
    boolean playerReady;
    int xStartposition;
    int yStartposition;

    final int radius = 48;

    int xBewegungsRichtung;
    int yBewegungsRichtung;

    Spieler(Pacman spielfeld) {
        this.spielfeld = spielfeld;
        spielerRechts = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanRechts.png")));
        spielerLinks = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanLinks.png")));
        spielerOben = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanOben.png")));
        spielerUnten = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanUnten.png")));
        this.setIcon(spielerRechts);
        this.setSize(Pacman.FELD_LAENGE, Pacman.FELD_LAENGE);
    }

    public int feld(int x, int y) {
        return Pacman.SPIELFELD_BREITE * y + x;
    }

    public void updateRichtung() {
        switch (spielfeld.startRichtung) {
            case 0 -> {
                xBewegungsRichtung = 1;
                yBewegungsRichtung = 0;
                this.setIcon(spielerRechts);
            }
            case 90 -> {
                xBewegungsRichtung = 0;
                yBewegungsRichtung = 1;
                this.setIcon(spielerUnten);
            }
            case 180 -> {
                xBewegungsRichtung = -1;
                yBewegungsRichtung = 0;
                this.setIcon(spielerLinks);
            }
            case 270 -> {
                xBewegungsRichtung = 0;
                yBewegungsRichtung = -1;
                this.setIcon(spielerOben);
            }
        }
    }

    public boolean bewegungMoeglich(int xBewegung, int yBewegung) {
        return spielfeld.feldFrei(getX() + xBewegung * Pacman.FELD_LAENGE, getY() + yBewegung * Pacman.FELD_LAENGE) || this.getX() % spielfeld.FELD_LAENGE != 0 || this.getY() % spielfeld.FELD_LAENGE != 0;
    }

    public void bewege() {
        if (this.getX() % spielfeld.FELD_LAENGE == 0 && this.getY() % spielfeld.FELD_LAENGE == 0) {
            updateRichtung();
        }
        if (bewegungMoeglich(xBewegungsRichtung, yBewegungsRichtung)) {
            this.setLocation(this.getX() + xBewegungsRichtung, this.getY() + yBewegungsRichtung);
        }
        spielfeld.untersucheScore();
    }
    public void setPosition(Position position) {
        setLocation(position.getX() * Pacman.FELD_LAENGE, position.getY() * Pacman.FELD_LAENGE);
    }

    public int getRadius() {
        return radius;
    }
}


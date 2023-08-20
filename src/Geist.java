import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public class Geist extends JLabel {
    ImageIcon geist;
    Pacman spielfeld;
    int letzteRichtung = (int) (Math.random() * 4);
    int xBewegung = 1;
    int yBewegung = 0;

    final int radius = 45;

    Geist(String farbe, Pacman spielfeld) {
        this.spielfeld = spielfeld;
        geist = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource(farbe + ".png")));
        this.setIcon(geist);
        this.setSize(Pacman.FELD_LAENGE, Pacman.FELD_LAENGE);
    }

    public void bewege() {
        if (this.getX() % Pacman.FELD_LAENGE == 0 && this.getY() % Pacman.FELD_LAENGE == 0) {
            int richtung = richtungAuswaehlen();
            switch (richtung) {
                case 0 -> {
                    xBewegung = 1;
                    yBewegung = 0;
                }
                case 1 -> {
                    xBewegung = -1;
                    yBewegung = 0;
                }
                case 2 -> {
                    xBewegung = 0;
                    yBewegung = 1;
                }
                case 3 -> {
                    xBewegung = 0;
                    yBewegung = -1;
                }
                default -> throw new RuntimeException("Das kann nicht sein");
            }
        }
        this.setLocation(this.getX() + xBewegung, this.getY() + yBewegung);
    }

    private boolean bewegungMoeglich(int xBewegung, int yBewegung) {
        return spielfeld.feldFrei(getX() + xBewegung * Pacman.FELD_LAENGE, getY() + yBewegung * Pacman.FELD_LAENGE);
    }

    private int richtungAuswaehlen() {
        int richtung;
        int anzahlVerbotenerRichtungen = 0;
        boolean[] richtungMoeglich = new boolean[4];
        Arrays.fill(richtungMoeglich, true);

        if (!bewegungMoeglich(1, 0)) {
            anzahlVerbotenerRichtungen++;
            richtungMoeglich[0] = false;
        }
        if (!bewegungMoeglich(-1, 0)) {
            anzahlVerbotenerRichtungen++;
            richtungMoeglich[1] = false;
        }
        if (!bewegungMoeglich(0, 1)) {
            anzahlVerbotenerRichtungen++;
            richtungMoeglich[2] = false;
        }
        if (!bewegungMoeglich(0, -1)) {
            anzahlVerbotenerRichtungen++;
            richtungMoeglich[3] = false;
        }
        if (anzahlVerbotenerRichtungen != 3) {
            int verboteneRichtung = switch (letzteRichtung) {
                case 0 -> 1;
                case 1 -> 0;
                case 2 -> 3;
                case 3 -> 2;
                default -> throw new RuntimeException("Verbotene Richtung: " + letzteRichtung);
            };
            if (richtungMoeglich[verboteneRichtung]) {
                anzahlVerbotenerRichtungen++;
            }
            richtungMoeglich[verboteneRichtung] = false;
        }

        richtung = (int) (Math.random() * (4 - anzahlVerbotenerRichtungen));
        for (int i = 0; i < 4; i++) {
            if (!richtungMoeglich[i]) {
                richtung++;
            } else if (richtung == i) {
                letzteRichtung = richtung;
                return richtung;
            }
        }
        throw new RuntimeException("Hier sollte man gar nicht ankommen");
    }

    public boolean spielerHit(Spieler spieler) {
        int distanzSquared = (this.getX()-spieler.getX()) * (this.getX()-spieler.getX()) + (this.getY()-spieler.getY()) * (this.getY()-spieler.getY());
        return distanzSquared < (getRadius() + spieler.getRadius()) * (getRadius() + spieler.getRadius());
    }

    public int getRadius() {
        return radius;
    }

    public void setPosition(Position position) {
        setLocation(position.getX() * Pacman.FELD_LAENGE, position.getY() * Pacman.FELD_LAENGE);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Objects;

public class Pacman implements KeyListener {
    public static final int SPIELFELD_HOEHE = 9;
    JFrame frame;
    public static final int SPIELFELD_BREITE = 13;
    public static final int FELD_LAENGE = 100;
    static final int ANZAHL_LEVEL = 10;
    public static boolean[] istWand;
    boolean[] istPunkt;
    int xStartposition;
    int yStartposition;
    int startRichtung;
    int score;
    int leben = 3;
    int aktuellesLevel;
    ImageIcon punkt;
    ImageIcon figur;
    JLabel[] labels;
    JLabel labelScore;
    JLabel labelLeben;
    JPanel panelSpielfeld;
    Spieler spieler;
    Geist[] geister;
    GameLoop gameLoop;
    private static final Level[] LEVELS = {
            Levels.parseIstWand(Levels.level1),
            Levels.parseIstWand(Levels.level2),
            Levels.parseIstWand(Levels.level3),
            Levels.parseIstWand(Levels.level4),
            Levels.parseIstWand(Levels.level5),
            Levels.parseIstWand(Levels.level6),
            Levels.parseIstWand(Levels.level7),
            Levels.parseIstWand(Levels.level8),
            Levels.parseIstWand(Levels.level9),
            Levels.parseIstWand(Levels.level10)
    };

    Pacman() {
        punkt = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanPunkt.png")));
        figur = new ImageIcon(Objects.requireNonNull(Pacman.class.getResource("PacmanRechts.png")));

        gameLoop = new GameLoop(this);

        geister = new Geist[3];
        geister[0] = new Geist("blau", this);
        geister[1] = new Geist("rot", this);
        geister[2] = new Geist("pink", this);
        spieler = new Spieler(this);
        labelScore = new JLabel();
        updateScore();
        labelScore.setBounds(FELD_LAENGE, 0, FELD_LAENGE * SPIELFELD_BREITE, FELD_LAENGE);
        labelScore.setFont(labelScore.getFont().deriveFont(64.0f));
        labelScore.setForeground(Color.white);

        labelLeben = new JLabel();
        updateLeben();
        labelLeben.setBounds(7 * FELD_LAENGE, 0, FELD_LAENGE * SPIELFELD_BREITE, FELD_LAENGE);
        labelLeben.setFont(labelScore.getFont().deriveFont(64.0f));
        labelLeben.setForeground(Color.white);

        panelSpielfeld = new JPanel();
        panelSpielfeld.setBounds(0, 0, SPIELFELD_BREITE * FELD_LAENGE, SPIELFELD_HOEHE * FELD_LAENGE);
        panelSpielfeld.setLayout(new GridLayout(SPIELFELD_HOEHE, SPIELFELD_BREITE));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, SPIELFELD_BREITE * FELD_LAENGE, SPIELFELD_HOEHE * FELD_LAENGE);
        for (Geist geist : geister) {
            layeredPane.add(geist, Integer.valueOf(1));
        }
        layeredPane.add(spieler, Integer.valueOf(1));
        layeredPane.add(labelScore, Integer.valueOf(2));
        layeredPane.add(labelLeben, Integer.valueOf(2));
        layeredPane.add(panelSpielfeld, Integer.valueOf(0));
        layeredPane.setBackground(Color.BLACK);

        frame = new JFrame();
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SPIELFELD_BREITE * FELD_LAENGE, SPIELFELD_HOEHE * FELD_LAENGE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.addKeyListener(this);

        labels = new JLabel[SPIELFELD_BREITE * SPIELFELD_HOEHE];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            panelSpielfeld.add(labels[i]);
            labels[i].setOpaque(true);
        }
        istWand = new boolean[SPIELFELD_BREITE * SPIELFELD_HOEHE];
        istPunkt = new boolean[SPIELFELD_BREITE * SPIELFELD_HOEHE];
        spielfeldAnzeigen(LEVELS[0]);
        frame.setVisible(true);
    }

    public void tick() {
        spieler.bewege();
        for (Geist geist : geister) {
            geist.bewege();
        }
        pruefeTod();
    }

    public int feld(int x, int y) {
        return SPIELFELD_BREITE * y + x;
    }

    public void untersucheScore() {
        if (spieler.getX() % FELD_LAENGE == 0
                && spieler.getY() % FELD_LAENGE == 0
                && istPunkt[feld(spieler.getX() / FELD_LAENGE, spieler.getY() / FELD_LAENGE)]) {
            istPunkt[feld(spieler.getX() / FELD_LAENGE, spieler.getY() / FELD_LAENGE)] = false;
            score++;
            updateScore();
            setzePunkte();
            untersucheLevelCompletion();
        }
    }

    public void untersucheLevelCompletion() {
        int noetigerScoreNextLevel = berechneKumulierterScore(aktuellesLevel + 1);
        if (score >= noetigerScoreNextLevel) {
            if (aktuellesLevel + 1 == ANZAHL_LEVEL) {
                JOptionPane.showMessageDialog(frame, "You won :)");
                System.exit(0);
            } else {
                aktuellesLevel++;
                completeLevel(aktuellesLevel);
                spielfeldAnzeigen(LEVELS[aktuellesLevel]);
                leben++;
                updateLeben();
            }
        }
    }

    private int berechneKumulierterScore(int bisLevelEinschliesslich) {
        int noetigerScoreNextLevel = 0;
        for (int i = 0; i < bisLevelEinschliesslich; i++) {
            noetigerScoreNextLevel += LEVELS[i].getNoetigerScore();
        }
        return noetigerScoreNextLevel;
    }

    private void completeLevel(int level) {
        JOptionPane.showMessageDialog(frame, "Level " + (level) + " complete :)");
        resetGame(level);
    }

    private void resetGame(int level) {
        gameLoop.stop();
        aktuellesLevel = level;
        Arrays.fill(istWand, false);
        Arrays.fill(istPunkt, false);
    }

    private void updateLeben() {
        labelLeben.setText("Lives: " + leben);
    }

    private void updateScore() {
        labelScore.setText("Score: " + score);
    }

    private void pruefeTod() {
        boolean spielerHit = false;
        for (Geist geist : geister) {
            if (geist.spielerHit(spieler)) {
                spielerHit = true;
            }
        }
        if (spielerHit) {
            leben--;
            updateLeben();
            if (leben == 0) {
                int nochEinSpiel = JOptionPane.showOptionDialog(frame, "Game Over :(\nNoch ein Spiel?", "Noch ein Spiel?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null,
                        new String[]{"Ja", "Nein"}, "Ja");
                if (nochEinSpiel == 0) {
                    leben = 3;
                    score = 0;
                    updateScore();
                    updateLeben();
                    resetGame(0);
                    spielfeldAnzeigen(LEVELS[0]);
                } else {
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "You died :(");
                gameLoop.stop();
                startRichtung = LEVELS[aktuellesLevel].getSpielerStartrichtung();
                xStartposition = LEVELS[aktuellesLevel].getSpielerPosition().getX();
                yStartposition = LEVELS[aktuellesLevel].getSpielerPosition().getY();
                for (int i = 0; i < geister.length; i++) {
                    geister[i].setPosition(LEVELS[aktuellesLevel].getGeisterPositionen()[i]);
                }
                spieler.updateRichtung();
                spieler.setPosition(new Position(xStartposition, yStartposition));
            }
        }
    }

    private void spielfeldAnzeigen(Level level) {
        startRichtung = LEVELS[aktuellesLevel].getSpielerStartrichtung();
        for (int i = 0; i < geister.length; i++) {
            geister[i].setPosition(LEVELS[aktuellesLevel].getGeisterPositionen()[i]);
        }
        spieler.setPosition(level.getSpielerPosition());
        spieler.updateRichtung();

        for (int i = 0; i < SPIELFELD_BREITE * SPIELFELD_HOEHE; i++) {
            if (!level.istWand[i]) {
                istPunkt[i] = true;
            }
        }
        for (int i = 0; i < labels.length; i++) {
            if (level.istWand[i]) {
                labels[i].setBackground(Color.BLACK);
            } else {
                labels[i].setBackground(Color.WHITE);
            }
        }
        setzePunkte();
    }

    private void setzePunkte() {
        for (int i = 0; i < istPunkt.length; i++) {
            if (istPunkt[i]) {
                labels[i].setIcon(punkt);
            } else if (!istWand[i]) {
                labels[i].setIcon(null);
            }
        }
    }

    public boolean feldFrei(int xAbbildung, int yAbbildung) {
        return !LEVELS[aktuellesLevel].istWand[feld(xAbbildung / FELD_LAENGE, yAbbildung / FELD_LAENGE)] || xAbbildung % FELD_LAENGE != 0 || yAbbildung % FELD_LAENGE != 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w', 'W' -> startRichtung = 270;
            case 'a', 'A' -> startRichtung = 180;
            case 's', 'S' -> startRichtung = 90;
            case 'd', 'D' -> startRichtung = 0;
            case ' ' -> gameLoop.start();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        new Pacman();
    }
}

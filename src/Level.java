public class Level {
    private final int spielerStartrichtung;
    private final Position spielerPosition;
    /**
     * Blau, Rot, Pink
     */
    private final Position[] geisterPositionen;
    private final int noetigerScore;
    final boolean[] istWand;


    public Level(int spielerStartrichtung, Position spielerPosition, Position[] geisterPositionen, int noetigerScore, boolean[] istWand) {
        this.spielerStartrichtung = spielerStartrichtung;
        this.spielerPosition = spielerPosition;
        this.geisterPositionen = geisterPositionen;
        this.noetigerScore = noetigerScore;
        this.istWand = istWand;
    }
    public int getSpielerStartrichtung() {
        return spielerStartrichtung;
    }

    public Position getSpielerPosition() {
        return spielerPosition;
    }

    public Position[] getGeisterPositionen() {
        return geisterPositionen;
    }

    public int getNoetigerScore() {
        return noetigerScore;
    }

    public boolean[] getIstWand() {
        return istWand;
    }
}

public class Levels {
    public static final String level1 = """
            1111111111111
            1000001000001
            1010101011101
            101B101001001
            101110r001P01
            101R101001001
            1010101011101
            1000001000001
            1111111111111""";
    public static final String level2 = """
            1111111111111
            1000000000001
            1011110111101
            1010000000101
            101R11B11P101
            1010100000101
            101011u111101
            1000000000001
            1111111111111
            """;
    public static final String level3 = """
            1111111111111
            1000000000001
            1011101111101
            100l101010101
            1011101010101
            1010001010101
            1010101010101
            100000B0R0P01
            1111111111111
            """;
    public static final String level4 = """
            1111111111111
            1000001000001
            1010111110101
            1010000000101
            101R11B11P101
            1010000000101
            101111u111101
            1000000000001
            1111111111111
            """;
    public static final String level5 = """
            1111111111111
            1000000000001
            10101o0110101
            1010110R10101
            1010000000101
            1010110B10101
            10101P0110101
            1000000000001
            1111111111111
            """;
    public static final String level6 = """
            1111111111111
            1000000000001
            1011101011101
            10000l1R00101
            1010111110101
            10100B1P00001
            1011101011101
            1000000000001
            1111111111111
            """;
    public static final String level7 = """
            1111111111111
            1000000000001
            101111o111101
            1010000000101
            10R0110110P01
            1010000000101
            101111B111101
            1000000000001
            1111111111111
            """;
    public static final String level8 = """
            1111111111111
            1000000000001
            101110o011101
            101R10101B101
            1000001000001
            100100P001001
            1101111111011
            1100000000011
            1111111111111
            """;
    public static final String level9 = """
            1111111111111
            100000r000001
            1111101011111
            1000000000001
            1010111110101
            10R000B000P01
            1111101011111
            1000000000001
            1111111111111
            """;
    public static final String level10 = """
            1111111111111
            100000r000001
            1010101010101
            1010100010101
            1010R010B0101
            101010P010101
            1010101010101
            1000000000001
            1111111111111""";


    public static Level parseIstWand(String levelString) {
        String[] rows = levelString.split("\n");
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].length() != Pacman.SPIELFELD_BREITE) {
                throw new RuntimeException("nicht passende Laenge der Zeile " + (i + 1) + ". Laenge: " + rows[i].length());
            }
        }
        if (rows.length != Pacman.SPIELFELD_HOEHE) {
            throw new RuntimeException("nicht passende Anzahl Zeilen. Anzahl Zeilen: " + rows.length + " Anzahl erlaubter Zeilen: 9");
        }

        int spielerStartrichtung = 0;
        Position spielerPosition = new Position(0, 0);
        Position[] geisterPositionen = new Position[3];
        int noetigerScore = Pacman.SPIELFELD_BREITE * Pacman.SPIELFELD_HOEHE;
        boolean[] istWand = new boolean[Pacman.SPIELFELD_BREITE * Pacman.SPIELFELD_HOEHE];

        for (int iy = 0; iy < Pacman.SPIELFELD_HOEHE; iy++) {
            for (int ix = 0; ix < Pacman.SPIELFELD_BREITE; ix++) {
                switch (rows[iy].charAt(ix)) {
                    case '0' -> {}
                    case '1' -> {
                        istWand[Pacman.SPIELFELD_BREITE * iy + ix] = true;
                        noetigerScore--;
                    }
                    case 'B' -> geisterPositionen[0] = new Position(ix, iy);
                    case 'R' -> geisterPositionen[1] = new Position(ix, iy);
                    case 'P' -> geisterPositionen[2] = new Position(ix, iy);
                    case 'r' -> {
                        spielerStartrichtung = 0;
                        spielerPosition = new Position(ix, iy);
                    }
                    case 'l' -> {
                        spielerStartrichtung = 180;
                        spielerPosition = new Position(ix, iy);
                    }
                    case 'u' -> {
                        spielerStartrichtung = 90;
                        spielerPosition = new Position(ix, iy);
                    }
                    case 'o' -> {
                        spielerStartrichtung = 270;
                        spielerPosition = new Position(ix, iy);
                    }
                    default -> throw new RuntimeException("ungueltiger Char eingegeben: " + rows[iy].charAt(ix));
                }
            }
        }

        return new Level(spielerStartrichtung, spielerPosition, geisterPositionen, noetigerScore, istWand);
    }

}

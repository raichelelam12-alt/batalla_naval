import java.util.ArrayList;

/**
 * Tablero de 8 por 8.
 */
class Tablero {
    private final Barco[][] barcosEnCasilla;
    private final char[][] estado;
    private final ArrayList<Barco> listaBarcos;

    public Tablero() {
        barcosEnCasilla = new Barco[8][8];
        estado = new char[8][8];
        listaBarcos = new ArrayList<Barco>();

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                estado[fila][columna] = '~';
            }
        }
    }

    /**
     * Intenta colocar un barco. Devuelve false si se sale del tablero
     * o si ocupa una casilla que ya tiene otro barco.
     */
    public boolean colocarBarco(
            Barco barco,
            int fila,
            int columna,
            boolean horizontal
    ) {
        int[][] coordenadas = new int[barco.getTamano()][2];

        // Primero validamos todas las casillas antes de colocar el barco.
        for (int i = 0; i < barco.getTamano(); i++) {
            int nuevaFila = fila;
            int nuevaColumna = columna;

            if (horizontal) {
                nuevaColumna = columna + i;
            } else {
                nuevaFila = fila + i;
            }

            // Validación de límites.
            if (nuevaFila < 0 || nuevaFila >= 8
                    || nuevaColumna < 0 || nuevaColumna >= 8) {
                return false;
            }

            // Validación para evitar superposición.
            if (barcosEnCasilla[nuevaFila][nuevaColumna] != null) {
                return false;
            }

            coordenadas[i][0] = nuevaFila;
            coordenadas[i][1] = nuevaColumna;
        }

        // Si todas las casillas son válidas, colocamos el barco.
        barco.setCoordenadas(coordenadas);

        for (int i = 0; i < coordenadas.length; i++) {
            int filaBarco = coordenadas[i][0];
            int columnaBarco = coordenadas[i][1];

            barcosEnCasilla[filaBarco][columnaBarco] = barco;
        }

        listaBarcos.add(barco);
        return true;
    }

    /**
     * Recibe un disparo y actualiza la casilla.
     */
    public String recibirDisparo(int fila, int columna) {
        if (fila < 0 || fila >= 8 || columna < 0 || columna >= 8) {
            return "La coordenada está fuera del tablero.";
        }

        if (yaRecibioDisparo(fila, columna)) {
            return "Ya se había disparado a esa casilla.";
        }

        Barco barco = barcosEnCasilla[fila][columna];

        if (barco == null) {
            estado[fila][columna] = 'O';
            return "Agua.";
        }

        estado[fila][columna] = 'X';
        barco.recibirImpacto();

        if (barco.estaHundido()) {
            return "¡Impacto! Hundiste un barco.";
        }

        return "¡Impacto!";
    }

    public boolean yaRecibioDisparo(int fila, int columna) {
        return estado[fila][columna] == 'X'
                || estado[fila][columna] == 'O';
    }

    /**
     * Devuelve true cuando todos los barcos están hundidos.
     */
    public boolean verificarVictoria() {
        if (listaBarcos.size() == 0) {
            return false;
        }

        for (int i = 0; i < listaBarcos.size(); i++) {
            if (!listaBarcos.get(i).estaHundido()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Dibuja el tablero usando ciclos for anidados.
     * Si mostrarBarcos es false, oculta los barcos que aún no han recibido
     * disparos.
     */
    public String dibujar(boolean mostrarBarcos) {
        StringBuilder dibujo = new StringBuilder();

        dibujo.append("     A   B   C   D   E   F   G   H\n");

        for (int fila = 0; fila < 8; fila++) {
            dibujo.append(String.format("%2d ", fila + 1));

            for (int columna = 0; columna < 8; columna++) {
                char simbolo = estado[fila][columna];

                if (simbolo == '~'
                        && mostrarBarcos
                        && barcosEnCasilla[fila][columna] != null) {
                    simbolo = barcosEnCasilla[fila][columna].getSimbolo();
                }

                dibujo.append("[").append(simbolo).append("] ");
            }

            dibujo.append("\n");
        }

        dibujo.append("\nLeyenda: ~ agua | X impacto | O fallo");
        if (mostrarBarcos) {
            dibujo.append(" | P portaaviones | S submarino | L lancha");
        }
        dibujo.append("\n");

        return dibujo.toString();
    }
}

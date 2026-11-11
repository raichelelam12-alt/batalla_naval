import javax.swing.*;
import java.awt.Font;
import java.util.Random;

public class BatallaNaval {

    public static void main(String[] args) {
        Random azar = new Random();

        Tablero tableroJugador = new Tablero();
        Tablero tableroComputadora = new Tablero();

        // Colocamos los barcos en ambos tableros.
        colocarFlotaAleatoria(tableroJugador, azar);
        colocarFlotaAleatoria(tableroComputadora, azar);

        JOptionPane.showMessageDialog(
                null,
                "¡Bienvenido a Batalla Naval!\n"
                        + "Escribe una coordenada como A5 o como 2,4.\n"
                        + "En el formato 2,4, el primer número es la fila.",
                "Batalla Naval",
                JOptionPane.INFORMATION_MESSAGE
        );

        while (true) {
            mostrarTableros(tableroJugador, tableroComputadora);

            String entrada = JOptionPane.showInputDialog(
                    null,
                    "Ingresa la coordenada para disparar:",
                    "Tu turno",
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el jugador presiona Cancelar o cierra la ventana, termina el juego.
            if (entrada == null) {
                JOptionPane.showMessageDialog(null, "Terminaste el juego.");
                break;
            }

            int[] coordenada;

            try {
                coordenada = convertirCoordenada(entrada);
            } catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(null, error.getMessage());
                continue;
            }

            int fila = coordenada[0];
            int columna = coordenada[1];

            if (tableroComputadora.yaRecibioDisparo(fila, columna)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Ya disparaste a esa casilla. Intenta con otra."
                );
                continue;
            }

            String resultadoJugador =
                    tableroComputadora.recibirDisparo(fila, columna);

            if (tableroComputadora.verificarVictoria()) {
                mostrarTableros(tableroJugador, tableroComputadora);
                JOptionPane.showMessageDialog(
                        null,
                        resultadoJugador + "\n¡Ganaste la partida!"
                );
                break;
            }

            // Turno de la computadora.
            int filaComputadora;
            int columnaComputadora;

            do {
                filaComputadora = azar.nextInt(8);
                columnaComputadora = azar.nextInt(8);
            } while (tableroJugador.yaRecibioDisparo(
                    filaComputadora, columnaComputadora
            ));

            String resultadoComputadora =
                    tableroJugador.recibirDisparo(
                            filaComputadora, columnaComputadora
                    );

            String mensaje = "Tu disparo: " + resultadoJugador
                    + "\nLa computadora disparó en "
                    + (filaComputadora + 1) + ","
                    + (columnaComputadora + 1) + ". "
                    + resultadoComputadora;

            if (tableroJugador.verificarVictoria()) {
                mostrarTableros(tableroJugador, tableroComputadora);
                JOptionPane.showMessageDialog(
                        null,
                        mensaje + "\nLa computadora hundió todos tus barcos. "
                                + "Ganó la computadora."
                );
                break;
            }

            JOptionPane.showMessageDialog(null, mensaje);
        }
    }

    /**
     * Coloca 1 portaaviones, 2 submarinos y 3 lanchas.
     */
    public static void colocarFlotaAleatoria(Tablero tablero, Random azar) {
        Barco[] flota = {
                new Portaaviones(),
                new Submarino(),
                new Submarino(),
                new Lancha(),
                new Lancha(),
                new Lancha()
        };

        for (int i = 0; i < flota.length; i++) {
            boolean colocado = false;

            while (!colocado) {
                int fila = azar.nextInt(8);
                int columna = azar.nextInt(8);
                boolean horizontal = azar.nextBoolean();

                colocado = tablero.colocarBarco(
                        flota[i], fila, columna, horizontal
                );
            }
        }
    }

    /**
     * Convierte A5 o 2,4 en una fila y una columna de matriz,
     * ambas empezando desde cero.
     */
    public static int[] convertirCoordenada(String entrada) {
        entrada = entrada.trim().toUpperCase();

        // Formato de letra y número, por ejemplo A5.
        if (entrada.length() == 2
                && entrada.charAt(0) >= 'A'
                && entrada.charAt(0) <= 'H'
                && entrada.charAt(1) >= '1'
                && entrada.charAt(1) <= '8') {

            int columna = entrada.charAt(0) - 'A';
            int fila = entrada.charAt(1) - '1';

            return new int[]{fila, columna};
        }

        // Formato de fila y columna, por ejemplo 2,4.
        String[] partes = entrada.split(",");

        if (partes.length == 2) {
            try {
                int fila = Integer.parseInt(partes[0].trim()) - 1;
                int columna = Integer.parseInt(partes[1].trim()) - 1;

                if (fila >= 0 && fila < 8
                        && columna >= 0 && columna < 8) {
                    return new int[]{fila, columna};
                }
            } catch (NumberFormatException error) {
                // Se muestra el mensaje de coordenada inválida más abajo.
            }
        }

        throw new IllegalArgumentException(
                "Coordenada inválida. Usa A5 o fila,columna, por ejemplo 2,4."
        );
    }

    /**
     * Construye y muestra ambos tableros en un diálogo con letra monoespaciada.
     */
    public static void mostrarTableros(
            Tablero tableroJugador,
            Tablero tableroComputadora
    ) {
        String texto = "TU TABLERO\n"
                + tableroJugador.dibujar(true)
                + "\nTABLERO DE LA COMPUTADORA\n"
                + tableroComputadora.dibujar(false);

        JTextArea areaTexto = new JTextArea(texto);
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));

        JScrollPane panel = new JScrollPane(areaTexto);
        panel.setPreferredSize(new java.awt.Dimension(430, 500));

        JOptionPane.showMessageDialog(
                null,
                panel,
                "Estado de los tableros",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

/**
 * Clase padre de los barcos.
 */
abstract class Barco {
    protected int tamano;
    protected int[][] coordenadas;
    protected boolean hundido;
    protected int impactos;

    public Barco(int tamano) {
        this.tamano = tamano;
        this.coordenadas = new int[tamano][2];
        this.hundido = false;
        this.impactos = 0;
    }

    public int getTamano() {
        return tamano;
    }

    public boolean estaHundido() {
        return hundido;
    }

    public void setCoordenadas(int[][] nuevasCoordenadas) {
        coordenadas = nuevasCoordenadas;
    }

    public void recibirImpacto() {
        impactos++;

        if (impactos >= tamano) {
            hundido = true;
        }
    }

    public abstract char getSimbolo();
}

/**
 * Subclases de Barco.
 */
class Portaaviones extends Barco {
    public Portaaviones() {
        super(4);
    }

    @Override
    public char getSimbolo() {
        return 'P';
    }
}

class Submarino extends Barco {
    public Submarino() {
        super(3);
    }

    @Override
    public char getSimbolo() {
        return 'S';
    }
}

class Lancha extends Barco {
    public Lancha() {
        super(1);
    }

    @Override
    public char getSimbolo() {
        return 'L';
    }
}

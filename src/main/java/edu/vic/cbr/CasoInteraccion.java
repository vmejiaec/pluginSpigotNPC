package edu.vic.cbr;

public class CasoInteraccion {

    private final String contextoJugador;
    private final AccionNPC accionNPC;
    private final int recompensa;

    public CasoInteraccion(String contextoJugador, AccionNPC accionNPC, int recompensa) {
        this.contextoJugador = contextoJugador;
        this.accionNPC = accionNPC;
        this.recompensa = recompensa;
    }

    public String getContextoJugador() {
        return contextoJugador;
    }

    public AccionNPC getAccionNPC() {
        return accionNPC;
    }

    public int getRecompensa() {
        return recompensa;
    }

    @Override
    public String toString() {
        return "CasoInteraccion{" +
                "contextoJugador='" + contextoJugador + '\'' +
                ", accionNPC=" + accionNPC +
                ", recompensa=" + recompensa +
                '}';
    }
}

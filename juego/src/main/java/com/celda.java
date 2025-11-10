package com;

public class celda {
    public static final String VACIA = " ";
    public static final String JUGADOR_X = "X";
    public static final String JUGADOR_O = "O";

    String estado;


    public celda() {
        this.estado = VACIA;
    }

    public void vaciar() {
        this.estado = VACIA;
    }


    public void marcar(String simbolo) {
        if (simbolo.equals(JUGADOR_X) || simbolo.equals(JUGADOR_O)) {
            this.estado = simbolo;
        }
    }


    public boolean esVacia() {
        return this.estado.equals(VACIA);
    }


    @Override
    public String toString() {
        return this.estado;
    }
}
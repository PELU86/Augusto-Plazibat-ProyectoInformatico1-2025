package com;

public class tablero {

    celda[][] tablero;


    public tablero() {
        tablero = new celda[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = new celda();
            }
        }
    }


    public void mostrarTablero() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(tablero[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }
    

    public String getEstadoProtocolo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(tablero[i][j].estado);
                if (i * 3 + j < 8) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }


    public boolean colocarSimbolo(int fila, int col, String simbolo) {
        if (fila >= 0 && fila < 3 && col >= 0 && col < 3 && tablero[fila][col].esVacia()) {
            tablero[fila][col].marcar(simbolo);
            return true;
        }
        return false;
    }


    public boolean esTableroCompleto() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j].esVacia()) {
                    return false;
                }
            }
        }
        return true;
    }

    public String checkGanador() {
        for (int i = 0; i < 3; i++) {
            if (!tablero[i][0].esVacia() &&
                tablero[i][0].estado.equals(tablero[i][1].estado) &&
                tablero[i][1].estado.equals(tablero[i][2].estado)) {
                return tablero[i][0].estado;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (!tablero[0][j].esVacia() &&
                tablero[0][j].estado.equals(tablero[1][j].estado) &&
                tablero[1][j].estado.equals(tablero[2][j].estado)) {
                return tablero[0][j].estado;
            }
        }

        if (!tablero[0][0].esVacia() &&
            tablero[0][0].estado.equals(tablero[1][1].estado) &&
            tablero[1][1].estado.equals(tablero[2][2].estado)) {
            return tablero[0][0].estado;
        }
        if (!tablero[0][2].esVacia() &&
            tablero[0][2].estado.equals(tablero[1][1].estado) &&
            tablero[1][1].estado.equals(tablero[2][0].estado)) {
            return tablero[0][2].estado;
        }
        
        return celda.VACIA;
    }


    public boolean esGanador() {
        return !checkGanador().equals(celda.VACIA);
    }


    public boolean esEmpate() {
        return esTableroCompleto() && !esGanador();
    }
}
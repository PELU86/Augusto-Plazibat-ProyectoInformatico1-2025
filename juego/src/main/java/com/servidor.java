package com;

import java.io.*;
import java.net.*;

public class servidor {

    private ServerSocket serverSocket;
    private final int PUERTO = 9090;

    public void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor de Tic-Tac-Toe iniciado en el puerto " + PUERTO);
            System.out.println("Esperando a dos jugadores...");

            while (true) {
                Socket socketJugador1 = serverSocket.accept();
                System.out.println("Jugador 1 (X) conectado: " + socketJugador1.getInetAddress());
                
                PrintWriter out1 = new PrintWriter(socketJugador1.getOutputStream(), true);
                out1.println("BIENVENIDO " + celda.JUGADOR_X);
                out1.println("MENSAJE Esperando al Jugador 2...");

                Socket socketJugador2 = serverSocket.accept();
                System.out.println("Jugador 2 (O) conectado: " + socketJugador2.getInetAddress());
                
                PrintWriter out2 = new PrintWriter(socketJugador2.getOutputStream(), true);
                out2.println("BIENVENIDO " + celda.JUGADOR_O);
                out2.println("MENSAJE La partida va a comenzar.");
                
                out1.println("MENSAJE Jugador 2 conectado. La partida comienza.");

                PartidaHandler partida = new PartidaHandler(socketJugador1, socketJugador2);
                new Thread(partida).start();
                System.out.println("Nueva partida iniciada. Esperando siguientes jugadores...");
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        servidor servidor = new servidor();
        servidor.iniciarServidor();
    }
}


class PartidaHandler implements Runnable {

    private Socket socketX;
    private Socket socketO;
    private tablero tablero;
    private jugador jugadorX;
    private jugador jugadorO;
    
    private PrintWriter outX, outO;
    private BufferedReader inX, inO;
    
    private jugador jugadorActual;

    public PartidaHandler(Socket socketX, Socket socketO) {
        this.socketX = socketX;
        this.socketO = socketO;
        this.tablero = new tablero();
        this.jugadorX = new jugador("Jugador 1", celda.JUGADOR_X);
        this.jugadorO = new jugador("Jugador 2", celda.JUGADOR_O);
        this.jugadorActual = jugadorX;
    }

    @Override
    public void run() {
        try {
            inX = new BufferedReader(new InputStreamReader(socketX.getInputStream()));
            outX = new PrintWriter(socketX.getOutputStream(), true);
            
            inO = new BufferedReader(new InputStreamReader(socketO.getInputStream()));
            outO = new PrintWriter(socketO.getOutputStream(), true);

            gestionarPartida();

        } catch (IOException e) {
            System.err.println("Error en la partida (jugador desconectado): " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
    }


    public void gestionarPartida() throws IOException {
        String ganador = celda.VACIA;

        while (ganador.equals(celda.VACIA) && !tablero.esEmpate()) {
            
            notificarEstadoJuego(); 
            
            if (jugadorActual == jugadorX) {
                gestionarTurno(outX, inX, outO);
            } else {
                gestionarTurno(outO, inO, outX);
            }
            
            ganador = tablero.checkGanador();
            
            if (!ganador.equals(celda.VACIA)) {
                notificarResultado(ganador);
                break;
            }
            
            if (tablero.esEmpate()) {
                notificarResultado("EMPATE");
                break;
            }

            jugadorActual = (jugadorActual == jugadorX) ? jugadorO : jugadorX;
        }
    }
    

    private void gestionarTurno(PrintWriter outActual, BufferedReader inActual, PrintWriter outEspera) throws IOException {
        outEspera.println("ESPERA");
        outActual.println("TURNO");
        
        String lineaMovimiento;
        while ((lineaMovimiento = inActual.readLine()) != null) {
            if (lineaMovimiento.startsWith("MOVER ")) {
                try {
                    String[] coords = lineaMovimiento.substring(6).split(",");
                    int fila = Integer.parseInt(coords[0]);
                    int col = Integer.parseInt(coords[1]);
                    
                    if (validarMovimiento(fila, col)) {
                        tablero.colocarSimbolo(fila, col, jugadorActual.getSimbolo());
                        outActual.println("VALIDO");
                        break;
                    } else {
                        outActual.println("INVALIDO Casilla ocupada o fuera de rango. Intenta de nuevo.");
                        outActual.println("TURNO");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    outActual.println("INVALIDO Formato incorrecto. Envíe MOVER fila,col.");
                    outActual.println("TURNO");
                }
            }
        }
    }


    public boolean validarMovimiento(int fila, int col) {
        return fila >= 0 && fila < 3 && col >= 0 && col < 3 && 
               tablero.tablero[fila][col].esVacia();
    }
    

    public void notificarEstadoJuego() {
        String estadoProtocolo = tablero.getEstadoProtocolo();
        outX.println("ESTADO " + estadoProtocolo);
        outO.println("ESTADO " + estadoProtocolo);
    }
    

    private void notificarResultado(String resultado) {
        notificarEstadoJuego();

        if (resultado.equals("EMPATE")) {
            outX.println("EMPATE");
            outO.println("EMPATE");
        } else if (resultado.equals(jugadorX.getSimbolo())) {
            outX.println("VICTORIA");
            outO.println("DERROTA");
        } else {
            outO.println("VICTORIA");
            outX.println("DERROTA");
        }
    }
    
    private void cerrarRecursos() {
        try {
            if (socketX != null) socketX.close();
            if (socketO != null) socketO.close();
            if (inX != null) inX.close();
            if (outX != null) outX.close();
            if (inO != null) inO.close();
            if (outO != null) outO.close();
            System.out.println("Partida terminada. Recursos cerrados.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
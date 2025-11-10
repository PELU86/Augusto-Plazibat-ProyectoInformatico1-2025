package com;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class cliente {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    
    private String miSimbolo = "?";
    public void conectarConServidor(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
            
            System.out.println("Conectado al servidor en " + ip + ":" + puerto);

            String lineaServidor;
            while ((lineaServidor = in.readLine()) != null) {
                gestionarRespuestaServidor(lineaServidor);
                
                if (lineaServidor.startsWith("VICTORIA") || 
                    lineaServidor.startsWith("DERROTA") || 
                    lineaServidor.startsWith("EMPATE")) {
                    break; 
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + ip);
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor (Servidor no iniciado?): " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
    }
    
    private void gestionarRespuestaServidor(String mensaje) {
        if (mensaje.startsWith("BIENVENIDO ")) {
            miSimbolo = mensaje.substring(11);
            System.out.println("¡Bienvenido! Juegas como: " + miSimbolo);
        
        } else if (mensaje.startsWith("MENSAJE ")) {
            System.out.println("[Servidor]: " + mensaje.substring(8));
        
        } else if (mensaje.startsWith("ESTADO ")) {
            recibirEstadoJuego(mensaje.substring(7));
        
        } else if (mensaje.equals("TURNO")) {
            realizarMovimiento();
        
        } else if (mensaje.equals("ESPERA")) {
            System.out.println("Esperando el movimiento del oponente...");
        
        } else if (mensaje.startsWith("INVALIDO ")) {
            System.out.println("[Error]: " + mensaje.substring(9));
        
        } else if (mensaje.equals("VALIDO")) {
             System.out.println("Movimiento aceptado.");
        
        } else if (mensaje.equals("VICTORIA")) {
            System.out.println("\n¡¡¡FELICIDADES, GANASTE!!! 🎉");
        
        } else if (mensaje.equals("DERROTA")) {
            System.out.println("\nPERDISTE. Mejor suerte la próxima vez. 😞");
        
        } else if (mensaje.equals("EMPATE")) {
            System.out.println("\nEl juego ha terminado en EMPATE. 😐");
        }
    }

    public void realizarMovimiento() {
        int fila = -1, col = -1;
        boolean inputValido = false;
        
        while (!inputValido) {
            try {
                System.out.println("Es tu turno (" + miSimbolo + ").");
                System.out.print("Ingresa fila (0, 1, o 2): ");
                fila = scanner.nextInt();
                System.out.print("Ingresa columna (0, 1, o 2): ");
                col = scanner.nextInt();
                
                if (fila >= 0 && fila <= 2 && col >= 0 && col <= 2) {
                    inputValido = true;
                } else {
                    System.out.println("Entrada inválida. Fila y columna deben ser 0, 1, o 2.");
                }
            } catch (Exception e) {
                 System.out.println("Entrada no válida. Debes ingresar un número.");
                 scanner.next();
            }
        }
        
        out.println("MOVER " + fila + "," + col);
    }

    public void recibirEstadoJuego(String estadoProtocolo) {
        String[] celdas = estadoProtocolo.split(",");
        System.out.println("\n--- ESTADO DEL TABLERO ---");
        System.out.println("-------------");
        System.out.println("| " + celdas[0] + " | " + celdas[1] + " | " + celdas[2] + " |");
        System.out.println("-------------");
        System.out.println("| " + celdas[3] + " | " + celdas[4] + " | " + celdas[5] + " |");
        System.out.println("-------------");
        System.out.println("| " + celdas[6] + " | " + celdas[7] + " | " + celdas[8] + " |");
        System.out.println("-------------");
    }
    
    private void cerrarRecursos() {
         try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (scanner != null) scanner.close();
            System.out.println("Conexión cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String ip = "localhost";
        int puerto = 9090;
        
        cliente cliente = new cliente();
        cliente.conectarConServidor(ip, puerto);
    }
}
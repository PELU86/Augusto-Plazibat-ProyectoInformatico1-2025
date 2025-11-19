package examen2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese IP (localhost para local): ");
        String ip = scanner.nextLine();  
        System.out.print("Ingrese puerto (5000 para local): ");
        int puerto = Integer.parseInt(scanner.nextLine());
        try {
            Socket socket = new Socket(ip, puerto);
            System.out.println("Conectado al servidor " + ip + ":" + puerto);
            Thread hiloEscucha = new Thread(new EscuchaServidor(socket));
            hiloEscucha.start();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String inputUsuario = scanner.nextLine();
                out.println(inputUsuario);
                if (inputUsuario.equalsIgnoreCase("/logout")) {
                    System.out.println("Cerrando cliente...");
                    break;
                }
            }
            socket.close();
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error: No se pudo conectar al servidor. Verifique IP y Puerto.");
        }
    }
    static class EscuchaServidor implements Runnable {
        private Socket socket;

        public EscuchaServidor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensajeServidor;
                
                while ((mensajeServidor = in.readLine()) != null) {
                    System.out.println(">> " + mensajeServidor);
                }
            } catch (IOException e) {
            }
        }
    }
}

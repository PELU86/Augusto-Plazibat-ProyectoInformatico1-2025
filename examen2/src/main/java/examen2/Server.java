package examen2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static final List<ClientHandler> clientes = new CopyOnWriteArrayList<>();
    public static void main(String[] args) {
        int puerto = 5000;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("=== SERVIDOR INICIADO EN PUERTO " + puerto + " ===");
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());
                ClientHandler handler = new ClientHandler(socketCliente);
                clientes.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void broadcast(String mensaje, ClientHandler remitente) {
        for (ClientHandler cliente : clientes) {
                cliente.enviarMensaje(mensaje);
        }
    }
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("BIENVENIDO AL SERVIDOR LOCAL");
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.equalsIgnoreCase("/hi")) {
                        out.println("BIENVENIDO AL SERVIDOR LOCAL");
                    } else if (mensaje.equalsIgnoreCase("/logout")) {
                        break;
                        
                    } else if (mensaje.startsWith("/")) {
                        out.println("COMANDO DESCONOCIDO");
                        
                    } else {
                        System.out.println("Broadcasting: " + mensaje);
                        Server.broadcast(mensaje, this);
                    }
                }

            } catch (IOException e) {
                System.err.println("Error en conexión con cliente");
            } finally {
                try {
                    clientes.remove(this);
                    socket.close();
                    System.out.println("Cliente desconectado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void enviarMensaje(String msg) {
            out.println(msg);
        }
    }
}
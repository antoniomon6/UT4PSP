package com.Amalagon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTelnetDummy {

    public static void main(String[] args) {
        int puerto = 2323;

        System.out.println("Iniciando Servidor Telnet Dummy en el puerto " + puerto + "...");

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones...");

            // Aceptamos la conexión del cliente
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println("¡Cliente conectado desde " + clientSocket.getInetAddress() + "!");

                // 1. Enviar mensaje de bienvenida
                out.println("Bienvenido al Servidor Echo PSP");

                // 2. Leer una línea del cliente
                String mensajeCliente = in.readLine();
                System.out.println("Recibido del cliente: " + mensajeCliente);

                // 3. Hacer eco (devolver el mismo mensaje)
                out.println("ECO: " + mensajeCliente);

                System.out.println("Conexión con el cliente finalizada.");
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}

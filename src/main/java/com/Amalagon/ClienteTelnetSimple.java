package com.Amalagon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteTelnetSimple {

    public static void main(String[] args) {
        // Definimos el host y el puerto indicados en la práctica
        String host = "localhost";
        int puerto = 2323;

        System.out.println("Iniciando Cliente Telnet...");

        // Usamos try-with-resources para asegurar que el Socket y los Streams se cierren correctamente
        try (Socket socket = new Socket(host, puerto);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conexión establecida con " + host + ":" + puerto);

            // 1. Leer y mostrar el mensaje de bienvenida del servidor [cite: 37]
            String mensajeBienvenida = entrada.readLine();
            System.out.println("Servidor dice: " + mensajeBienvenida);

            // 2. Enviar un mensaje predefinido al servidor [cite: 38]
            String mensajeEnviar = "Saludos desde PSP";
            System.out.println("Cliente envía: " + mensajeEnviar);
            salida.println(mensajeEnviar);

            // 3. Leer y mostrar la respuesta (eco) del servidor [cite: 39]
            String respuestaServidor = entrada.readLine();
            System.out.println("Respuesta (Eco) del servidor: " + respuestaServidor);

            System.out.println("Cerrando la conexión...");

        } catch (UnknownHostException e) {
            System.err.println("Error: Host desconocido - " + e.getMessage());
        } catch (IOException e) {
            // Manejo de la excepción si el servidor no está corriendo o hay un fallo de red
            System.err.println("Error de E/S al conectar con el servidor. ¿Está el servidor Dummy ejecutándose en el puerto " + puerto + "?");
            System.err.println("Detalle del error: " + e.getMessage());
        }
    }
}

package com.Amalagon;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteHttpBiblioteca {

    public static void main(String[] args) {
        // Asegúrate de que tu aplicación Spring Boot 'biblioluis' esté corriendo en este puerto
        String url = "http://localhost:8083/libros"; [cite: 95]

        System.out.println("Iniciando Cliente HTTP...");
        System.out.println("Realizando petición GET a: " + url);

        try {
            // 1. Crear el cliente
            HttpClient client = HttpClient.newHttpClient(); [cite: 94]

            // 2. Construir la petición GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET() [cite: 95]
                    .build();

            // 3. Enviar la petición y manejar la respuesta como String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Verificar el código de estado (debe ser 200)
            int statusCode = response.statusCode();
            System.out.println("Código de estado HTTP: " + statusCode); [cite: 96]

            if (statusCode == 200) { [cite: 96]
                System.out.println("¡Petición exitosa!\n");

                // Obtener el cuerpo de la respuesta
                String body = response.body(); [cite: 97]

                System.out.println("--- Primeros 500 caracteres del HTML ---");
                // Imprimir los primeros 500 caracteres (o menos si es más corto)
                if (body.length() > 500) { [cite: 99]
                    System.out.println(body.substring(0, 500)); [cite: 99]
                    System.out.println("\n[... truncado ...]");
                } else {
                    System.out.println(body);
                }
                System.out.println("----------------------------------------");

            } else {
                System.err.println("Error en la petición. Código inesperado.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al conectar con el servidor web.");
            System.err.println("¿Está la aplicación Spring Boot ejecutándose en el puerto 8083?");
            System.err.println("Detalle: " + e.getMessage());
        }
    }
}

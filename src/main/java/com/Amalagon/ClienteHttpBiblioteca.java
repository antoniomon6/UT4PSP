package com.Amalagon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ClienteHttpBiblioteca {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Cliente HTTP (Java 11+) ===\n");

        // Creamos una única instancia del cliente HTTP para reusarla
        HttpClient client = HttpClient.newHttpClient();

        // ---------------------------------------------------------
        // OPCIÓN 1: Petición HTML
        // ---------------------------------------------------------
        String urlHtml = "http://localhost:8093/libros"; // Endpoint para HTML
        System.out.println("--- 1. Probando endpoint HTML ---");
        System.out.println("GET a: " + urlHtml);

        try {
            HttpRequest requestHtml = HttpRequest.newBuilder()
                    .uri(URI.create(urlHtml))
                    .GET()
                    .build(); //

            HttpResponse<String> responseHtml = client.send(requestHtml, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado HTTP: " + responseHtml.statusCode()); //

            if (responseHtml.statusCode() == 200) { //
                String bodyHtml = responseHtml.body(); //
                System.out.println("Cuerpo de la respuesta (primeros 500 caracteres):"); //

                if (bodyHtml.length() > 500) {
                    System.out.println(bodyHtml.substring(0, 500) + "\n... [HTML TRUNCADO]"); //
                } else {
                    System.out.println(bodyHtml);
                }
            } else {
                System.err.println("Error al obtener HTML. Código: " + responseHtml.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error en la petición HTML: " + e.getMessage());
        }

        System.out.println("\n=================================================\n");

        // ---------------------------------------------------------
        // OPCIÓN 2: Petición JSON
        // ---------------------------------------------------------
        String urlJson = "http://localhost:8093/api/libros"; // Endpoint para JSON
        System.out.println("--- 2. Probando endpoint REST (JSON) ---");
        System.out.println("GET a: " + urlJson);

        try {
            HttpRequest requestJson = HttpRequest.newBuilder()
                    .uri(URI.create(urlJson))
                    .header("Accept", "application/json")
                    .GET()
                    .build(); //

            HttpResponse<String> responseJson = client.send(requestJson, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado HTTP: " + responseJson.statusCode()); //

            if (responseJson.statusCode() == 200) { //
                String bodyJson = responseJson.body(); //

                // Parsear usando Gson
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<List<Map<String, Object>>>(){}.getType(); //
                List<Map<String, Object>> libros = gson.fromJson(bodyJson, tipoLista); //

                System.out.println("\n📚 Títulos obtenidos del JSON:"); //
                System.out.println("----------------------------------");
                if (libros != null && !libros.isEmpty()) {
                    for (int i = 0; i < libros.size(); i++) {
                        Map<String, Object> libro = libros.get(i);
                        // Asumimos que la API devuelve la clave "titulo" (o "title" dependiendo de tu app Spring Boot)
                        Object titulo = libro.get("titulo");
                        System.out.println((i + 1) + ". " + titulo);
                    }
                } else {
                    System.out.println("La lista JSON está vacía.");
                }
                System.out.println("----------------------------------");
            } else {
                System.err.println("Error al obtener JSON. Código: " + responseJson.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error en la petición JSON: " + e.getMessage());
            System.err.println("¿Has implementado el endpoint /api/libros en tu app Spring Boot?");
        }
    }
}

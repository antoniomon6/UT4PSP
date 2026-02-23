package com.Amalagon;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClienteFtpOperaciones {

    public static void main(String[] args) {
        String server = "localhost";
        int port = 21;
        String user = "psp_user"; // Cambia esto por tu usuario de prueba
        String pass = "psp_pass"; // Cambia esto por tu contraseña de prueba

        FTPClient ftpClient = new FTPClient();

        try {
            // Conectar y loguearse
            ftpClient.connect(server, port);
            int reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("El servidor FTP rechazó la conexión.");
                return;
            }

            boolean success = ftpClient.login(user, pass);
            if (!success) {
                System.err.println("Error de autenticación. Verifica usuario y contraseña.");
                return;
            }
            System.out.println("¡Conectado exitosamente al servidor FTP!");

            // Entrar en modo pasivo y tipo binario
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Mostrar el directorio de trabajo actual
            String cwd = ftpClient.printWorkingDirectory();
            System.out.println("Directorio actual: " + cwd);

            // Listar ficheros y directorios
            listarArchivos(ftpClient);

            // Crear archivo localmente
            String localFileName = "psp_test.txt";
            Files.writeString(Paths.get(localFileName), "Hola FTP");
            System.out.println("\nArchivo local '" + localFileName + "' creado con éxito.");

            // Subir archivo al servidor
            System.out.println("Subiendo archivo al servidor...");
            try (InputStream inputStream = new FileInputStream(localFileName)) {
                boolean uploaded = ftpClient.storeFile(localFileName, inputStream);
                if (uploaded) {
                    System.out.println("Archivo subido exitosamente.");
                } else {
                    System.err.println("Error al subir el archivo.");
                }
            }

            // Listar de nuevo para verificar
            System.out.println("\nVerificando archivos en el servidor tras la subida:");
            listarArchivos(ftpClient);

            // Descargar el archivo subido con otro nombre
            String downloadFileName = "psp_downloaded.txt";
            System.out.println("\nDescargando archivo como '" + downloadFileName + "'...");
            try (OutputStream outputStream = new FileOutputStream(downloadFileName)) {
                boolean downloaded = ftpClient.retrieveFile(localFileName, outputStream);
                if (downloaded) {
                    System.out.println("Archivo descargado exitosamente.");
                } else {
                    System.err.println("Error al descargar el archivo.");
                }
            }

            // Hacer logout
            ftpClient.logout();
            System.out.println("\nDesconectado del servidor FTP.");

        } catch (IOException ex) {
            System.err.println("Error de E/S: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Método auxiliar para listar archivos
    private static void listarArchivos(FTPClient ftpClient) throws IOException {
        System.out.println("--- Contenido del directorio ---");
        FTPFile[] files = ftpClient.listFiles();
        if (files != null && files.length > 0) {
            for (FTPFile file : files) {
                String tipo = file.isDirectory() ? "[DIRECTORIO]" : "[FICHERO]";
                System.out.println(tipo + " " + file.getName());
            }
        } else {
            System.out.println("El directorio está vacío.");
        }
        System.out.println("--------------------------------");
    }
}
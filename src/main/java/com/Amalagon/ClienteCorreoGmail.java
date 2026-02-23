package com.Amalagon;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class ClienteCorreoGmail {

    // Configura aquí tus credenciales (¡Recuerda no subir este archivo a un repositorio público con tus contraseñas!)
    private static final String USUARIO = "tu_correo@gmail.com";
    private static final String APP_PASSWORD = "tu_contraseña_de_aplicacion";
    private static final String DESTINATARIO = "tu_correo@gmail.com"; // Puede ser a ti mismo

    public static void main(String[] args) {
        System.out.println("--- Iniciando Cliente de Correo ---");

        // 1. Envío (SMTP)
        enviarCorreo();

        // Pausa de 5 segundos para dar tiempo a que el correo llegue a la bandeja de entrada
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        // 2. Recepción (POP3)
        leerCorreo();
    }

    private static void enviarCorreo() {
        System.out.println("\n[1] Intentando enviar correo mediante SMTP...");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Requerido por Gmail
        props.put("mail.smtp.host", "smtp.gmail.com"); [cite: 70]
        props.put("mail.smtp.port", "587"); // Puerto estándar para STARTTLS

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USUARIO, APP_PASSWORD); [cite: 71]
            }
        });

        try {
            Message message = new MimeMessage(session); [cite: 72]
            message.setFrom(new InternetAddress(USUARIO)); [cite: 73]
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(DESTINATARIO)); [cite: 73]
            message.setSubject("Prueba PSP Correo"); [cite: 74]
            message.setText("Mensaje de prueba desde Java - SMTP OK"); [cite: 74, 75]

            Transport.send(message); [cite: 76]
            System.out.println("✅ Correo enviado con éxito."); [cite: 77]

        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar el correo: " + e.getMessage()); [cite: 77]
        }
    }

    private static void leerCorreo() {
        System.out.println("\n[2] Intentando leer correos mediante POP3...");

        Properties props = new Properties();
        props.put("mail.store.protocol", "pop3s");
        props.put("mail.pop3s.host", "pop.gmail.com"); [cite: 79]
        props.put("mail.pop3s.port", "995"); [cite: 79]
        props.put("mail.pop3s.ssl.enable", "true"); [cite: 79]

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore("pop3s"); [cite: 80]
            store.connect(USUARIO, APP_PASSWORD); [cite: 80, 81]

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages(); [cite: 82]
            System.out.println("Bandeja de entrada: " + messages.length + " mensajes.");

            // Buscamos el mensaje enviado desde el final (los más recientes)
            boolean encontrado = false;
            for (int i = messages.length - 1; i >= 0; i--) {
                Message msg = messages[i];
                if (msg.getSubject() != null && msg.getSubject().equals("Prueba PSP Correo")) { [cite: 82, 83]
                    System.out.println("\n✉️ ¡Mensaje de prueba encontrado!");
                    System.out.println("Remitente: " + msg.getFrom()[0]); [cite: 84]
                    System.out.println("Asunto: " + msg.getSubject()); [cite: 84]
                    System.out.println("Contenido:\n" + msg.getContent().toString()); [cite: 84]
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("No se encontró el mensaje de prueba. Puede que tarde unos minutos en llegar.");
            }

            folder.close(false);
            store.close(); [cite: 84]
            System.out.println("\nConexión POP3 cerrada.");

        } catch (Exception e) {
            System.err.println("❌ Error al leer el correo: " + e.getMessage());
        }
    }
}

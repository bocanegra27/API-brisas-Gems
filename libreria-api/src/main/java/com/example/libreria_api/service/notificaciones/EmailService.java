package com.example.libreria_api.service.notificaciones;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envía un correo HTML para recuperación de contraseña
     */
    public void enviarCorreoRecuperacion(String destinatario, String nombreUsuario, String token) {
        try {
            // Crear el mensaje
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Recuperación de Contraseña - Brisas Gems");

            // Construir el enlace
            String link = "http://127.0.0.1:8000/restablecer/" + token;

            // Contenido HTML del correo
            String htmlContent = """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>
                    <h2 style='color: #009688; text-align: center;'>Brisas Gems</h2>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Hemos recibido una solicitud para restablecer tu contraseña.</p>
                    <p>Haz clic en el siguiente botón para continuar:</p>
                    <div style='text-align: center; margin: 30px 0;'>
                        <a href='%s' style='background-color: #009688; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Restablecer Contraseña</a>
                    </div>
                    <p style='font-size: 12px; color: #777;'>Si no solicitaste este cambio, puedes ignorar este correo.</p>
                    <hr style='border: 0; border-top: 1px solid #eee;'>
                    <p style='font-size: 10px; color: #aaa; text-align: center;'>&copy; 2024 Brisas Gems</p>
                </div>
            """.formatted(nombreUsuario, link);

            helper.setText(htmlContent, true); // true indica que es HTML

            // Enviar
            mailSender.send(message);
            System.out.println("✅ Correo enviado exitosamente a: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("❌ Error enviando correo: " + e.getMessage());
            // Opcional: Lanzar excepción si quieres que el controlador se entere
        }
    }
}
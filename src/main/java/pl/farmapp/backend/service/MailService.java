package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBackupEmail(String email, String json) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);

            helper.setSubject("Kopia zapasowa danych – Asystent producenta papryki 2.0");

            helper.setText("""
                    Dzień dobry,

                    w załączniku znajduje się kopia zapasowa Twoich danych z aplikacji
                    Asystent producenta papryki 2.0.

                    Możesz zachować ten plik jako backup swoich danych gospodarstwa.

                    Z pozdrowieniami
                    Zespół Asystenta producenta papryki 2.0
                    """);

            helper.addAttachment(
                    "asystent-producenta-papryki-backup.json",
                    new ByteArrayResource(json.getBytes())
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Błąd wysyłki maila", e);
        }
    }
}
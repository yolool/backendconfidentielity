package org.example.confidentialite;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.confidentialite.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEngagementEmailAsync_shouldSendEmail() throws Exception {

        // Arrange
        byte[] fileBytes = "test document".getBytes();
        String filename = "engagement.pdf";
        String subject = "Document confidentiel";

        when(mailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        // Act
        assertDoesNotThrow(() ->
                emailService.sendEngagementEmailAsync(
                        fileBytes,
                        filename,
                        subject
                )
        );

        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEngagementEmailAsync_shouldUseDefaultFilenameWhenFilenameIsNull()
            throws Exception {

        // Arrange
        byte[] fileBytes = "test document".getBytes();

        when(mailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        // Act
        assertDoesNotThrow(() ->
                emailService.sendEngagementEmailAsync(
                        fileBytes,
                        null,
                        "Test"
                )
        );

        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEngagementEmailAsync_shouldThrowExceptionWhenMailFails()
            throws Exception {

        // Arrange
        byte[] fileBytes = "test document".getBytes();

        when(mailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        doThrow(new RuntimeException("Mail server error"))
                .when(mailSender)
                .send(mimeMessage);

        // Act + Assert
        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> emailService.sendEngagementEmailAsync(
                                fileBytes,
                                "test.pdf",
                                "Test"
                        )
                );

        // Assert
        org.junit.jupiter.api.Assertions.assertEquals(
                "Mail server error",
                exception.getMessage()
        );

        verify(mailSender).send(mimeMessage);
    }
}

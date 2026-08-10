package org.example.confidentialite;



import org.example.confidentialite.Dto.LoginResDto;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Repository.PersonnelRepo;

import org.example.confidentialite.Service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PersonnelRepo personnelRepo;

    @InjectMocks
    private AuthService authService;


    @Test
    void loadUserByUsername_shouldReturnUser() {

            Personnel personnel = new Personnel();
        personnel.setIdPersonnel("P001");
        personnel.setDepartment("ADMIN");

        when(personnelRepo.findById("P001"))
                .thenReturn(Optional.of(personnel));

        UserDetails result =
                authService.loadUserByUsername("P001");

        assertNotNull(result);
        assertEquals("P001", result.getUsername());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority().equals("ADMIN")
                        )
        );

        verify(personnelRepo).findById("P001");
    }


    @Test
    void loadUserByUsername_shouldThrowExceptionWhenNotFound() {

        when(personnelRepo.findById("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> authService.loadUserByUsername("UNKNOWN")
        );

        verify(personnelRepo).findById("UNKNOWN");
    }


    @Test
    void login_shouldReturnLoginResponse() {

        // Arrange
        Personnel personnel = new Personnel();
        personnel.setIdPersonnel("P001");

        when(personnelRepo.findByIdInDep("P001", "ADMIN"))
                .thenReturn(Optional.of(personnel));

        // Act
        LoginResDto result =
                authService.login("P001", "ADMIN");

        // Assert
        assertNotNull(result);

        verify(personnelRepo)
                .findByIdInDep("P001", "ADMIN");
    }


    @Test
    void login_shouldThrowExceptionWhenNotFound() {

        // Arrange
        when(personnelRepo.findByIdInDep("P001", "ADMIN"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> authService.login("P001", "ADMIN")
        );

        verify(personnelRepo)
                .findByIdInDep("P001", "ADMIN");
    }
}
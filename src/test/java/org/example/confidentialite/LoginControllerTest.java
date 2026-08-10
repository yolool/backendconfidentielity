package org.example.confidentialite;

import org.example.confidentialite.Controller.LoginController;
import org.example.confidentialite.Dto.LoginResDto;
import org.example.confidentialite.Service.AuthService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private SecurityContextRepository securityContextRepository;


    @Test
    void login_shouldReturn200() throws Exception {

        LoginResDto response =
                new LoginResDto("P001");

        Authentication authentication =
                mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authService.login("P001", "ADMIN"))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "idPersonnel": "P001",
                                "dep": "ADMIN"
                            }
                            """)
                )
                .andExpect(status().isOk());

        verify(authenticationManager)
                .authenticate(any());

        verify(authService)
                .login("P001", "ADMIN");
    }


    @Test
    void login_shouldReturnErrorWhenAuthenticationFails()
            throws Exception {

        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new RuntimeException("Authentication failed")
                );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "idPersonnel": "UNKNOWN",
                                "dep": "ADMIN"
                            }
                            """)
                )
                .andExpect(status().isInternalServerError());

        verify(authenticationManager)
                .authenticate(any());

        verifyNoInteractions(authService);
    }
}
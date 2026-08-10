package org.example.confidentialite;


import org.example.confidentialite.Controller.EngagementController;
import org.example.confidentialite.Dto.EngagementDto;
import org.example.confidentialite.Service.EmailService;
import org.example.confidentialite.Service.EngagementService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EngagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class EngagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EngagementService engagementService;

    @MockitoBean
    private EmailService emailService;


    @Test
    void getAllEngagements_shouldReturn200() throws Exception {

        EngagementDto engagement = mock(EngagementDto.class);

        when(engagementService.findEngagements())
                .thenReturn(List.of(engagement));

        mockMvc.perform(get("/api/v1/engagement"))
                .andExpect(status().isOk());

        verify(engagementService)
                .findEngagements();
    }


    @Test
    void getAllEngagements_shouldReturn500WhenServiceFails()
            throws Exception {

        when(engagementService.findEngagements())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/engagement"))
                .andExpect(status().isInternalServerError());

        verify(engagementService)
                .findEngagements();
    }


    @Test
    void getEngagementById_shouldReturn200() throws Exception {

        EngagementDto engagement = mock(EngagementDto.class);

        when(engagementService.findEngagementById(1L))
                .thenReturn(engagement);

        mockMvc.perform(
                        get("/api/v1/engagement/1")
                )
                .andExpect(status().isOk());

        verify(engagementService)
                .findEngagementById(1L);
    }


    @Test
    void getEngagementById_shouldReturn500WhenServiceFails()
            throws Exception {

        when(engagementService.findEngagementById(1L))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(
                        get("/api/v1/engagement/1")
                )
                .andExpect(status().isInternalServerError());

        verify(engagementService)
                .findEngagementById(1L);
    }


    @Test
    void deleteEngagement_shouldReturn200() throws Exception {

        doNothing()
                .when(engagementService)
                .DeleteEngagement(1L);

        mockMvc.perform(
                        delete("/api/v1/engagement/1")
                )
                .andExpect(status().isOk());

        verify(engagementService)
                .DeleteEngagement(1L);
    }


    @Test
    void deleteEngagement_shouldReturn500WhenServiceFails()
            throws Exception {

        doThrow(new RuntimeException("Delete error"))
                .when(engagementService)
                .DeleteEngagement(1L);

        mockMvc.perform(
                        delete("/api/v1/engagement/1")
                )
                .andExpect(status().isInternalServerError());

        verify(engagementService)
                .DeleteEngagement(1L);
    }


    @Test
    void getEngagementByPersonalId_shouldReturn200()
            throws Exception {

        EngagementDto engagement = mock(EngagementDto.class);

        when(
                engagementService.FindEngagementByIdPersonnel("P001")
        ).thenReturn(engagement);

        mockMvc.perform(
                        get("/api/v1/engagement/perso/P001")
                )
                .andExpect(status().isOk());

        verify(
                engagementService
        ).FindEngagementByIdPersonnel("P001");
    }


    @Test
    void uploadEngagement_shouldReturn201()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "engagement.pdf",
                        "application/pdf",
                        "test document".getBytes()
                );

        EngagementDto dto = mock(EngagementDto.class);

        when(
                engagementService.UploadEngagement(
                        any(),
                        eq("P001")
                )
        ).thenReturn(dto);

        mockMvc.perform(
                        multipart("/api/v1/engagement/upload")
                                .file(file)
                                .param("idPersonnel", "P001")
                                .param("subject", "Test engagement")
                )
                .andExpect(status().isCreated());

        verify(
                engagementService
        ).UploadEngagement(any(), eq("P001"));

        verify(
                emailService
        ).sendEngagementEmailAsync(
                any(),
                eq("engagement.pdf"),
                eq("Test engagement")
        );
    }


    @Test
    void uploadEngagement_shouldReturn400WhenFileIsEmpty()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "",
                        "application/pdf",
                        new byte[0]
                );

        mockMvc.perform(
                        multipart("/api/v1/engagement/upload")
                                .file(file)
                                .param("subject", "Test")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(engagementService);
        verifyNoInteractions(emailService);
    }
}

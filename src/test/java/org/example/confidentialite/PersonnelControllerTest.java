package org.example.confidentialite;


import org.example.confidentialite.Controller.PersonnelController;
import org.example.confidentialite.Dto.DepartementDto;
import org.example.confidentialite.Dto.PersonnelDto;
import org.example.confidentialite.Service.PersonnelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonnelController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonnelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonnelService personnelService;


    @Test
    void findAllPersonnels_shouldReturn200() throws Exception {

        PersonnelDto personnel = mock(PersonnelDto.class);

        when(personnelService.findAllPersonnels())
                .thenReturn(List.of(personnel));

        mockMvc.perform(
                        get("/api/v1/Personnel")
                )
                .andExpect(status().isOk());

        verify(personnelService)
                .findAllPersonnels();
    }


    @Test
    void findAllPersonnels_shouldReturn500WhenServiceFails()
            throws Exception {

        when(personnelService.findAllPersonnels())
                .thenThrow(
                        new RuntimeException("Database error")
                );

        mockMvc.perform(
                        get("/api/v1/Personnel")
                )
                .andExpect(status().isInternalServerError());

        verify(personnelService)
                .findAllPersonnels();
    }


    @Test
    void findAllDepartement_shouldReturn200()
            throws Exception {

        DepartementDto department =
                mock(DepartementDto.class);

        when(personnelService.findAllDepartments())
                .thenReturn(List.of(department));

        mockMvc.perform(
                        get("/api/v1/Personnel/deps")
                )
                .andExpect(status().isOk());

        verify(personnelService)
                .findAllDepartments();
    }


    @Test
    void findAllDepartement_shouldReturn500WhenServiceFails()
            throws Exception {

        when(personnelService.findAllDepartments())
                .thenThrow(
                        new RuntimeException("Database error")
                );

        mockMvc.perform(
                        get("/api/v1/Personnel/deps")
                )
                .andExpect(status().isInternalServerError());

        verify(personnelService)
                .findAllDepartments();
    }


    @Test
    void findPersonnel_shouldReturn200()
            throws Exception {

        PersonnelDto personnel =
                mock(PersonnelDto.class);

        when(personnelService.findPersonnelById("P001"))
                .thenReturn(personnel);

        mockMvc.perform(
                        get("/api/v1/Personnel/P001")
                )
                .andExpect(status().isOk());

        verify(personnelService)
                .findPersonnelById("P001");
    }


    @Test
    void findPersonnel_shouldReturn500WhenServiceFails()
            throws Exception {

        when(personnelService.findPersonnelById("P001"))
                .thenThrow(
                        new RuntimeException("Personnel not found")
                );

        mockMvc.perform(
                        get("/api/v1/Personnel/P001")
                )
                .andExpect(status().isInternalServerError());

        verify(personnelService)
                .findPersonnelById("P001");
    }
}

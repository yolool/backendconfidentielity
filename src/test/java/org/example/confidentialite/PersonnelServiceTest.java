package org.example.confidentialite;


import org.example.confidentialite.Dto.DepartementDto;
import org.example.confidentialite.Dto.PersonnelDto;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Mapping.PersonnelMapper;
import org.example.confidentialite.Repository.PersonnelRepo;
import org.example.confidentialite.Service.PersonnelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonnelServiceTest {

    @Mock
    private PersonnelRepo personnelRepo;

    @Mock
    private PersonnelMapper personnelMapper;

    @InjectMocks
    private PersonnelService personnelService;

    private Personnel personnel;
    private PersonnelDto personnelDto;

    @BeforeEach
    void setUp() {
        personnel = new Personnel();

        personnel.setIdPersonnel("P001");

        personnelDto = mock(PersonnelDto.class);
    }

    @Test
    void findPersonnelById_shouldReturnPersonnel() {

        // Arrange
        when(personnelRepo.findById("P001"))
                .thenReturn(Optional.of(personnel));

        when(personnelMapper.apply(personnel))
                .thenReturn(personnelDto);

        // Act
        PersonnelDto result =
                personnelService.findPersonnelById("P001");

        // Assert
        assertNotNull(result);
        assertEquals(personnelDto, result);

        verify(personnelRepo).findById("P001");
        verify(personnelMapper).apply(personnel);
    }

    @Test
    void findPersonnelById_shouldThrowExceptionWhenNotFound() {

        // Arrange
        when(personnelRepo.findById("UNKNOWN"))
                .thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> personnelService.findPersonnelById("UNKNOWN")
                );

        assertEquals(
                "No Personnel found with id: \" + id",
                exception.getMessage()
        );

        verify(personnelRepo).findById("UNKNOWN");
        verifyNoInteractions(personnelMapper);
    }

    @Test
    void findAllPersonnels_shouldReturnList() {

        // Arrange
        Personnel personnel2 = new Personnel();

        when(personnelRepo.findAll())
                .thenReturn(List.of(personnel, personnel2));

        when(personnelMapper.apply(personnel))
                .thenReturn(personnelDto);

        PersonnelDto personnelDto2 =
                mock(PersonnelDto.class);

        when(personnelMapper.apply(personnel2))
                .thenReturn(personnelDto2);

        // Act
        List<PersonnelDto> result =
                personnelService.findAllPersonnels();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(personnelDto, result.get(0));
        assertEquals(personnelDto2, result.get(1));

        verify(personnelRepo).findAll();
        verify(personnelMapper).apply(personnel);
        verify(personnelMapper).apply(personnel2);
    }

    @Test
    void findAllPersonnels_shouldReturnEmptyList() {

        // Arrange
        when(personnelRepo.findAll())
                .thenReturn(List.of());

        // Act
        List<PersonnelDto> result =
                personnelService.findAllPersonnels();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(personnelRepo).findAll();
        verifyNoInteractions(personnelMapper);
    }

    @Test
    void findAllDepartments_shouldReturnList() {

        // Arrange
        List<String> departments =
                List.of("ADMIN", "IT");

        when(personnelRepo.findAllDepartement())
                .thenReturn(departments);

        // Act
        List<DepartementDto> result =
                personnelService.findAllDepartments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(personnelRepo).findAllDepartement();
    }

    @Test
    void findAllDepartments_shouldReturnEmptyList() {

        // Arrange
        when(personnelRepo.findAllDepartement())
                .thenReturn(List.of());

        // Act
        List<DepartementDto> result =
                personnelService.findAllDepartments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(personnelRepo).findAllDepartement();
    }
}
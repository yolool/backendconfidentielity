package org.example.confidentialite;


import org.example.confidentialite.Dto.EngagementDto;
import org.example.confidentialite.Entity.Engagement;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Mapping.EngagementMapper;
import org.example.confidentialite.Repository.EngagementRepo;
import org.example.confidentialite.Repository.PersonnelRepo;
import org.example.confidentialite.Service.EngagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EngagementServiceTest {

    @Mock
    private EngagementRepo engagementRepo;

    @Mock
    private EngagementMapper engagementMapper;

    @Mock
    private PersonnelRepo personnelRepo;

    @InjectMocks
    private EngagementService engagementService;

    @Test
    void findEngagements_shouldReturnList() {

        Engagement engagement = new Engagement();
        EngagementDto dto = mock(EngagementDto.class);

        when(engagementRepo.findAll())
                .thenReturn(List.of(engagement));

        when(engagementMapper.apply(engagement))
                .thenReturn(dto);

        List<EngagementDto> result =
                engagementService.findEngagements();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(engagementRepo).findAll();
        verify(engagementMapper).apply(engagement);
    }

    @Test
    void findEngagements_shouldReturnEmptyList() {

        when(engagementRepo.findAll())
                .thenReturn(List.of());

        List<EngagementDto> result =
                engagementService.findEngagements();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(engagementRepo).findAll();
        verifyNoInteractions(engagementMapper);
    }

    @Test
    void getEngagement_shouldReturnEngagement() {

        Engagement engagement = new Engagement();

        when(engagementRepo.findById(1L))
                .thenReturn(Optional.of(engagement));

        Engagement result =
                engagementService.getEngagement(1L);

        assertNotNull(result);
        assertEquals(engagement, result);

        verify(engagementRepo).findById(1L);
    }

    @Test
    void getEngagement_shouldThrowExceptionWhenNotFound() {

        when(engagementRepo.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> engagementService.getEngagement(1L)
                );

        assertEquals(
                "No engagement found with id: 1",
                exception.getMessage()
        );
    }

    @Test
    void findEngagementById_shouldReturnDto() {

        Engagement engagement = new Engagement();
        EngagementDto dto = mock(EngagementDto.class);

        when(engagementRepo.findById(1L))
                .thenReturn(Optional.of(engagement));

        when(engagementMapper.apply(engagement))
                .thenReturn(dto);

        EngagementDto result =
                engagementService.findEngagementById(1L);

        assertEquals(dto, result);

        verify(engagementRepo).findById(1L);
        verify(engagementMapper).apply(engagement);
    }

    @Test
    void findEngagementById_shouldThrowExceptionWhenNotFound() {

        when(engagementRepo.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> engagementService.findEngagementById(1L)
                );

        assertEquals(
                "No engagement found with id: 1",
                exception.getMessage()
        );
    }

    @Test
    void deleteEngagement_shouldCallRepository() {

        engagementService.DeleteEngagement(1L);

        verify(engagementRepo).deleteById(1L);
    }

    @Test
    void findEngagementByPersonnel_shouldReturnDto() {

        Engagement engagement = new Engagement();
        engagement.setStatut("signed");

        when(engagementRepo.findByIdPersonnel("P001"))
                .thenReturn(Optional.of(engagement));

        EngagementDto result =
                engagementService.FindEngagementByIdPersonnel("P001");

        assertNotNull(result);

        verify(engagementRepo)
                .findByIdPersonnel("P001");
    }

    @Test
    void findEngagementByPersonnel_shouldThrowExceptionWhenNotFound() {

        when(engagementRepo.findByIdPersonnel("P001"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> engagementService
                                .FindEngagementByIdPersonnel("P001")
                );

        assertEquals(
                "No engagement found with id: P001",
                exception.getMessage()
        );
    }

    @Test
    void uploadEngagement_shouldUploadFile() throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "engagement.pdf",
                        "application/pdf",
                        "test content".getBytes()
                );

        Personnel personnel = new Personnel();
        personnel.setIdPersonnel("P001");

        EngagementDto dto = mock(EngagementDto.class);

        when(personnelRepo.findById("P001"))
                .thenReturn(Optional.of(personnel));

        when(engagementRepo.save(any(Engagement.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(engagementMapper.apply(any(Engagement.class)))
                .thenReturn(dto);

        EngagementDto result =
                engagementService.UploadEngagement(
                        file,
                        "P001"
                );

        assertNotNull(result);
        assertEquals(dto, result);

        verify(personnelRepo)
                .findById("P001");

        verify(engagementRepo, times(2))
                .save(any(Engagement.class));

        verify(engagementMapper)
                .apply(any(Engagement.class));
    }

    @Test
    void uploadEngagement_shouldThrowExceptionWhenPersonnelNotFound()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.pdf",
                        "application/pdf",
                        "content".getBytes()
                );

        when(personnelRepo.findById("UNKNOWN"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> engagementService.UploadEngagement(
                                file,
                                "UNKNOWN"
                        )
                );

        assertEquals(
                "No personnel found with id: UNKNOWN",
                exception.getMessage()
        );

        verify(personnelRepo)
                .findById("UNKNOWN");

        verifyNoInteractions(engagementMapper);
    }
}

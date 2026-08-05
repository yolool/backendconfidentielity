package org.example.confidentialite.Dto;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public record PersonnelDto(
        String IdPersonnel,
        String Name,
        String Department,
        LocalDateTime date,
        LocalDateTime updatedAt
) {
}

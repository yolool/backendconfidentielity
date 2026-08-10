package org.example.confidentialite.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.example.confidentialite.Entity.Personnel;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public record EngagementDto(
         Long id,
         String name,
         String type,
         String statut,
         String url,
         byte[] Document,
         String IdPersonnel,
         LocalDateTime date,
         LocalDateTime updatedAt
) {
}

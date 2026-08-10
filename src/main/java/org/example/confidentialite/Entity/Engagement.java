package org.example.confidentialite.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Engagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Name;
    private String type;
    private String statut;
    private String url;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] Document;
    @CurrentTimestamp
    private LocalDateTime date;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToOne
    @JoinColumn(name = "idPersonnel")
    private Personnel personnel;


}

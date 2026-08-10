package org.example.confidentialite.Mapping;

import org.example.confidentialite.Dto.EngagementDto;
import org.example.confidentialite.Entity.Engagement;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EngagementMapper implements Function<Engagement, EngagementDto> {
    @Override
    public EngagementDto apply(Engagement engagement) {
        return new EngagementDto(
                engagement.getId(),
                engagement.getName(),
                engagement.getType(),
                engagement.getStatut(),
                engagement.getUrl(),
                null,
                engagement.getPersonnel() != null ? engagement.getPersonnel().getIdPersonnel() :null,
                engagement.getDate(),
                engagement.getUpdatedAt()
        );
    }

    public Engagement toEntity(EngagementDto dto) {
          Engagement engagement = new Engagement();


          engagement.setName(dto.name());
          engagement.setType(dto.type());
          engagement.setStatut(dto.statut());
          engagement.setUrl(dto.url());
          engagement.setDocument(dto.Document());
          return engagement;

    }
}

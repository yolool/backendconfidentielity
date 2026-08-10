package org.example.confidentialite.Mapping;

import org.example.confidentialite.Dto.PersonnelDto;
import org.example.confidentialite.Entity.Personnel;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PersonnelMapper implements Function<Personnel, PersonnelDto> {
    @Override
    public PersonnelDto apply(Personnel personnel) {
        return new PersonnelDto(
                personnel.getIdPersonnel(),
                personnel.getName(),
                personnel.getDepartment(),
                personnel.getDate(),
                personnel.getUpdatedAt()

        );
    }
    public Personnel ToEntity(PersonnelDto dto) {
        Personnel personnel = new Personnel();
        personnel.setIdPersonnel(dto.IdPersonnel());
        personnel.setName(dto.Name());
        personnel.setDepartment(dto.Department());
        return personnel;
    }

}

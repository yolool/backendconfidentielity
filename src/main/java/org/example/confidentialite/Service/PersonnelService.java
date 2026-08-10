package org.example.confidentialite.Service;

import lombok.AllArgsConstructor;
import org.example.confidentialite.Dto.DepartementDto;
import org.example.confidentialite.Dto.PersonnelDto;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Mapping.PersonnelMapper;
import org.example.confidentialite.Repository.PersonnelRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonnelService {
    private PersonnelMapper personnelMapper;
    private PersonnelRepo personnelRepo;


    public PersonnelDto findPersonnelById(String id) {
        Personnel personnel = personnelRepo.findById(id).orElseThrow(() -> new RuntimeException("No Personnel found with id: \" + id"));
        return personnelMapper.apply(personnel);
    }

    public List<PersonnelDto> findAllPersonnels() {
        return personnelRepo.findAll().stream()
                .map(personnelMapper).collect(Collectors.toList());

    }

    public List<DepartementDto> findAllDepartments() {
        return personnelRepo.findAllDepartement().stream()
                .map(DepartementDto::new)
                .toList();

    }


}
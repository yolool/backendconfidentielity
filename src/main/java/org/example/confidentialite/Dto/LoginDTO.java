package org.example.confidentialite.Dto;

import lombok.Data;
import lombok.Getter;

public record LoginDTO(
        String idPersonnel ,
        String dep
) {


}

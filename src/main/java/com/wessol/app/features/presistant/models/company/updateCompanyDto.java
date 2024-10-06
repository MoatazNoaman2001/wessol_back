package com.wessol.app.features.presistant.models.company;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class updateCompanyDto {
    String oldName;
    String newName;
}

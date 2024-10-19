package com.wessol.app.features.presistant.models.rep;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepresentativeDto {
    String id;
    String name;
    List<String> companyName;
    LocalDateTime date;
}

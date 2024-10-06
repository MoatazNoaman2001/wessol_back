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
public class AdminRep {

    private String id;
    private String name;
    private List<String> companies;
    private LocalDateTime date;
}

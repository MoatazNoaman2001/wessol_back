package com.wessol.app.features.presistant.models.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequest {

    @NotBlank(message = "Must add Title")
    private String title;
    @NotBlank(message = "Must add price")
    private Double price;
    @NotBlank(message = "Must add some benefits")
    private List<String> prons;
}

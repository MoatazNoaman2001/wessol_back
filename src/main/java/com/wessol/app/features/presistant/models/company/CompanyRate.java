package com.wessol.app.features.presistant.models.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRate {
    private String companyName;
    private Long productReceived;
    private Long productWait;
    private Long productPending;
    private String date;
}

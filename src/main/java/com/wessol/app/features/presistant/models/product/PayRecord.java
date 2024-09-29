package com.wessol.app.features.presistant.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class PayRecord {
    private String rec_name;
    private Double cost;
    private String payMethod;
    private LocalDateTime date;
}

package com.wessol.app.features.presistant.models.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductReceivedAndRefusedCount {
    private long accepted;
    private long refused;
}

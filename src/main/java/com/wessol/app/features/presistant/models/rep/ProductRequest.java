package com.wessol.app.features.presistant.models.rep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String
            company,
            rec_name,
            rec_phone,
            sen_type,
            sen_name,
            sen_phone,
            pay_type,
            price,
            div_type,
            ship_place;
}

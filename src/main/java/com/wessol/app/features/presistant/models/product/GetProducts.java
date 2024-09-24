package com.wessol.app.features.presistant.models.product;

import com.wessol.app.features.presistant.entities.products.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProducts {
    private List<Product> products;
}

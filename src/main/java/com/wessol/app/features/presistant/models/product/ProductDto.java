package com.wessol.app.features.presistant.models.product;


import com.wessol.app.features.presistant.entities.products.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String companyName;
    private String repName;
    private String recName;
    private String planName;
    private String recPhone;
    private String repPhone;
    private String state;
    private String payLocation;
    private String driveType;
    private Double totalCost;
    private Double companyCost;
    private Double repCost;
    private Double repWallet;
    private Double companyWallet;
    private LocalDateTime chargeDate;
    private String payMethod;

    public static ProductDto fromProduct(Product product){
        return  ProductDto.builder()
                .id(product.getId())
                .companyName(product.getCompany().getName())
                .repName(product.getRepresentative().getName())
                .recName(product.getReceiverName())
                .recPhone(product.getReceiverPhoneNumber())
                .repPhone(product.getRepresentative().getPhoneNumber())
                .planName(product.getRepresentative().getMonthAttendancePay().getTitle())
                .totalCost(product.getCost())
                .state(product.getProductState().name())
                .payLocation(product.getShippingPlace().getPlace())
                .driveType(product.getDriveType().name())
                .companyCost(product.getShippingCost())
                .repCost(product.getCost() * product.getRepresentativeEarnings())
                .repWallet(product.getRepresentative().getWallet().getSaving())
                .companyWallet(0.0)
                .chargeDate(product.getReceivedDate())
                .payMethod(product.getPayType().getMethod())
                .build();
    }
}

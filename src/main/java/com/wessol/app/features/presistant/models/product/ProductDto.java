package com.wessol.app.features.presistant.models.product;


import com.wessol.app.features.presistant.entities.clients.Submission;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.DriveType;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String companyName;
    private String repName;
    private String repPhone;
    private String senName;
    private String senPhone;
    private String recName;
    private String recPhone;
    private String planName;
    private String state;
    private String payLocation;
    private String driveType;
    private Double totalCost;
    private Double companyCost;
    private Double repCost;
    private Double repWallet;
    private Double companyWallet;
    private LocalDateTime chargeDate;
    private LocalDateTime returnDate;
    private LocalDateTime cancelDate;
    private String payMethod;
    private Boolean isPaid;
    private Submission sub;

    public static ProductDto fromProduct(Product product){
        return  ProductDto.builder()
                .id(product.getId())
                .companyName(product.getCompany().getName())
                .repName(product.getRepresentative().getName())
                .recName(product.getReceiverName())
                .recPhone(product.getReceiverPhoneNumber())
                .senName(product.getSenderName())
                .senPhone(product.getSenderPhoneNumber())
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
                .returnDate(product.getReturnDate())
                .cancelDate(product.getCancelDate())
                .payMethod(product.getPayType().getMethod())
                .isPaid(product.getIsPaid())
                .sub(product.getSub())
                .build();
    }
}

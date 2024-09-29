package com.wessol.app.features.domain.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.product.PayRecord;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import com.wessol.app.features.presistant.models.rep.WhatsappMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RepresentativeService {
    ResponseEntity<SuccessResponse> updateMyPlan(String planName, String phoneNumber);
    ResponseEntity<Map> getMyPlan(String phoneNumber);

    ResponseEntity<List<PayRecord>> getMyWallet(Representative representative);
    ResponseEntity<SuccessResponse> updateMyImg(MultipartFile file, String phoneNumber);

    ResponseEntity<GetProducts> getUserProducts(Representative rep);

    ResponseEntity<SuccessResponse> sendWhatsappMessage(Representative representative, WhatsappMsg msg) throws JsonProcessingException;

    ResponseEntity<SuccessResponse> addProduct(ProductRequest request);

    ResponseEntity<Representative> getProfile(String phoneNumber);

    ResponseEntity<List<Method>> getMethods(String role);

    ResponseEntity<List<ShippingPlaceE>> getShippingPlaces(String role);

}

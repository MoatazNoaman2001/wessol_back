package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.entities.wallet.BankWallet;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.product.PayRecord;
import com.wessol.app.features.presistant.models.product.ProductDto;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import com.wessol.app.features.presistant.models.rep.WhatsappMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RepresentativeService {
    ResponseEntity<SuccessResponse> updateMyPlan(String planName, String phoneNumber);
    ResponseEntity<Map> getMyPlan(String phoneNumber);

    ResponseEntity<List<PayRecord>> getMyWallet(Representative representative);

    ResponseEntity<Representative> editPhoneNumber(Representative representative, String phoneNumber);
    ResponseEntity<SuccessResponse> updateMyImg(MultipartFile file, String phoneNumber);
    ResponseEntity<SuccessResponse> updateMyWallet(BankWallet wallet, String phoneNumber);
    ResponseEntity<SuccessResponse> getMyImg(String phoneNumber) throws FileNotFoundException;

    ResponseEntity<GetProducts> getUserProducts(Representative rep);

    ResponseEntity<Map<ProductState, Integer>> getBoardState(Representative rep , LocalDateTime start, LocalDateTime end);
    ResponseEntity<List<ProductDto>> getUserProductsCurrent(Representative rep, String start, String end);
    ResponseEntity<List<ProductDto>> getUserProductsPrevious(Representative rep, String start, String end);

    ResponseEntity<SuccessResponse> confirmReceive(Representative rep, String id);

    ResponseEntity<SuccessResponse> sendWhatsappMessage(Representative representative, WhatsappMsg msg) throws IOException, InterruptedException;

    ResponseEntity<SuccessResponse> addProduct(ProductRequest request, Representative rep);

    ResponseEntity<Representative> getProfile(String phoneNumber);

    ResponseEntity<List<Method>> getMethods(String role);

    ResponseEntity<List<ShippingPlaceE>> getShippingPlaces(String role);

}

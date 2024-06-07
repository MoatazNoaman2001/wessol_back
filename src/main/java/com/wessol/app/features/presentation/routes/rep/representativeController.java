package com.wessol.app.features.presentation.routes.rep;

import com.wessol.app.features.domain.services.RepresentativeService;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rep")
@RequiredArgsConstructor
public class representativeController {
    private final RepresentativeService representativeService;

    @PostMapping("/updateMyPlan")
    public ResponseEntity<SuccessResponse> updateMyPlan(@RequestBody PlanRequest planRequest,@RequestPart("phone") String phoneNumber){
        return representativeService.updateMyPlan(planRequest, phoneNumber);
    }

    @GetMapping("/getMyProducts")
    public ResponseEntity<List<Product>> getMyProducts(@RequestPart("phone") String phoneNumber){
        return representativeService.getUserProducts(phoneNumber);
    }

    @GetMapping("/addProduct")
    private ResponseEntity<SuccessResponse> addNewProduct(@RequestBody ProductRequest request, @RequestPart("phone") String phoneNumber){
        return  representativeService.addProduct(request, phoneNumber);
    }

    @GetMapping("/profile")
    private ResponseEntity<Representative> getMyProfile(@RequestPart("phone") String phoneNumber){
        return representativeService.getProfile(phoneNumber);
    }

    @GetMapping("/payments")
    private ResponseEntity<List<Method>> getMethods(){
        return representativeService.getMethods();
    }
    @GetMapping("/shipping-places")
    private ResponseEntity<List<ShippingPlaceE>> getShippingMethods(){
        return representativeService.getShippingPlaces();
    }

}

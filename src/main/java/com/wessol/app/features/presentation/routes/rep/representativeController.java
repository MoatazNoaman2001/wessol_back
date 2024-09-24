package com.wessol.app.features.presentation.routes.rep;

import com.wessol.app.features.domain.services.RepresentativeService;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rep")
@RequiredArgsConstructor
public class representativeController {
    private final RepresentativeService representativeService;

    @PostMapping("/updateMyPlan")
    public ResponseEntity<SuccessResponse> updateMyPlan(@RequestPart("plan_title") String title,@RequestPart("phone") String phoneNumber){
        return representativeService.updateMyPlan(title, phoneNumber);
    }


    @GetMapping("/getMyPlan")
    public ResponseEntity<Map> getMyPlan(@RequestPart("phone") String phoneNumber){
        return representativeService.getMyPlan(phoneNumber);
    }

    @GetMapping("/getMyProducts")
    public ResponseEntity<GetProducts> getMyProducts(Authentication authentication){
        var rep = (Representative) authentication.getPrincipal();
        return representativeService.getUserProducts(rep);
    }

    @PostMapping("/addProduct")
    private ResponseEntity<SuccessResponse> addNewProduct(@RequestBody ProductRequest request, Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        var rep = (Representative) authentication.getPrincipal();
        request.setSen_phone(rep.getPhoneNumber());
        request.setSen_name(rep.getName());
        return  representativeService.addProduct(request);
    }

    @GetMapping("/profile")
    private ResponseEntity<Representative> getMyProfile(@RequestPart("phone") String phoneNumber){
        return representativeService.getProfile(phoneNumber);
    }

    @GetMapping("/payments")
    private ResponseEntity<List<Method>> getMethods(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals(Role.Admin.name()));
        return representativeService.getMethods(isAdmin? Role.Admin.name() :  Role.User.name());
    }
    @GetMapping("/shipping-places")
    private ResponseEntity<List<ShippingPlaceE>> getShippingMethods(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals(Role.Admin.name()));
        return representativeService.getShippingPlaces(isAdmin ? Role.Admin.name() : Role.User.name());
    }

}

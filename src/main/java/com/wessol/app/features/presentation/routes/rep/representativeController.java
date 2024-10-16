package com.wessol.app.features.presentation.routes.rep;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wessol.app.features.domain.services.RepresentativeService;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.entities.wallet.BankWallet;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.product.PayRecord;
import com.wessol.app.features.presistant.models.product.ProductDto;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import com.wessol.app.features.presistant.models.rep.WhatsappMsg;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.QueryParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
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

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProductsByState(@RequestParam("state") String state , Authentication authentication){
        var rep = (Representative) authentication.getPrincipal();
        if (state.equals("current"))
            return representativeService.getUserProductsCurrent(rep);
        else if (state.equals("previous"))
            return representativeService.getUserProductsPrevious(rep);
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/board-state")
    public ResponseEntity<Map<ProductState, Integer>> getBoardState(Authentication authentication, @RequestPart String start, @RequestPart String end){
        var rep = (Representative) authentication.getPrincipal();
        return representativeService.getBoardState(rep, LocalDateTime.parse(start), LocalDateTime.parse(end));
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

    @PostMapping("/whatsapp-msg")
    private ResponseEntity<SuccessResponse> sendWhatsAppMessage(Authentication authentication, @RequestBody WhatsappMsg msg) throws IOException, InterruptedException {
        var rep = ((Representative) authentication.getPrincipal());
        return representativeService.sendWhatsappMessage(rep , msg);
    }

    @GetMapping("/wallet")
    private ResponseEntity<List<PayRecord>> getMyWallet(Authentication authentication){
        var rep = ((Representative) authentication.getPrincipal());
        return representativeService.getMyWallet(rep);
    }

    @PutMapping("/set-image")
    private ResponseEntity<SuccessResponse> setProfileImage(@RequestPart MultipartFile image, Authentication authentication){
        var rep = ((Representative) authentication.getPrincipal());
        return representativeService.updateMyImg(image, rep.getPhoneNumber());
    }

    @PutMapping("/set-wallet")
    private ResponseEntity<SuccessResponse> setWallet(@RequestBody BankWallet wallet, Authentication authentication){
        var rep = ((Representative) authentication.getPrincipal());
        return representativeService.updateMyWallet(wallet, rep.getPhoneNumber());
    }


    @PutMapping("/get-image")
    private ResponseEntity<SuccessResponse> getProfileImage(Authentication authentication) throws FileNotFoundException {
        var rep = ((Representative) authentication.getPrincipal());
        return representativeService.getMyImg(rep.getPhoneNumber());
    }

}

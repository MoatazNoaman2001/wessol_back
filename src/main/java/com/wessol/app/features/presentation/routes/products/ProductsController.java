package com.wessol.app.features.presentation.routes.products;


import com.wessol.app.features.domain.services.ClientService;
import com.wessol.app.features.domain.services.RepresentativeService;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.client.ClientProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prods")
public class ProductsController {

    private final ClientService clientService;
    private final RepresentativeService rs;

    @PostMapping("/confirm/{pid}")
    ResponseEntity<SuccessResponse> verifyUserProduct(@PathVariable String pid, @RequestPart MultipartFile file , @RequestPart ClientProductResponse response){
        try {
            return clientService.sendVerifyProductReceive(file, response, pid);
        } catch (IOException e) {
            throw new RuntimeException("error saving file: " + e.getMessage());
        }
    }

    @PutMapping("/confirm-receive/{id}")
    private ResponseEntity<SuccessResponse> confirmReceive(@PathVariable("id") String id, Authentication authentication){
        var rep = ((Representative) authentication.getPrincipal());
        return rs.confirmReceive(rep, id);
    }
}

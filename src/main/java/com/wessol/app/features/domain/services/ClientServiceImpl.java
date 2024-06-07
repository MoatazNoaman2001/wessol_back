package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.clients.Client;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.client.ClientProductResponse;
import com.wessol.app.features.presistant.repo.ClientRepository;
import com.wessol.app.features.presistant.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Configuration
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ProductRepository pr;
    private final ClientRepository cr;
    private final FileServices fileServices;

    @Value("${project.location}")
    String path;

    @Value("${base.url}")
    String url;

    @Override
    public ResponseEntity<SuccessResponse> sendVerifyProductReceive(MultipartFile file, ClientProductResponse response, String id) throws IOException {
        Product prd =pr.findById(id).orElseThrow(() -> new RuntimeException("cant find product"));
        if (response.getIsCanceled()){
            prd.setProductState(ProductState.Canceled);
            pr.save(prd);
            return ResponseEntity.ok(SuccessResponse.builder().msg("canceled safely").build());
        }else if (response.getPostponed()){
            prd.setProductState(ProductState.Returned);
            prd.setReceivedDate(prd.getReceivedDate().plusDays(1));
            pr.save(prd);
            return ResponseEntity.ok(SuccessResponse.builder().msg("postponed safely").build());
        }
        Client client = Client.builder()
                .firstName(response.getF_name()).lastName(response.getL_name())
                .phoneNumber(response.getPhone()).Message(response.getMessage())
                .location(response.getLoc()).build();
        String name = fileServices.uploadFile(path, file);
        client.setImgName(name);
        cr.save(client);

        prd.setVerifiedDate(LocalDateTime.now());
        pr.save(prd);
        return ResponseEntity.ok(SuccessResponse.builder().msg("confirmed safely").build());
    }
}

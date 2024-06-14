package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.client.ClientProductResponse;
import com.wessol.app.features.presistant.models.contact.ContactUsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClientService {
    ResponseEntity<SuccessResponse> sendVerifyProductReceive(
            MultipartFile file,
            ClientProductResponse response,
            String id
    ) throws IOException;

    ResponseEntity<SuccessResponse> contactUS(ContactUsModel model);
}

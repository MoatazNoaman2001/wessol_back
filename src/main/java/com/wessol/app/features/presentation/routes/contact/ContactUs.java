package com.wessol.app.features.presentation.routes.contact;

import com.wessol.app.features.domain.services.ClientService;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.contact.ContactUsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/p")
@RequiredArgsConstructor
public class ContactUs {
    private final ClientService cs;

    @CrossOrigin
    @PostMapping("/contact-us")
    ResponseEntity<SuccessResponse> contactus(@RequestBody ContactUsModel model){
        return cs.contactUS(model);
    }
}

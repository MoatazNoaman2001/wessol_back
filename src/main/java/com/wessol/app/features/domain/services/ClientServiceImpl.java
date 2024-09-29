package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.clients.Feedback;
import com.wessol.app.features.presistant.entities.clients.Submission;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.models.Pair;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.client.ClientProductResponse;
import com.wessol.app.features.presistant.models.contact.ContactUsModel;
import com.wessol.app.features.presistant.repo.FeedBackRepository;
import com.wessol.app.features.presistant.repo.SubmissionRepository;
import com.wessol.app.features.presistant.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

@Service
@Configuration
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ProductRepository pr;
    private final SubmissionRepository sr;
    private final FileServices fileServices;
    private final FeedBackRepository fr;

    @Value("${project.location}")
    String path;

    @Value("${base.url}")
    String url;

    @Override
    public ResponseEntity<SuccessResponse> sendVerifyProductReceive(MultipartFile file, ClientProductResponse response, String id) throws IOException {
        Product prd =pr.findById(id).orElseThrow(() -> new RuntimeException("cant find product"));
        if (response.getIsCanceled()){
            prd.setProductState(ProductState.Canceled);
            pr.deleteById(prd.getId());
            pr.save(prd);
            return ResponseEntity.ok(SuccessResponse.builder().msg("canceled safely").build());
        }else if (response.getPostponed()){
            prd.setProductState(ProductState.Returned);
            prd.setReceivedDate(prd.getReceivedDate().plusDays(1));
            pr.deleteById(prd.getId());
            pr.save(prd);
            return ResponseEntity.ok(SuccessResponse.builder().msg("postponed safely").build());
        }
        Submission sub = Submission.builder()
                .location(new Pair<Double, Double>(response.getLon(), response.getLit()))
                .isPostponed(response.getPostponed())
                .cause(response.getCause())
                .build();
        String name = fileServices.uploadFile(path, file);
        sub.setImgName(name);
        sub.setProduct(prd);

        sr.save(sub);

        prd.setVerifiedDate(
                LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), TimeZone.getDefault().toZoneId())
        );
        prd.setProductState(ProductState.WAIT);
        pr.deleteById(prd.getId());
        pr.save(prd);
        return ResponseEntity.ok(SuccessResponse.builder().msg("confirmed safely").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> contactUS(ContactUsModel model) {
        Feedback feed = Feedback.builder().l_name(model.getL_name())
                .f_name(model.getF_name())
                .phone(model.getPhone())
                .msg(model.getMsg())
                .build();

        fr.save(feed);
        return ResponseEntity.ok(SuccessResponse.builder().msg("Feed Saved").build());
    }
}

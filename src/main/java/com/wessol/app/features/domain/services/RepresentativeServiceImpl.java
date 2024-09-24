package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.products.*;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import com.wessol.app.features.presistant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RepresentativeServiceImpl implements RepresentativeService {
    private final RepresentativeRepository repRepo;
    private final PlanRepository planRepository;
    private final FileServices fileServices;
    private final ProductRepository pr;
    private final CompanyRepository cr;
    private final MethodRepository mr;
    private final ShippingPlaceRepository sr;

    @Value("${project.location}")
    String path;

    @Value("${base.url}")
    String url;

    @Override
    public ResponseEntity<SuccessResponse> updateMyPlan(String planName, String phoneNumber) {
        var planOp = planRepository.findByTitle(planName);
        if (planOp.isPresent()) {
            var repOp = repRepo.findByPhoneNumber(phoneNumber);
            if (repOp.isPresent()) {
                var rep = repOp.get();
                rep.setMonthAttendancePay(planOp.get());
                rep.setMothAttendancePayStartDate(
                        LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), TimeZone.getDefault().toZoneId())
                );

                repRepo.save(rep);
                return ResponseEntity.ok(SuccessResponse.builder().msg("plan updated").build());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Map> getMyPlan(String phoneNumber) {
        var rep = repRepo.findByPhoneNumber(phoneNumber);
        Plan plan = null;
        if (rep.isPresent()) {
            plan = rep.get().getMonthAttendancePay();
            if (plan != null) {
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("getMonthAttendancePay", rep.get().getMonthAttendancePay(), "getMothAttendancePayStartDat", rep.get().getMothAttendancePayStartDate()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rep.isEmpty() ? "cant find representative" : "dont have any plan"));

    }

    @Override
    public ResponseEntity<SuccessResponse> updateMyImg(MultipartFile file, String phoneNumber) {
        boolean isUserExist = repRepo.findByPhoneNumber(phoneNumber).isPresent();
        if (isUserExist) {
            Representative rep = repRepo.findByPhoneNumber(phoneNumber).get();
            if (rep.getMonthAttendancePay() != null) {
                planRepository.delete(rep.getMonthAttendancePay());
            }
            String fileName = "";
            try {
                fileServices.uploadFile(path, file);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            rep.setImageName(fileName);
            repRepo.save(rep);
            return ResponseEntity.ok(SuccessResponse.builder().msg("image updated").build());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<GetProducts> getUserProducts(Representative rep) {
        var products = pr.findByRepresentative(rep);
        return products.map(productList -> ResponseEntity.ok(GetProducts.builder().products(
                productList.stream().map(product -> {
                    var payType = product.getPayType();
                    var company = product.getCompany();
                    var place = product.getShippingPlace();
                    var rep_Edited= product.getRepresentative();
                    payType.setProducts(new ArrayList<>());
                    company.setProducts(new ArrayList<>());
                    place.setProducts(new ArrayList<>());
                    rep_Edited.setProducts(new ArrayList<>());
                    return Product.builder().id(product.getId())
                            .Cost(product.getCost())
                            .representative(rep_Edited)
                            .sub(product.getSub())
                            .productState(product.getProductState())
                            .driveType(product.getDriveType())
                            .representativeEarnings(product.getRepresentativeEarnings())
                            .company(company)
                            .payType(payType)
                            .shippingPlace(place)
                            .dateCreated(product.getDateCreated())
                            .verifiedDate(product.getVerifiedDate())
                            .receiverName(product.getReceiverName())
                            .receiverPhoneNumber(product.getReceiverPhoneNumber())
                            .ReceivedDate(product.getReceivedDate())
                            .build();
                }).toList()
        ).build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<SuccessResponse> addProduct(ProductRequest request) {
        Representative rep = repRepo.findByPhoneNumber(request.getSen_phone()).orElseThrow(() -> new RuntimeException("cant find representative"));
        var company = cr.findByName(request.getCompany());
        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var method = mr.findByMethod(request.getPay_type());
        var place = sr.findByPlace(request.getShip_place());
        if (rep.getMonthAttendancePay() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder().msg("rep has no plan").build());

        if (method.isPresent() && place.isPresent()) {
            Product prd = Product.builder()
                    .receiverName(request.getRec_name())
                    .receiverPhoneNumber(request.getRec_phone())
                    .representative(rep)
                    .company(company.get())
                    .payType(method.get())
                    .Cost(Long.parseLong(request.getPrice()))
                    .driveType(request.getDiv_type().equals(DriveType.CatchProduct.name()) ? DriveType.CatchProduct :
                            DriveType.InHand)
                    .shippingPlace(place.get())
                    .dateCreated(LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), TimeZone.getDefault().toZoneId()))
                    .productState(ProductState.Pending)
                    .build();

            pr.save(prd);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SuccessResponse.builder().msg("method or place not found").build());
        }
        return ResponseEntity.ok(SuccessResponse.builder()
                .msg("Product added Successfully").build());
    }

    @Override
    public ResponseEntity<Representative> getProfile(String phoneNumber) {
        var rp = repRepo.findByPhoneNumber(phoneNumber);
        return rp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Representative()));
    }

    @Override
    public ResponseEntity<List<Method>> getMethods(String role) {
        var methods = role.equals(Role.Admin.name()) ? mr.findAll() : mr.findAll()
                .stream().peek(method -> method.setProducts(new ArrayList<>())).toList();
        return ResponseEntity.ok(methods);
    }

    @Override
    public ResponseEntity<List<ShippingPlaceE>> getShippingPlaces(String role) {
        var places = role.equals(Role.Admin.name()) ? sr.findAll() :
                sr.findAll().stream().peek(shippingPlaceE -> shippingPlaceE.setProducts(new ArrayList<>())).toList();
        return ResponseEntity.ok(places);
    }
}
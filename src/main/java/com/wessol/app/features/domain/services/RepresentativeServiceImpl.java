package com.wessol.app.features.domain.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wessol.app.core.Config.Constants;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.products.*;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.entities.wallet.BankWallet;
import com.wessol.app.features.presistant.models.auth.LetsBotModel;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.product.GetProducts;
import com.wessol.app.features.presistant.models.product.PayRecord;
import com.wessol.app.features.presistant.models.product.ProductDto;
import com.wessol.app.features.presistant.models.rep.ProductRequest;
import com.wessol.app.features.presistant.models.rep.WhatsappMsg;
import com.wessol.app.features.presistant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<PayRecord>> getMyWallet(Representative representative) {
        List<Product> wallet = new ArrayList<>();
        pr.findByRepresentative(representative).ifPresent(products -> {
            for (Product product : products) {
                if (product.getIsPaid()){
                    wallet.add(product);
                }
            }
        });
        return ResponseEntity.ok(wallet.stream().map(product -> PayRecord.builder()
                .rec_name(product.getReceiverName())
                .cost(product.getCost())
                .date(product.getReceivedDate())
                .payMethod(product.getPayType().getMethod())
                .build()).toList());
    }

    @Override
    public ResponseEntity<SuccessResponse> updateMyImg(MultipartFile file, String phoneNumber) {
        boolean isUserExist = repRepo.findByPhoneNumber(phoneNumber).isPresent();
        if (isUserExist) {
            Representative rep = repRepo.findByPhoneNumber(phoneNumber).get();
            String fileName = "";
            try {
                fileName = fileServices.uploadFile(path, file);
                rep.setImageName(fileName);
                repRepo.saveAndFlush(rep);
                return ResponseEntity.ok(SuccessResponse.builder().msg("image updated").build());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<SuccessResponse> updateMyWallet(BankWallet wallet, String phoneNumber) {
        var rep = repRepo.findByPhoneNumber(phoneNumber);
        if (rep.isPresent()){
            var representative = rep.get();
            representative.setWallet(wallet);
            repRepo.saveAndFlush(representative);
            return  ResponseEntity.ok(SuccessResponse.builder().msg("wallet updated").build());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<SuccessResponse> getMyImg(String phoneNumber) throws FileNotFoundException {
        var image = fileServices.getResourceFile(path , repRepo.findByPhoneNumber(phoneNumber).get().getImageName());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).build();
    }

    @Override
    public ResponseEntity<GetProducts> getUserProducts(Representative rep) {
        var products = pr.findByRepresentative(rep);
        return products.map(productList -> ResponseEntity.ok(GetProducts.builder().products(
                productList.stream().map(product -> {
                    var payType = product.getPayType();
                    var company = product.getCompany();
                    var place = product.getShippingPlace();
                    var rep_Edited = product.getRepresentative();
                    payType.setProducts(new ArrayList<>());
                    company.setProducts(new ArrayList<>());
                    place.setProducts(new ArrayList<>());
                    rep_Edited.setProducts(new ArrayList<>());
                    var plan = rep_Edited.getMonthAttendancePay();
                    if (plan != null)
                        plan.setRepresentatives(new ArrayList<>());
                    rep_Edited.setMonthAttendancePay(plan);

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
    public ResponseEntity<Map<ProductState , Integer>> getBoardState(Representative rep, LocalDateTime start, LocalDateTime end) {
        Map<ProductState , Integer> counts = new HashMap<>();

        for (ProductState state : ProductState.values()) {
            counts.put(state ,pr.findByProductState(state)
                    .stream().filter(product -> product.getDateCreated().isBefore(end) && product.getDateCreated().isAfter(start))
                    .toList().size());
        }

        return ResponseEntity.ok(counts);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getUserProductsCurrent(Representative rep, String start, String end) {
        List<ProductDto>productDtos = new ArrayList<ProductDto>(){{
            addAll(pr.findByProductState(ProductState.WAIT).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
            addAll(pr.findByProductState(ProductState.Pending).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
            addAll(pr.findByProductState(ProductState.Accepted).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
        }}.stream().filter(productDto -> productDto.getRepPhone().equals(rep.getPhoneNumber())).collect(Collectors.toCollection(ArrayList::new));
        return ResponseEntity.ok(productDtos);
    }


    @Override
    public ResponseEntity<List<ProductDto>> getUserProductsPrevious(Representative rep, String start, String end) {
        List<ProductDto>productDtos = new ArrayList<>(new ArrayList<ProductDto>() {{
            addAll(pr.findByProductState(ProductState.DELIVERED).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
            addAll(pr.findByProductState(ProductState.Canceled).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
            addAll(pr.findByProductState(ProductState.Returned).stream().filter(product ->
                            product.getDateCreated().isAfter(LocalDateTime.parse(start))
                                    && product.getDateCreated().isBefore(LocalDateTime.parse(end)))
                    .map(ProductDto::fromProduct).toList());
        }}).stream().filter(productDto -> productDto.getRepPhone().equals(rep.getPhoneNumber())).collect(Collectors.toCollection(ArrayList::new));
        return ResponseEntity.ok(productDtos);
    }


    @Override
    public ResponseEntity<SuccessResponse> confirmReceive(Representative rep, String id) {
        var prod = pr.findById(id);
        if (prod.isPresent()){
            Product product = prod.get();
            product.confirmReceive();
            pr.saveAndFlush(product);
            return  ResponseEntity.ok(SuccessResponse.builder().msg("product receive confirmed").build());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<SuccessResponse> sendWhatsappMessage(Representative representative, WhatsappMsg msg) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
        LetsBotModel letsBotModel = generateGetLocationMessage(representative, msg);
        String requestBody = new ObjectMapper().writeValueAsString(letsBotModel);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://letsbot.net/api/v1/message/send"))
                .timeout(Duration.of(30, ChronoUnit.SECONDS))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Constants.LESTBOT_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            if (msg.getId() == 3) {
                Product product = pr.findById(msg.getProductId()).get();
                if (product.getProductState() !=ProductState.WAIT){
                    return ResponseEntity.ok(SuccessResponse.builder().msg("يجب تاكيد من قبل العميل").build());
                }
                product.confirmReceive();
                pr.saveAndFlush(product);
                return ResponseEntity.ok(SuccessResponse.builder().msg("product receive confirmed").build());
            }else if (msg.getId() == 2){
                Product product = pr.findById(msg.getProductId()).get();
                product.returnProduct();
                pr.saveAndFlush(product);
                return ResponseEntity.ok(SuccessResponse.builder().msg("product return successfully").build());
            }else if (msg.getId() == 4){
                Product product = pr.findById(msg.getProductId()).get();
                product.cancelProduct();
                pr.saveAndFlush(product);
                return ResponseEntity.ok(SuccessResponse.builder().msg("product canceled successfully").build());
            }
            return ResponseEntity.ok().body(SuccessResponse.builder().msg("message sent successfully").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                .msg("still in dev mode").build());
    }

    private LetsBotModel generateGetLocationMessage(Representative representative, WhatsappMsg msg) {
//        var product = pr.findById(msg.getProductId());
        return LetsBotModel.builder().phone(msg.getPhone()).body(msg.getMsg()
                + (msg.getId() ==0 ? "http://209.250.233.30:8080/location/" + msg.getProductId(): "")).build();
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
            return ResponseEntity.notFound().build();

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
        if (rp.isPresent()){
            Representative rep = rp.get();
            Plan current = rep.getMonthAttendancePay();
            if (current != null)
                current.setRepresentatives(new ArrayList<>());
            rep.setMonthAttendancePay(current);
            rep.setProducts(rep.getProducts().stream().peek(product -> product.setRepresentative(null)).toList());
            return  ResponseEntity.ok(rep);
        }
        return rp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Representative()));
    }

    @Override
    public ResponseEntity<List<Method>> getMethods(String role) {
        var methods = role.equals(Role.Admin.name()) ? mr.findAll() : mr.findAll()
                .stream().peek(method -> {
                    method.setProducts(new ArrayList<>());
                    method.setImageName(path + File.separator + method.getImageName());
                }).toList();
        return ResponseEntity.ok(methods);
    }

    @Override
    public ResponseEntity<List<ShippingPlaceE>> getShippingPlaces(String role) {
        var places = role.equals(Role.Admin.name()) ? sr.findAll() :
                sr.findAll().stream().peek(shippingPlaceE -> shippingPlaceE.setProducts(new ArrayList<>())).toList();
        return ResponseEntity.ok(places);
    }
}
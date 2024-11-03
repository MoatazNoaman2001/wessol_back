package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.admin.AddMethod;
import com.wessol.app.features.presistant.models.company.CompanyRate;
import com.wessol.app.features.presistant.models.company.addCompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.admin.ServicesState;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.company.updateCompanyDto;
import com.wessol.app.features.presistant.models.product.AdminProductReceivedAndRefusedCount;
import com.wessol.app.features.presistant.models.product.ProductDto;
import com.wessol.app.features.presistant.models.rep.AdminRep;
import com.wessol.app.features.presistant.models.rep.RepresentativeDto;
import com.wessol.app.features.presistant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final PlanRepository repository;
    private final CompanyRepository cr;
    private final SubmissionRepository clr;
    private final RepresentativeRepository rp;
    private final ShippingPlaceRepository sr;
    private final ProductRepository pr;
    private final MethodRepository mr;
    private final FileServices fileServices;


    @Value("${project.location}")
    String path;

    @Value("${base.url}")
    String url;

    @Override
    public ResponseEntity<List<CompanyRate>> getMonthsMove() {
        List<Company> companies = cr.findAll();
        var res = companies.stream().map(company -> CompanyRate.builder().companyName(company.getName())
                .productReceived(company.getProducts().stream().filter(product -> product.getProductState() == ProductState.DELIVERED).count())
                .productWait(company.getProducts().stream().filter(product ->
                                product.getProductState() == ProductState.Pending || product.getProductState() == ProductState.WAIT
                        ).count())
                .productPending(company.getProducts().stream().filter(product ->
                                product.getProductState() != ProductState.DELIVERED &&
                                product.getProductState() != ProductState.Pending &&
                                product.getProductState() != ProductState.WAIT
                        ).count())
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM")))

                .build());
        return  ResponseEntity.ok(res.toList());

    }

    @Override
    public ResponseEntity<AdminProductReceivedAndRefusedCount> getRecAndRefCount() {
        var canceled = pr.findByProductState(ProductState.Canceled);
        var delivered = pr.findByProductState(ProductState.DELIVERED);

        return ResponseEntity.ok(AdminProductReceivedAndRefusedCount.builder().refused(canceled.size())
                .accepted(delivered.size()).build());
    }

    @Override
    public ResponseEntity<Long> getProductsCount() {

        return ResponseEntity.ok(pr.count());
    }

    @Override
    public ResponseEntity<List<Representative>> getAllReps() {
        var reps = rp.findAll();
        return ResponseEntity.ok(reps);
    }

    @Override
    public ResponseEntity<SuccessResponse> addPlan(PlanRequest addPlanRequest) {
        System.out.println(addPlanRequest);
        Plan plan = Plan.builder()
                .title(addPlanRequest.getTitle())
                .AttendancePay(addPlanRequest.getPrice())
                .prons(addPlanRequest.getPros())
                .build();
        boolean isExist  = repository.findByTitle(plan.getTitle()).isPresent();
        if (isExist){
            return ResponseEntity.badRequest()
                    .body(SuccessResponse.builder()
                    .msg("Plan Already Exist").build());
        }
        repository.save(plan);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder()
                .msg("Created").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> UpdatePlan(PlanRequest addPlanRequest) {
        Plan plan = Plan.builder()
                .title(addPlanRequest.getTitle())
                .AttendancePay(addPlanRequest.getPrice())
                .prons(addPlanRequest.getPros())
                .build();
        boolean isExist  = repository.findByTitle(plan.getTitle()).isPresent();
        if (isExist){
            repository.delete(repository.findByTitle(plan.getTitle()).get());
        }else{
            return ResponseEntity.badRequest()
                    .body(SuccessResponse.builder()
                            .msg("Plan not Exist").build());
        }
        repository.save(plan);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                .msg("Updated").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> DeletePlan(String title) {
        boolean isExist  = repository.findByTitle(title).isPresent();
        if (isExist){
            repository.delete(repository.findByTitle(title).get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(SuccessResponse.builder()
                            .msg("Plan not Found").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                .msg("Deleted").build());
    }

    @Override
    public ResponseEntity<List<Plan>> getAllAvailablePlans() {
        var plans = repository.findAll();
        return ResponseEntity.ok(plans);
    }

    @Override
    public ResponseEntity<List<Company>> getAllCompanies(String role, String id) {
        return ResponseEntity.ok(Objects.equals(role, Role.Admin.name()) ? cr.findAll() :
                cr.findAll().stream().peek(company -> company.setProducts(new ArrayList<>())).toList());
    }

    @Override
    public ResponseEntity<List<Method>> getAllMethods(String role , String id) {
        var methods = Objects.equals(role, Role.Admin.name()) ? mr.findAll() :
                mr.findAll().stream().peek(method -> method.setProducts(new ArrayList<>())).toList();
        return ResponseEntity.ok(
                methods.stream().peek(method ->
                        method.setImageName(!method.getImageName().isEmpty()?url + "/" + method.getImageName(): "")
                ).toList()
        );
    }

    @Override
    public ResponseEntity<SuccessResponse> addNewMethod(AddMethod addMethod, MultipartFile file) throws IOException {
        if (mr.findByMethod(addMethod.getMethodName()).isEmpty()) {
            var res = fileServices.uploadFile(path, file);
            mr.save(Method.builder().method(addMethod.getMethodName()).imageName(res).build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder()
                .msg("Method Created Successfully").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> updateMethod(String oldName, String newName, MultipartFile file) throws IOException {
        if (mr.findByMethod(oldName).isPresent()) {
            var method = mr.findByMethod(oldName).get();
            String filename  = "";
            if (file != null)
                filename = fileServices.uploadFile(path, file);
            mr.saveAndFlush(Method.builder().method(newName != null? newName : method.getMethod())
                    .id(method.getId()).imageName(filename).build());
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                    .msg("Method updated Successfully").build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder()
                .msg("Method Cant be found").build());
    }
    @Override
    public ResponseEntity<SuccessResponse> deleteMethod(Long id) {
        if (mr.findById(id).isPresent()) {
            mr.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                    .msg("Method Deleted Successfully").build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder()
                .msg("Method Cant be found").build());
    }

    @Override
    public ResponseEntity<String> getLoginLogs(Representative representative) {
        try {
            FileInputStream inputStream = new FileInputStream("logs/login_history.png");
            String logs = new String(inputStream.readAllBytes());
            return ResponseEntity.ok(logs);
        } catch (IOException ignored) {}
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<List<ShippingPlaceE>> getAllShippingPlaces() {
        return ResponseEntity.ok(sr.findAll());
    }

    @Override
    public ResponseEntity<SuccessResponse> addNewShippingPlace(String name) {
        if (sr.findByPlace(name).isEmpty()){
            sr.save(ShippingPlaceE.builder().place(name).build());
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                    .msg("new place added Successfully").build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder()
                .msg("place is already exist").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteShippingPlace(Long id) {
        if (sr.findById(id).isPresent()) {
            sr.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                    .msg("place Deleted Successfully").build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder()
                .msg("place Cant be found").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> updateShippingPlace(String oldName, String newName) throws IOException {
        if (sr.findByPlace(oldName).isPresent()) {
            var placeE = sr.findByPlace(oldName).get();
            sr.saveAndFlush(ShippingPlaceE.builder()
                            .place(newName)
                            .id(placeE.getId())
                    .build());
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                    .msg("place updated Successfully").build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SuccessResponse.builder()
                .msg("place Cant be found").build());
    }


    @Override
    public ResponseEntity<SuccessResponse> addNewCompany(addCompanyDto companyDto) {
        cr.save(Company.builder().name(companyDto.getName()).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().msg("created").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteCompany(String name) {
        cr.findByName(name).ifPresent(cr::delete);
        return ResponseEntity.ok(SuccessResponse.builder().msg("deleted Successfully").build());
    }

    @Override
    public ResponseEntity<SuccessResponse> updateCompany(updateCompanyDto update) {
        var companySearch = cr.findByName(update.getOldName());
        if(companySearch.isPresent()){
            var company = companySearch.get();
            company.setName(update.getNewName());
            cr.save(company);
            return ResponseEntity.ok(SuccessResponse.builder().msg("company updated").build());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<ServicesState> getServiceState() {
        var companies = cr.findAll();
        var clients = clr.findAll();

        return ResponseEntity.ok(ServicesState.builder().clients(clients).companies(companies).build());
    }

    @Override
    public ResponseEntity<Page<ProductDto>> getAllProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> promotions = pr.findAll(pageRequest);
        Page<ProductDto> promotionDtos = promotions.map(ProductDto::fromProduct);
        return ResponseEntity.ok(promotionDtos);
    }

    @Override
    public ResponseEntity<List<AdminRep>> getAllAdminReps() {
        var Reps = rp.findAll().stream().map(rep -> AdminRep.builder()
                .id(rep.getNationalId())
                .date(rep.getCreateDate())
                .name(rep.getName())
                .companies(rep.getProducts().stream().map(product -> product.getCompany().getName()).toList())
                .build()).toList();
        return ResponseEntity.ok(Reps);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllInvoice(String start, String end, Integer id) {
        List<ProductDto> products = pr.findByProductState(ProductState.DELIVERED).stream()
                .filter(product -> product.getVerifiedDate().isAfter(LocalDateTime.parse(start)) &&
                                product.getVerifiedDate().isBefore(LocalDateTime.parse(end)) &&
                        (id == null || Objects.equals(product.getRepresentative().getMonthAttendancePay().getId(), id))
                )
                .map(ProductDto::fromProduct).toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<List<RepresentativeDto>> getAllReps(String start, String end, Integer id) {
        List<RepresentativeDto> reps = rp.findAll()
                .stream()

                .filter(
                        representative ->
                                representative.getMonthAttendancePay() != null &&
                                representative.getMothAttendancePayStartDate().isAfter(LocalDateTime.parse(start))&&
                                representative.getMothAttendancePayStartDate().isBefore(LocalDateTime.parse(end))
                ).map(representative -> RepresentativeDto.builder()
                        .companyName(representative.getProducts().stream().map(product -> product.getCompany().getName()).toList())
                        .id(representative.getNationalId())
                        .name(representative.getName())
                        .date(representative.getMothAttendancePayStartDate())
                        .build()).toList();
        return ResponseEntity.ok(reps);
    }

}

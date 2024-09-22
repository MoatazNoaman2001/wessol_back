package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.admin.AddMethod;
import com.wessol.app.features.presistant.models.company.CompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.admin.ServicesState;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            mr.save(
                    Method.builder().method(addMethod.getMethodName()).imageName(res).build()
            );
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
            mr.saveAndFlush(Method.builder().method(newName)
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
    public ResponseEntity<SuccessResponse> addNewCompany(CompanyDto companyDto) {
        cr.save(Company.builder().name(companyDto.getName()).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().msg("created").build());
    }

    @Override
    public ResponseEntity<ServicesState> getServiceState() {
        var companies = cr.findAll();
        var clients = clr.findAll();

        return ResponseEntity.ok(ServicesState.builder().clients(clients).companies(companies).build());
    }

    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(pr.findAll());
    }

}

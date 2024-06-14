package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.models.company.CompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.admin.ServicesState;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.repo.SubmissionRepository;
import com.wessol.app.features.presistant.repo.CompanyRepository;
import com.wessol.app.features.presistant.repo.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final PlanRepository repository;
    private final CompanyRepository cr;
    private final SubmissionRepository clr;

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
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(cr.findAll());
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

}

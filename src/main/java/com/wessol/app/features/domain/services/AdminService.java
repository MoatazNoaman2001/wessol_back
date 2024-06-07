package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.models.company.CompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.admin.ServicesState;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<SuccessResponse> addPlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> UpdatePlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> DeletePlan(String title);

    ResponseEntity<List<Plan>> getAllAvailablePlans();

    ResponseEntity<List<Company>> getAllCompanies();
    ResponseEntity<SuccessResponse> addNewCompany(CompanyDto companyDto);

    ResponseEntity<ServicesState> getServiceState();
}

package com.wessol.app.features.presentation.routes.admin;

import com.wessol.app.features.domain.services.AdminService;
import com.wessol.app.features.domain.services.AuthService;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.SendOTPModel;
import com.wessol.app.features.presistant.models.company.CompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @GetMapping("/reps")
    public  ResponseEntity<List<Representative>> getAllReps(){
        return  adminService.getAllReps();
    }

    @PostMapping("/add-plan")
    public ResponseEntity<SuccessResponse> addPlan(@RequestBody PlanRequest planRequest){
        return adminService.addPlan(planRequest);
    }

    @PostMapping("/update-plan")
    public ResponseEntity<SuccessResponse> updatePlan(@RequestBody PlanRequest planRequest){
        return adminService.UpdatePlan(planRequest);
    }

    @PostMapping("/delete-plan")
    public ResponseEntity<SuccessResponse> deletePlan(@RequestPart String title){
        return adminService.DeletePlan(title);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        return adminService.getAllCompanies();
    }

    @PostMapping("/add-company")
    public ResponseEntity<SuccessResponse> addCompany(@RequestBody CompanyDto companyDto){
        return adminService.addNewCompany(companyDto);
    }

    @GetMapping("/prods")
    public ResponseEntity<List<Product>> getAllProducts(){
        return adminService.getAllProducts();
    }

}

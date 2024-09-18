package com.wessol.app.features.domain.services;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    ResponseEntity<List<Representative>> getAllReps();
    ResponseEntity<SuccessResponse> addPlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> UpdatePlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> DeletePlan(String title);
    ResponseEntity<List<Plan>> getAllAvailablePlans();
    ResponseEntity<List<Company>> getAllCompanies();

    ResponseEntity<List<Method>> getAllMethods();
    ResponseEntity<SuccessResponse> addNewMethod(AddMethod addMethod, MultipartFile file) throws IOException;
    ResponseEntity<SuccessResponse> deleteMethod(Long id);
    ResponseEntity<SuccessResponse> updateMethod(String oldName, String newName, MultipartFile file) throws IOException;

    ResponseEntity<List<ShippingPlaceE>> getAllShippingPlaces();
    ResponseEntity<SuccessResponse> addNewShippingPlace(String name) throws IOException;
    ResponseEntity<SuccessResponse> deleteShippingPlace(Long id);
    ResponseEntity<SuccessResponse> updateShippingPlace(String oldName, String newName) throws IOException;
    ResponseEntity<SuccessResponse> addNewCompany(CompanyDto companyDto);
    ResponseEntity<ServicesState> getServiceState();
    ResponseEntity<List<Product>> getAllProducts();
}

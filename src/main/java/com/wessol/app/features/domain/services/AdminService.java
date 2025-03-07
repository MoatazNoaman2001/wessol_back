package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    ResponseEntity<List<CompanyRate>> getMonthsMove();
    ResponseEntity<AdminProductReceivedAndRefusedCount> getRecAndRefCount();
    ResponseEntity<Long> getProductsCount();
    ResponseEntity<Page<ProductDto>> getAllProducts(int page, int size);
    ResponseEntity<List<Representative>> getAllReps();
    ResponseEntity<SuccessResponse> addPlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> UpdatePlan(PlanRequest addPlanRequest);
    ResponseEntity<SuccessResponse> DeletePlan(String title);
    ResponseEntity<List<Plan>> getAllAvailablePlans();
    ResponseEntity<SuccessResponse> addNewCompany(addCompanyDto companyDto);
    ResponseEntity<SuccessResponse> deleteCompany(String name);
    ResponseEntity<SuccessResponse> updateCompany(updateCompanyDto update);
    ResponseEntity<List<Company>> getAllCompanies(String role, String id);

    ResponseEntity<List<Method>> getAllMethods(String role, String id);
    ResponseEntity<SuccessResponse> addNewMethod(AddMethod addMethod, MultipartFile file) throws IOException;
    ResponseEntity<SuccessResponse> deleteMethod(Long id);
    ResponseEntity<String> getLoginLogs(Representative representative);
    ResponseEntity<SuccessResponse> updateMethod(String oldName, String newName, MultipartFile file) throws IOException;

    ResponseEntity<List<ShippingPlaceE>> getAllShippingPlaces();
    ResponseEntity<SuccessResponse> addNewShippingPlace(String name) throws IOException;
    ResponseEntity<SuccessResponse> deleteShippingPlace(Long id);
    ResponseEntity<SuccessResponse> updateShippingPlace(String oldName, String newName) throws IOException;
    ResponseEntity<ServicesState> getServiceState();

    ResponseEntity<List<AdminRep>> getAllAdminReps();

    ResponseEntity<List<ProductDto>> getAllInvoice(String start, String end, Integer id);
    ResponseEntity<List<RepresentativeDto>> getAllReps(String start, String end, Integer id);

}

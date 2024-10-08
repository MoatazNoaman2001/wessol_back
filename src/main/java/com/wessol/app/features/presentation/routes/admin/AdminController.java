package com.wessol.app.features.presentation.routes.admin;

import com.wessol.app.features.domain.services.AdminService;
import com.wessol.app.features.domain.services.AuthService;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.admin.AddMethod;
import com.wessol.app.features.presistant.models.company.CompanyRate;
import com.wessol.app.features.presistant.models.company.addCompanyDto;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.models.admin.PlanRequest;
import com.wessol.app.features.presistant.models.auth.SuccessResponse;
import com.wessol.app.features.presistant.models.company.updateCompanyDto;
import com.wessol.app.features.presistant.models.product.AdminProductReceivedAndRefusedCount;
import com.wessol.app.features.presistant.models.product.ProductDto;
import com.wessol.app.features.presistant.models.rep.AdminRep;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;


    @GetMapping("/companies/rate")
    public ResponseEntity<List<CompanyRate>> getCompaniesRate(){
        return adminService.getMonthsMove();
    }

    @GetMapping("/receiver_and_canceled")
    public ResponseEntity<AdminProductReceivedAndRefusedCount> getRecAndRefCount(){
        return adminService.getRecAndRefCount();
    }

    @GetMapping("/pr/count")
    public ResponseEntity<Long> getPrCount(){
        return adminService.getProductsCount();
    }

    @GetMapping("/reps")
    public  ResponseEntity<List<Representative>> getAllReps(){
        return  adminService.getAllReps();
    }

    @GetMapping("/admin_rep")
    public  ResponseEntity<List<AdminRep>> getAllRepresentatives(){
        return adminService.getAllAdminReps();
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
    public ResponseEntity<List<Company>> getAllCompanies(Authentication authentication){
        var rep = (Representative ) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals(Role.Admin.name()));
        return adminService.getAllCompanies(isAdmin? Role.Admin.name() : Role.User.name(), rep.getNationalId());
    }

    @PostMapping("/add-company")
    public ResponseEntity<SuccessResponse> addCompany(@RequestBody addCompanyDto companyDto, Authentication auth){
        Representative rep = (Representative) auth.getPrincipal();
        System.out.println(rep.getRole());
        return adminService.addNewCompany(companyDto);
    }
    @PostMapping("/update-company")
    public ResponseEntity<SuccessResponse> updateCompany(@RequestBody updateCompanyDto update){
        return adminService.updateCompany(update);
    }

    @PostMapping("/delete-company")
    public ResponseEntity<SuccessResponse> deleteCompany(@RequestPart String name){
        return adminService.deleteCompany(name);
    }

    @GetMapping("/prods")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam int page , @RequestParam int size){
        return adminService.getAllProducts(page , size);
    }

    @PostMapping("/add-method")
    public ResponseEntity<SuccessResponse> addMethod(@RequestPart MultipartFile image,@RequestPart AddMethod addMethod) throws IOException {
        return adminService.addNewMethod(addMethod, image);
    }

    @PutMapping("/update-method")
    public ResponseEntity<SuccessResponse> updateMethod(@RequestPart String oldName, @RequestPart String newName, @RequestPart MultipartFile image) throws IOException {
        return adminService.updateMethod(oldName, newName, image);
    }
    @DeleteMapping("/delete-method")
    public ResponseEntity<SuccessResponse> deleteMethod(@RequestPart Long id){
        return adminService.deleteMethod(id);
    }

    @GetMapping("/get-all-methods")
    public ResponseEntity<List<Method>> getAllMethods(Authentication authentication){
        var rep = (Representative ) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals(Role.Admin.name()));
        return adminService.getAllMethods(isAdmin? Role.Admin.name() : Role.User.name(), rep.getNationalId());
    }

    @GetMapping("/logs")
    public  ResponseEntity<String> getLoginLogs(Authentication authentication){
        var rep = (Representative ) authentication.getPrincipal();
        return adminService.getLoginLogs(rep);
    }
    @PostMapping("/add-place")
    public ResponseEntity<SuccessResponse> addPlace(@RequestPart String name) throws IOException {
        return adminService.addNewShippingPlace(name);
    }

    @PutMapping("/update-place")
    public ResponseEntity<SuccessResponse> updatePlace(@RequestPart String oldName, @RequestPart String newName) throws IOException {
        return adminService.updateShippingPlace(oldName, newName);
    }
    @DeleteMapping("/delete-place")
    public ResponseEntity<SuccessResponse> deletePlace(@RequestPart Long id){
        return adminService.deleteShippingPlace(id);
    }

    @GetMapping("/get-all-places")
    public ResponseEntity<List<ShippingPlaceE>> getAllPlaces(){
        return adminService.getAllShippingPlaces();
    }


}

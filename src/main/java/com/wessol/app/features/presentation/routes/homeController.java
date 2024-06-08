package com.wessol.app.features.presentation.routes;

import com.wessol.app.features.domain.services.AdminService;
import com.wessol.app.features.presistant.entities.plan.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class homeController {

    private final AdminService adminService;

    @GetMapping("/")
    String RootPage(){
        return  "<h1>Hello to Wessol Page<h1>";
    }


    @GetMapping("/pricing/plans")
    ResponseEntity<List<Plan>> getAllExistPlans(){
        return adminService.getAllAvailablePlans();
    }
}


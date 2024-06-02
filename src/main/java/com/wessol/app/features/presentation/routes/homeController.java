package com.wessol.app.features.presentation.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class homeController {

    @GetMapping("/")
    String RootPage(){
        return  "<h1>Hello to Wessol Page<h1>";
    }
}

package com.wessol.app.features.presistant.models.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class    LetsBotModel {
    private String phone;
    private String body;
}

package com.wessol.app.features.presistant.models.rep;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappMsg {
    @Valid
    @Size(min = 8)
    private String phone;
    @NotNull
    private String msg;
    private int id;
    @NotNull
    private String productId;
}

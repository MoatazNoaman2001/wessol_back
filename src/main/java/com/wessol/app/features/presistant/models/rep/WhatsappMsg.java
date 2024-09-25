package com.wessol.app.features.presistant.models.rep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappMsg {
    private String phone;
    private String msg;
    private int id;
}

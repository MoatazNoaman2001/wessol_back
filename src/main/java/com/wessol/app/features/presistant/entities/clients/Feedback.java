package com.wessol.app.features.presistant.entities.clients;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Entity
@Table(name = "_feeds")
public class Feedback {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank(message = "Should enter first name")
    private String f_name;
    @NotNull
    @NotBlank(message = "Should enter last name")
    private String l_name;
    @NotNull
    @NotBlank(message = "Should enter phoneNumber")
    @Size(min = 8)
    private String phone;
    @NotNull
    @NotBlank(message = "Should enter Msg")
    @Size(max = 1024)
    private String msg;
}

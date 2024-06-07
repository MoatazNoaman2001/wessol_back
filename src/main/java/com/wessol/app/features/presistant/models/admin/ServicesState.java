package com.wessol.app.features.presistant.models.admin;

import com.wessol.app.features.presistant.entities.clients.Client;
import com.wessol.app.features.presistant.entities.company.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicesState {

    List<Company> companies;
    List<Client> clients;

}

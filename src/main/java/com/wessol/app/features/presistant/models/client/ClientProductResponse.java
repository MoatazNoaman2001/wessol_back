package com.wessol.app.features.presistant.models.client;


import com.wessol.app.features.presistant.models.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductResponse {

    private Double lon;
    private Double lit;
    private Boolean isCanceled;
    private Boolean postponed;
    private String cause;
}
/*{loc: {78562 , 54789}, isCanceled: false, postponed: false, cause: null}*/

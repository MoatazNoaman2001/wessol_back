package com.wessol.app;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.repo.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
@ComponentScan("com.wessol.app")
@EnableJpaAuditing
//@OpenAPIDefinition
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }


    @Bean
    public CommandLineRunner runner(CompanyRepository cr,
                                    RepresentativeRepository rr,
                                    PlanRepository pr,
                                    MethodRepository mr ,
                                    PasswordEncoder pe,
                                    OtpRepo or,
                                    ShippingPlaceRepository sr) {
        return args -> {
            if (cr.findAll().isEmpty()) {
                cr.saveAll(
                        List.of(
                                Company.builder().name("raya").build(),
                                Company.builder().name("toshiba").build(),
                                Company.builder().name("sum-sung").build(),
                                Company.builder().name("apple").build(),
                                Company.builder().name("ejada").build()
                        )
                );
            }

            if (pr.findAll().isEmpty()) {
                pr.saveAll(List.of(
                        Plan.builder().title("الخطة الشهرية").AttendancePay(15.0)
                                .prons(List.of("توصيل الشحنات بنسبة 100٪ في الوقت المحدد.",
                                        "تحديد مواعيد التسليم المرنة حسب احتياجاتك.",
                                        "تتبع الشحنات بالوقت الحقيقي.",
                                        " دعم العملاء على مدار الساعة.")
                                ).build(),
                        Plan.builder().title("الخطة النصف سنوية").AttendancePay(35.0).prons(
                                List.of("جميع ميزات الخطة الشهرية",
                                        "خصم يصل إلى 10٪ عند الدفع مقدماً لنصف السنة",
                                        "توفير المزيد من الاستقرار والثبات في الخدمة")
                        ).build(),
                        Plan.builder().title("الخطة السنوية").AttendancePay(60.0).prons(
                                List.of("جميع ميزات الخطط الشهرية والنصف سنوية",
                                        "خصم يصل إلى 20٪ عند الدفع مقدماً للسنة كاملة",
                                        "ضمان استمرارية الخدمة على مدار السنة دون انقطاع")
                        ).build()
                ));
            }

            if (mr.findAll().isEmpty()) {
                mr.save(Method.builder().method("Visa").build());
                mr.save(Method.builder().method("MasterCard").build());
                mr.save(Method.builder().method("AmericanExpress").build());
                mr.save(Method.builder().method("Paypal").build());
                mr.save(Method.builder().method("Diners").build());
            }

            if(sr.findAll().isEmpty()){
                sr.saveAll(List.of(
                        ShippingPlaceE.builder().place("Back").build(),
                        ShippingPlaceE.builder().place("Front").build()
//                        ShippingPlaceE.builder().place("Front").build()
                ));
            }

            if (rr.findAll().isEmpty()) {
                var rep = Representative.builder()
                        .role(Role.Admin)
                        .phoneNumber("201098518194")
                        .NationalId("30107222502032")
                        .name("Eng.Moataz").build();
                rr.save(rep);
                or.save(
                        OTP.builder()
                                .OTP(pe.encode("889745"))
                                .expiresAt(LocalDateTime.now().plusDays(30))
                                .representative(rep)
                        .build()
                );
            }
        };
    }

}

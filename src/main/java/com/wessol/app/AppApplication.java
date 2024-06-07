package com.wessol.app;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.repo.CompanyRepository;
import com.wessol.app.features.presistant.repo.MethodRepository;
import com.wessol.app.features.presistant.repo.PlanRepository;
import com.wessol.app.features.presistant.repo.RepresentativeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@ComponentScan("com.wessol.app")
@EnableJpaAuditing
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}


	@Bean
	public CommandLineRunner runner(CompanyRepository cr, RepresentativeRepository rr, PlanRepository pr, MethodRepository mr){
		return args -> {
			if (cr.findAll().isEmpty()){
				cr.save(Company.builder().name("raya").build());
				cr.save(Company.builder().name("toshiba").build());
				cr.save(Company.builder().name("sum-sung").build());
				cr.save(Company.builder().name("apple").build());
				cr.save(Company.builder().name("ejada").build());
			}

			if(pr.findAll().isEmpty()){
				pr.save(Plan.builder().title("الخطة الشهرية").AttendancePay(15.0).prons(
						List.of("توصيل الشحنات بنسبة 100٪ في الوقت المحدد.",
								"تحديد مواعيد التسليم المرنة حسب احتياجاتك.",
								"تتبع الشحنات بالوقت الحقيقي.",
								" دعم العملاء على مدار الساعة.")
				).build());
				pr.save(Plan.builder().title("الخطة النصف سنوية").AttendancePay(35.0).prons(
						List.of("جميع ميزات الخطة الشهرية" ,
								"خصم يصل إلى 10٪ عند الدفع مقدماً لنصف السنة",
								"توفير المزيد من الاستقرار والثبات في الخدمة")
				).build());
				pr.save(Plan.builder().title("الخطة السنوية").AttendancePay(60.0).prons(
						List.of("جميع ميزات الخطط الشهرية والنصف سنوية" ,
								"خصم يصل إلى 20٪ عند الدفع مقدماً للسنة كاملة" ,
								"ضمان استمرارية الخدمة على مدار السنة دون انقطاع")
				).build());
			}

			if (mr.findAll().isEmpty()){
				mr.save(Method.builder().method("Visa").build());
				mr.save(Method.builder().method("MasterCard").build());
				mr.save(Method.builder().method("AmericanExpress").build());
				mr.save(Method.builder().method("Paypal").build());
				mr.save(Method.builder().method("Diners").build());
			}

			if (rr.findAll().isEmpty()){
				rr.save(Representative.builder()
						.role(Role.Admin)
						.phoneNumber("8897456321")
						.NationalId("7789656265655741")
						.name("Eng.Moataz").build());
			}
		};
	}

}

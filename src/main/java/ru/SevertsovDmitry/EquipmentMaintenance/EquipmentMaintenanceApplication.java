package ru.SevertsovDmitry.EquipmentMaintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EquipmentMaintenanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquipmentMaintenanceApplication.class, args);
	}

}

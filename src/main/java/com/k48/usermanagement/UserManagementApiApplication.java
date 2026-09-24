package com.k48.usermanagement;

import com.k48.usermanagement.entity.Role;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserManagementApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserManagementApiApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {

			if (userRepository.findByEmail("admin@example.com").isEmpty()) {

				User admin = new User();

				admin.setName("Administrateur");
				admin.setEmail("admin@example.com");
				admin.setPassword(
						passwordEncoder.encode("Admin123!")
				);
				admin.setRole(Role.ADMIN);
				admin.setEmailVerified(true);

				userRepository.save(admin);

				System.out.println(
						"========================================"
				);
				System.out.println(
						"ADMIN créé : admin@example.com"
				);
				System.out.println(
						"========================================"
				);
			}
		};
	}

}

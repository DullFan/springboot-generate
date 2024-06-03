package com.dullfan.generate;

import com.dullfan.generate.build.BuildFile;
import com.dullfan.generate.entity.TableInfo;
import com.dullfan.generate.service.DatabaseManagementService;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringBootGenerateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootGenerateApplication.class, args);
	}
}

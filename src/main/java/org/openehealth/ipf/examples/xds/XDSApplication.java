package org.openehealth.ipf.examples.xds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"org.openehealth.ipf.examples.xds"})
public class XDSApplication implements CommandLineRunner {
   
	public static void main(String[] args) {
		SpringApplication.run(XDSApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("================ Preparing, initialization etc ==============================");
	}
}

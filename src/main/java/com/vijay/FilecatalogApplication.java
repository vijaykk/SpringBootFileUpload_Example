package com.vijay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author Vijay Koppineedi
 *
 */
@SpringBootApplication
@EnableScheduling
public class FilecatalogApplication {

    /***
     *   	
     * NOTE: NO UI/HTML interface is in place for this application.
     *       1. Can upload files to application using any HTTP REST client (Chrome or Firefox)
     *       2. JUnit in this application uses JAVA Rest client class FileCatalogServiceClient
     *       3. Please follow 'readme.txt' to test and proceed further.
     * 
     */
	public static void main(String[] args) {
		SpringApplication.run(FilecatalogApplication.class, args);
		
	}
}

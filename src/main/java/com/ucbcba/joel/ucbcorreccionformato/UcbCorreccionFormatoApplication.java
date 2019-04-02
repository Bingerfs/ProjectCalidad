package com.ucbcba.joel.ucbcorreccionformato;

import com.ucbcba.joel.ucbcorreccionformato.uploaddownloadfile.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class UcbCorreccionFormatoApplication {

	public static void main(String[] args) {
		SpringApplication.run(UcbCorreccionFormatoApplication.class, args);
	}

}

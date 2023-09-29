package com.alejo.photodownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableRetry
public class PhotoDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoDownloaderApplication.class, args);
	}

}

package com.bhailaverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bhailaverse.service.DarkSkyWeatherService;
import com.bhailaverse.service.NewsService;
import com.bhailaverse.service.NyTimesNewsService;
import com.bhailaverse.service.WeatherService;

@SpringBootApplication
public class AainaApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public WeatherService weatherService() {
		return new DarkSkyWeatherService();
	}
	
	@Bean
	public NewsService newsService() {
		return new NyTimesNewsService();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AainaApplication.class, args);
	}
}

package com.bhailaverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhailaverse.model.WeatherData;
import com.bhailaverse.service.WeatherService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AainaRestController {
	
	private static final String WEATHER_URL = "/weather/{latLng}";
	
	@Autowired
	WeatherService weatherService;
	
	@RequestMapping(WEATHER_URL)
	public WeatherData getWeather(@PathVariable("latLng") String latLng) throws Exception {
		log.debug("Accessing " + WEATHER_URL + " with " + latLng);
		return weatherService.getWeather(latLng);
	}
}

package com.bhailaverse.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bhailaverse.exception.AainaException;
import com.bhailaverse.model.SkinnyWeatherData;
import com.bhailaverse.model.WeatherData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DarkSkyWeatherService implements WeatherService {

	@Value("${aaina.darksky.api.key}")
	String apiKey;

	@Value("${aaina.darksky.url}")
	String serviceUrl;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public WeatherData getWeather(String latLng) throws AainaException {
		ResponseEntity<String> response = restTemplate.getForEntity(serviceUrl, String.class, apiKey, latLng);
		if(!response.getStatusCode().equals(HttpStatus.OK)) {
			log.error("DarkSky weather API error :: Status Code " + response.getStatusCode() + " Status Message " + response.getStatusCode().getReasonPhrase());
			throw new AainaException("DarkSky weather service threw an exception");
		}
		// process json string response
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
		List<Map<String,Object>> forecastData = JsonPath.read(document, "$.daily.data");
		System.out.println(response.getBody());
		WeatherData weatherData = WeatherData.builder()
		.currentTemp(JsonPath.read(document, "$.currently.temperature"))
		.currentTempDesc(JsonPath.read(document, "$.currently.summary"))
		//.alert(JsonPath.read(document, "$.alerts.0.title"))
		.dailySummary(JsonPath.read(document, "$.daily.summary"))
		.hourlySummmary(JsonPath.read(document, "$.hourly.summary"))
		.highTemp(JsonPath.read(document, "$.daily.data[0].temperatureMax"))
		.lowTemp(JsonPath.read(document, "$.daily.data[0]temperatureMin"))
		.sunriseTime((Integer)JsonPath.read(document, "$.daily.data[0].sunriseTime"))
		.sunsetTime((Integer)JsonPath.read(document, "$.daily.data.[0].sunsetTime"))
		.forecast(forecastData.stream().map(
				item -> 
				SkinnyWeatherData.builder()
					.highTemp((Double)item.get("temperatureMax"))
					.lowTemp((Double)item.get("temperatureMin"))
					.build())
				.collect(Collectors.toList())
		)
		.build();
		
		return weatherData;
			
	}

}

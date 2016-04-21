package com.bhailaverse.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;


@Builder
@Data
@JsonDeserialize(builder = WeatherData.WeatherDataBuilder.class)
public class WeatherData {
	
	private Double currentTemp;
	private String currentTempDesc;
	private String alert;
	private String hourlySummary;
	private String dailySummary;
	
	private Double highTemp;
	private Double lowTemp;
	
	private long sunriseTime;
	private long sunsetTime;
	
	@Singular("forecast")
	private List<SkinnyWeatherData> forecast;
	
}

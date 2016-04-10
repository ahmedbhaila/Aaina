package com.bhailaverse.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Data;
import rx.Observable;

@Builder
@Data
@JsonDeserialize(builder = WeatherData.WeatherDataBuilder.class)
public class WeatherData {
	private Double currentTemp;
	private String currentTempDesc;
	private String alert;
	private String hourlySummmary;
	private String dailySummary;
	
	private Double highTemp;
	private Double lowTemp;
	
	private long sunriseTime;
	private long sunsetTime;
	
	//@Singular("forecast")
	private Observable<List<SkinnyWeatherData>> forecast;
	
	public List<SkinnyWeatherData> getForecast() {
		return forecast.toBlocking().single();
	}
	
}

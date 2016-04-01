package com.bhailaverse.service;

import com.bhailaverse.exception.AainaException;
import com.bhailaverse.model.WeatherData;

public interface WeatherService {
	public WeatherData getWeather(String latLng) throws AainaException;
}

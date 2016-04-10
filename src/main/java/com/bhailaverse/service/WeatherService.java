package com.bhailaverse.service;

import com.bhailaverse.exception.AainaException;
import com.bhailaverse.model.WeatherData;

import rx.Observable;

public interface WeatherService {
	public Observable<WeatherData> getWeather(String latLng) throws AainaException;
}

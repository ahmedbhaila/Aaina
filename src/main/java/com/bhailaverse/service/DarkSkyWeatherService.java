package com.bhailaverse.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bhailaverse.exception.AainaException;
import com.bhailaverse.model.SkinnyWeatherData;
import com.bhailaverse.model.WeatherData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import rx.Observable;
import rx.schedulers.Schedulers;

@Slf4j
public class DarkSkyWeatherService implements WeatherService {

	@Value("${aaina.darksky.api.key}")
	String apiKey;

	@Value("${aaina.darksky.url}")
	String serviceUrl;

	@Autowired
	RestTemplate restTemplate;

	private Observable<ResponseEntity<String>> makeHttpCall(String latLng) {
		return Observable.create(sub -> {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(serviceUrl, String.class, apiKey, latLng);
				//System.out.println(response.getBody().toString());
				sub.onNext(response);
				sub.onCompleted();
			}
			catch(RestClientException e) {
				log.error("Error is " + e.getMessage());
				e.printStackTrace();
				sub.onError(e);
			}
			catch(Exception e) {
				sub.onError(e);
			}
		});
	}
	
	
	private Observable<WeatherData> createWeatherData(Object document) {
		WeatherData data = WeatherData
				.builder()
				.currentTemp(JsonPath.read(document, "$.currently.temperature"))
				.currentTempDesc(JsonPath.read(document, "$.currently.summary"))
				.dailySummary(JsonPath.read(document, "$.daily.summary"))
				.hourlySummary(JsonPath.read(document, "$.hourly.summary"))
				.highTemp(JsonPath.read(document, "$.daily.data[0].temperatureMax"))
				.lowTemp(JsonPath.read(document, "$.daily.data[0]temperatureMin"))
				.sunriseTime((Integer)JsonPath.read(document, "$.daily.data[0].sunriseTime"))
				.sunsetTime((Integer)JsonPath.read(document, "$.daily.data.[0].sunsetTime"))
				.build();
		return Observable.just(data);
	}
	public Observable<WeatherData> getWeather(String latLng) throws AainaException {
		return makeHttpCall(latLng)
	    .subscribeOn(Schedulers.io())
		.map( res -> Configuration.defaultConfiguration().jsonProvider().parse(res.getBody()))
		.flatMap(document -> {
			
			
			Observable<WeatherData> wd = createWeatherData(document);
			
			Observable<List<SkinnyWeatherData>> forecast = getFutureCast(document).toList();
			
			return Observable.zip(wd, forecast, (w,f) -> {
				w.setForecast(f);
				return w;
			});
					
		});
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Observable<SkinnyWeatherData> getFutureCast(Object document) {
		return Observable
		.from((JSONArray)JsonPath.read(document, "$.daily.data"))
		.map(item -> {
			LinkedHashMap<String, Object> temp = (LinkedHashMap<String, Object>)item;
			return SkinnyWeatherData.builder()
			.highTemp(temp.get("temperatureMax"))
			.lowTemp(temp.get("temperatureMin"))
			.build();
		});
	}
}

package com.bhailaverse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhailaverse.model.NewsData;
import com.bhailaverse.model.WeatherData;
import com.bhailaverse.service.NewsService;
import com.bhailaverse.service.WeatherService;

import lombok.extern.slf4j.Slf4j;
import rx.schedulers.Schedulers;

@Slf4j
@RestController
public class AainaRestController {
	
	private static final String WEATHER_URL = "/weather/{latLng:.+}";
	private static final String NEWS_URL = "/news";
	
	@Autowired
	WeatherService weatherService;
	
	@Autowired
	NewsService newsService;
	
	@RequestMapping(WEATHER_URL)
	@CrossOrigin(origins = "http://localhost:3002")
	public WeatherData getWeather(@PathVariable("latLng") String latLng) throws Exception {
		log.debug("Accessing " + WEATHER_URL + " with " + latLng);
		return weatherService.getWeather(latLng)
				.subscribeOn(Schedulers.computation())
				.toBlocking().single();
				//.first();
	}
	
	@RequestMapping(NEWS_URL)
	@CrossOrigin(origins = "http://localhost:3002")
	public List<NewsData> getNews() throws Exception {
		log.debug("Accessing " + NEWS_URL);
		return newsService.getNews()
				.subscribeOn(Schedulers.computation())
				.toBlocking().single();
				//.single();
	}
}

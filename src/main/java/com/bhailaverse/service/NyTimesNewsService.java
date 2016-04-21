package com.bhailaverse.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bhailaverse.model.NewsData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import rx.Observable;

public class NyTimesNewsService implements NewsService {
	
	@Value("${aaina.nytimes.topstories.url}")
	String newsUrl;
	
	@Value("${aaina.nytimes.api.key}")
	String apiKey;
	
	@Autowired
	RestTemplate restTemplate;
	
	private Observable<ResponseEntity<String>> makeHttpCall() {
		return Observable.create(sub -> {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(newsUrl, String.class, apiKey);
				System.out.println(response.getBody().toString());
				sub.onNext(response);
				sub.onCompleted();
			}
			catch(RestClientException e) {
				sub.onError(e);
			}
			catch(Exception e) {
				sub.onError(e);
			}
		});
	}
	
	@Override
	public Observable<List<NewsData>> getNews() {
		Configuration conf = Configuration.builder().mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonProvider()).build();
		TypeRef<NewsData[]> type = new TypeRef<NewsData[]>(){};
		
		return makeHttpCall()
			.map( res -> Arrays.asList(JsonPath.using(conf).parse(res.getBody()).read("$.results", type)));
	}
}

package com.bhailaverse.service;

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
	public Observable<NewsData> getNews() {
		return makeHttpCall()
			.map( res -> Configuration.defaultConfiguration().jsonProvider().parse(res.getBody()))
			.map(document -> (List<Map<String,Object>>)JsonPath.read(document, "$.results"))
			.flatMap(data -> Observable.from((List<Map<String,Object>>)data))
			.map(b -> new NewsData((String)b.get("title"), (String)b.get("abstract")));
	}
}

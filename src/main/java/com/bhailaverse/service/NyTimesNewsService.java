package com.bhailaverse.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bhailaverse.model.NewsData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class NyTimesNewsService implements NewsService {
	
	@Value("${aaina.nytimes.topstories.url}")
	String newsUrl;
	
	@Value("${aaina.nytimes.api.key}")
	String apiKey;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<NewsData> getNews() {
		ResponseEntity<String> response = restTemplate.getForEntity(newsUrl, String.class, apiKey);
		
		// process json string response
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
		List<Map<String,Object>> newsData = JsonPath.read(document, "$.results");
		
		return newsData.stream().map(item -> new NewsData((String)item.get("title"), (String)item.get("abstract"))).collect(Collectors.toList());
	}
}

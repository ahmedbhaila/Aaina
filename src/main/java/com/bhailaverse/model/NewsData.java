package com.bhailaverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsData {
	public NewsData() {
		
	}
	private String title;
	
	@JsonProperty("abstract")
	private String shortDesc;
}

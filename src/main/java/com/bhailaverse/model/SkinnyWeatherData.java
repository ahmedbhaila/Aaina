package com.bhailaverse.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SkinnyWeatherData {
	private Object highTemp;
	private Object lowTemp;
}

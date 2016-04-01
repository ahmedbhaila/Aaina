package com.bhailaverse.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SkinnyWeatherData {
	private Double highTemp;
	private Double lowTemp;
}

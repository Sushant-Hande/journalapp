package com.sushant.journalapp.service;

import com.sushant.journalapp.apiresponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;

    public static final String BASE_URL = "http://api.weatherstack.com/current?access_key=API_KEY"  + "&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public  WeatherResponse getWeather(String city) {
       String finalUrl = BASE_URL.replace("CITY", city).replace("API_KEY", API_KEY);
       ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
       WeatherResponse weatherResponse = response.getBody();
       return weatherResponse;
    }
}

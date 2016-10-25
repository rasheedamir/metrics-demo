package com.example;

import com.netflix.spectator.api.DefaultRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MetricsDemoApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(MetricsDemoApplication.class, args);
	}

	@Bean
	public DefaultRegistry getDefaultRegistry()
	{
		return new DefaultRegistry();
	}
}

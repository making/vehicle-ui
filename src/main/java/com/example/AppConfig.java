package com.example;

import am.ik.accesslogger.AccessLogger;
import io.micrometer.core.instrument.config.MeterFilter;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(VehicleProps.class)
public class AppConfig {
	@Bean
	public VehicleClient vehicleClient(WebClient.Builder builder, VehicleProps props) {
		final WebClient webClient = builder
				.baseUrl(props.apiUrl())
				.build();
		final HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builder(WebClientAdapter.forClient(webClient))
				.build();
		return factory.createClient(VehicleClient.class);
	}

	@Bean
	public AccessLogger accessLogger() {
		return new AccessLogger(httpExchange -> {
			final String uri = httpExchange.getRequest().getUri().getPath();
			return uri != null && !(uri.equals("/readyz") || uri.equals("/livez")
									|| uri.startsWith("/actuator") || uri.startsWith("/_static"));
		});
	}

	@Bean
	public MeterRegistryCustomizer<?> meterRegistryCustomizer() {
		return registry -> registry.config() //
				.meterFilter(MeterFilter.deny(id -> {
					final String uri = id.getTag("uri");
					return uri != null && (uri.equals("/readyz") || uri.equals("/livez")
										   || uri.startsWith("/actuator") || uri.startsWith("/_static"));
				}));
	}
}

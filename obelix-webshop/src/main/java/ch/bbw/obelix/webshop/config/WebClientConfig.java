package ch.bbw.obelix.webshop.config;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.quarry.api.QuarryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
@Profile("!test")
public class WebClientConfig {

	@Bean
	public WebClient quarryWebClient(@Value("${obelix.quarry.base-url}") String baseUrl) {
		log.info("configuring quarry webclient with base-url: {}", baseUrl);
		return WebClient.builder()
				.baseUrl(baseUrl)
				.defaultStatusHandler(
						status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> {
							log.error("error calling quarry service: status={}", clientResponse.statusCode());
							return clientResponse.createException()
									.doOnNext(ex -> log.error("quarry service error details", ex));
						}
				)
				.build();
	}

	@Bean
	public QuarryApi quarryWebclient(WebClient quarryWebClient) {
		var adapter = WebClientAdapter.create(quarryWebClient);
		var factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(QuarryApi.class);
	}

	@Bean
	public WebClient basketWebClient(@Value("${obelix.basket.base-url}") String baseUrl) {
		log.info("configuring basket webclient with base-url: {}", baseUrl);
		return WebClient.builder()
				.baseUrl(baseUrl)
				.defaultStatusHandler(
						status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> {
							log.error("error calling basket service: status={}", clientResponse.statusCode());
							return clientResponse.createException()
									.doOnNext(ex -> log.error("basket service error details", ex));
						}
				)
				.build();
	}

	@Bean
	public BasketApi basketWebclient(WebClient basketWebClient) {
		var adapter = WebClientAdapter.create(basketWebClient);
		var factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(BasketApi.class);
	}
}

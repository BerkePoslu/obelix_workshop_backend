package ch.bbw.obelix.basket.config;

import ch.bbw.obelix.quarry.api.QuarryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class BasketWebClientConfig {

	@Bean
	public WebClient quarryWebClient(@Value("${obelix.quarry.base-url}") String baseUrl) {
		log.info("basket: configuring quary webclient with baseurl: {}", baseUrl);
		return WebClient.builder()
				.baseUrl(baseUrl)
				.defaultStatusHandler(
						status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> {
							log.error("basket: error calling quarry service: status={}", clientResponse.statusCode());
							return clientResponse.createException()
									.doOnNext(ex -> log.error("basket: quarry service error details", ex));
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
}

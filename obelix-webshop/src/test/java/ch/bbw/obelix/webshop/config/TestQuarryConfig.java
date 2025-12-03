package ch.bbw.obelix.webshop.config;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@TestConfiguration
public class TestQuarryConfig {

	@Bean
	@Primary
	public QuarryApi quarryWebclient() {
		return new QuarryApi() {
			private final UUID testId = UUID.randomUUID();
			private boolean deleted = false;

			@Override
			public List<MenhirDto> getAllMenhirs() {
				if (deleted) {
					return List.of();
				}
				return List.of(new MenhirDto(testId, 2.5, "Granite Gaulois", DecorativenessDto.DECORATED, "test menhir"));
			}

			@Override
			public MenhirDto getMenhirById(UUID menhirId) {
				if (deleted || !testId.equals(menhirId)) {
					throw new RuntimeException("menhir not found");
				}
				return new MenhirDto(testId, 2.5, "Granite Gaulois", DecorativenessDto.DECORATED, "test menhir");
			}

			@Override
			public void deleteById(UUID menhirId) {
				if (testId.equals(menhirId)) {
					deleted = true;
				}
			}
		};
	}

	@Bean
	@Primary
	public BasketApi basketWebclient() {
		return new BasketApi() {
			private BasketDto basket = BasketDto.empty();

			@Override
			public BasketDto getBasket() {
				return basket;
			}

			@Override
			public BasketDto offer(BasketDto.BasketItem basketItem) {
				var items = new java.util.ArrayList<>(basket.items());
				items.add(basketItem);
				basket = new BasketDto(List.copyOf(items));
				return basket;
			}

			@Override
			public void leave() {
				basket = BasketDto.empty();
			}
		};
	}

	@Bean
	@Primary
	public WebClient basketWebClient() {
		return WebClient.builder().baseUrl("http://localhost:8082").build();
	}
}

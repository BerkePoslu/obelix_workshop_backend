package ch.bbw.obelix.webshop;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.webshop.config.TestQuarryConfig;
import ch.bbw.obelix.webshop.dto.BasketDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestQuarryConfig.class)
class ObelixWebshopApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	@org.junit.jupiter.api.Disabled("requires running quarry service on port 8081")
	void buyMenhir() {
		var anyId = webTestClient.get()
				.uri("/api/menhirs")
				.exchange()
				.expectBodyList(MenhirDto.class)
				.returnResult()
				.getResponseBody()
				.getFirst()
				.id();
		webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isBadRequest();
		webTestClient.put().uri("/api/basket/offer").bodyValue(new BasketDto.BasketItem("boar", 2)).exchange().expectStatus().isOk();
		webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isOk();
		webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isBadRequest();
	}
}

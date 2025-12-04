package ch.bbw.obelix.webshop;

import ch.bbw.obelix.webshop.dto.BasketDto;
import ch.bbw.obelix.quarry.dto.MenhirDto;
import ch.bbw.obelix.quarry.dto.DecorativenessDto;
import ch.bbw.obelix.webshop.service.QuarryWebClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ObelixWebshopApplicationTests {

    @MockBean
    private QuarryWebClientService quarryWebclient;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void buyMenhir() {
        var id = UUID.randomUUID();
        var menhir = new MenhirDto(id, 100.0, "Granite", DecorativenessDto.SIMPLE, "Test Menhir");
        when(quarryWebclient.getAllMenhirs()).thenReturn(List.of(menhir));
        when(quarryWebclient.getMenhirById(any())).thenReturn(menhir);

        var anyId = quarryWebclient.getAllMenhirs().getFirst().id();

        webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isBadRequest();
        webTestClient.put().uri("/api/basket/offer").bodyValue(new BasketDto.BasketItem("boar", 2)).exchange().expectStatus().isOk();
        webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isOk();
        webTestClient.post().uri("/api/basket/buy/{id}", anyId).exchange().expectStatus().isBadRequest();
    }
}

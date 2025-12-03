package ch.bbw.obelix.webshop.service;

import java.util.List;
import java.util.UUID;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ObelixWebshopService {

	private final QuarryApi quarryWebclient;
	private final WebClient basketWebClient;

	public List<MenhirDto> getAllMenhirs() {
		return quarryWebclient.getAllMenhirs();
	}

	public MenhirDto getMenhirById(UUID menhirId) {
		return quarryWebclient.getMenhirById(menhirId);
	}

	public void deleteById(UUID menhirId) {
		quarryWebclient.deleteById(menhirId);
	}

	public void exchange(UUID menhirId) {
		try {
			basketWebClient.post()
					.uri("/api/basket/buy/{menhirId}", menhirId)
					.retrieve()
					.toBodilessEntity()
					.block();
		} catch (Exception e) {
			throw new RuntimeException("exchange failed", e);
		}
	}
}

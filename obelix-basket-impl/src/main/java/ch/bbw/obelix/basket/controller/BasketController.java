package ch.bbw.obelix.basket.controller;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BasketController implements BasketApi {

	private final BasketService basketService;

	@Override
	public BasketDto getBasket() {
		return basketService.getBasket();
	}

	@Override
	public BasketDto offer(BasketDto.BasketItem basketItem) {
		return basketService.offer(basketItem);
	}

	@Override
	public void leave() {
		basketService.leave();
	}

	@PostMapping("/api/basket/buy/{menhirId}")
	public void exchangeFor(@PathVariable UUID menhirId) {
		basketService.exchange(menhirId);
	}

	@ExceptionHandler(BasketService.BadOfferException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handleBadOffer() {
	}
}

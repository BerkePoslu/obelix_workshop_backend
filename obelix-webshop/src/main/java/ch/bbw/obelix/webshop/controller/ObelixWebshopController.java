package ch.bbw.obelix.webshop.controller;

import java.util.List;
import java.util.UUID;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.webshop.service.ObelixWebshopService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ObelixWebshopController {

	private final ObelixWebshopService obelixWebshopService;
	private final BasketApi basketWebclient;

	@GetMapping("/api")
	public String welcome() {
		return "Welcome to Obelix's Menhir Shop! The finest menhirs in all of Gaul! Ces Romains sont fous!";
	}

	@GetMapping("/api/menhirs")
	public List<MenhirDto> getAllMenhirs() {
		return obelixWebshopService.getAllMenhirs();
	}

	@GetMapping("/api/menhirs/{menhirId}")
	public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
		return obelixWebshopService.getMenhirById(menhirId);
	}

	/**
	 * Note that this should only be called by Asterix himself.
	 * Hopefully, no customer will ever find this endpoint...
	 */
	@DeleteMapping("/api/quarry/{menhirId}")
	public void deleteById(@PathVariable UUID menhirId) {
		obelixWebshopService.deleteById(menhirId);
	}

	/**
	 * Customer adds even more shinies in exchange for a beautiful menhir.
	 */
	@PutMapping("/api/basket/offer")
	public BasketDto offer(@RequestBody BasketDto.BasketItem basketItem) {
		return basketWebclient.offer(basketItem);
	}

	@DeleteMapping("/api/basket")
	public void leave() {
		basketWebclient.leave();
	}

	@PostMapping("/api/basket/buy/{menhirId}")
	public void exchangeFor(@PathVariable UUID menhirId) {
		obelixWebshopService.exchange(menhirId);
	}



	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class UnknownMenhirException extends RuntimeException {}
}

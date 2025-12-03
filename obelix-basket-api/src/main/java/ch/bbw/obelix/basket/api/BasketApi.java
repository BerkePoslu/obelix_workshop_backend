package ch.bbw.obelix.basket.api;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BasketApi {
	
	@GetMapping("/api/basket")
	BasketDto getBasket();
	
	@PutMapping("/api/basket/offer")
	BasketDto offer(@RequestBody BasketDto.BasketItem basketItem);
	
	@DeleteMapping("/api/basket")
	void leave();
}

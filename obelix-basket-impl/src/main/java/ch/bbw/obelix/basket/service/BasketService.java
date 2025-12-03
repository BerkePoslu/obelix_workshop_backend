package ch.bbw.obelix.basket.service;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketService {

	private final QuarryApi quarryWebclient;
	private BasketDto basket;

	static <T> List<T> append(List<T> immutableList, T element) {
		var tmpList = new ArrayList<>(immutableList);
		tmpList.add(element);
		return Collections.unmodifiableList(tmpList);
	}

	public BasketDto getBasket() {
		return basket;
	}

	public BasketDto offer(@NonNull BasketDto.BasketItem basketItem) {
		basket = basket.withItems(append(basket.items(), basketItem));
		return basket;
	}

	@PostConstruct
	public void leave() {
		basket = BasketDto.empty();
	}

	public boolean isGoodOffer(DecorativenessDto decorativeness) {
		var stoneWorth = decorativeness.ordinal();
		var basketWorth = basket.items()
				.stream().map(x -> switch (x.name().toLowerCase(Locale.ROOT)) {
					case "boar" -> 5;
					case "honey" -> 2;
					case "magic potion" -> 0;
					default -> 1;
				} * x.count()).reduce(0, Integer::sum);
		log.info("basket worth {} vs menhir worth {} ({})", basketWorth, decorativeness, stoneWorth);
		return basketWorth >= stoneWorth;
	}

	public void exchange(UUID menhirId) {
		var menhir = quarryWebclient.getMenhirById(menhirId);
		var decorativeness = menhir.decorativeness();
		if (!isGoodOffer(decorativeness)) {
			throw new BadOfferException("Bad Offer: That won't even feed Idefix!");
		}
		quarryWebclient.deleteById(menhirId);
		leave();
	}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class BadOfferException extends RuntimeException {}
}

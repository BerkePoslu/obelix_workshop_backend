package ch.bbw.obelix.webshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.webshop.dto.BasketDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Note that Obelix is definitely not multitasking-capable.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ObelixWebshopService {

	private final QuarryApi quarryWebclient;

	private BasketDto basket;

	static <T> List<T> append(List<T> immutableList, T element) {
		var tmpList = new ArrayList<>(immutableList);
		tmpList.add(element);
		return Collections.unmodifiableList(tmpList);
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
					case "boar" -> 5; // oh boy, oh boy!
					case "honey" -> 2;
					case "magic potion" -> 0; // not allowed to drink this!
					default -> 1; // everything is worth something
				} * x.count()).reduce(0, Integer::sum);
		log.info("basket worth {} vs menhir worth {} ({})", basketWorth, decorativeness, stoneWorth);
		return basketWorth >= stoneWorth;
	}

	public void exchange(UUID menhirId) {
		var menhir = getMenhirById(menhirId);
		var decorativeness = menhir.decorativeness();
		if (!isGoodOffer(decorativeness)) {
			throw new BadOfferException("Bad Offer: That won't even feed Idefix!");
		}
		deleteById(menhirId);
		leave();
	}

	public List<MenhirDto> getAllMenhirs() {
		return quarryWebclient.getAllMenhirs();
	}

	public MenhirDto getMenhirById(UUID menhirId) {
		return quarryWebclient.getMenhirById(menhirId);
	}

	public void deleteById(UUID menhirId) {
		quarryWebclient.deleteById(menhirId);
	}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class BadOfferException extends RuntimeException {}
}

package ch.bbw.obelix.quarry.service;

import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuarryService {

	private final MenhirRepository menhirRepository;
	private final MeterRegistry meterRegistry;

	@PostConstruct
	public void initializeMenhirs() {
		if (menhirRepository.count() == 0) {
			createDefaultMenhirs();
		}
		Gauge.builder("obelix.quarry.menhirs.available", menhirRepository, MenhirRepository::count)
				.description("number of available menhirs in quarry")
				.register(meterRegistry);
		log.info("registered gauge: obelix.quarry.menhirs.available");
	}

	public void createDefaultMenhirs() {
		menhirRepository.deleteAll();

		var obelixSpecial = new MenhirEntity();
		obelixSpecial.setWeight(2.5);
		obelixSpecial.setStoneType("Granite Gaulois");
		obelixSpecial.setDecorativeness(MenhirEntity.Decorativeness.DECORATED);
		obelixSpecial.setDescription("Obelix's personal favorite! Perfect for throwing at Romans. ");
		menhirRepository.save(obelixSpecial);

		var getafixMasterpiece = new MenhirEntity();
		getafixMasterpiece.setWeight(4.2);
		getafixMasterpiece.setStoneType("Mystical Dolmen Stone");
		getafixMasterpiece.setDecorativeness(MenhirEntity.Decorativeness.MASTERWORK);
		getafixMasterpiece.setDescription("Blessed by Getafix himself! This menhir is rumored to " +
				"enhance magic potion brewing. Side effects may include: sudden urge to fight Romans.");
		menhirRepository.save(getafixMasterpiece);

		var touristTrap = new MenhirEntity();
		touristTrap.setWeight(1.0);
		touristTrap.setStoneType("Imported Roman Marble");
		touristTrap.setDecorativeness(MenhirEntity.Decorativeness.PLAIN);
		touristTrap.setDescription("Budget-friendly option! Made from 'liberated' Roman materials. " +
				"Perfect for beginners or those who just want to annoy Caesar. Asterix approved!");
		menhirRepository.save(touristTrap);
		
		log.info("initialized {} menhirs in quarry", menhirRepository.count());
	}
}

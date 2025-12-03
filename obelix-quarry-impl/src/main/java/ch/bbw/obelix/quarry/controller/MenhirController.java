package ch.bbw.obelix.quarry.controller;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import lombok.RequiredArgsConstructor;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MenhirController implements QuarryApi {


    private final MenhirRepository menhirRepository;

    public List<MenhirDto> getAllMenhirs() {
        return menhirRepository.findAll()
                .stream().map(MenhirEntity::toDto).toList();
    }

	public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
		return menhirRepository.findById(menhirId)
				.map(MenhirEntity::toDto)
				.orElseThrow(() -> new UnknownMenhirException("unknwon menhir with id " + menhirId));
	}

	public void deleteById(@PathVariable UUID menhirId) {
		menhirRepository.deleteById(menhirId);
	}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class UnknownMenhirException extends RuntimeException {}
}

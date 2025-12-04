package ch.bbw.obelix.quarry.controller;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.dto.MenhirDto;

import ch.bbw.obelix.quarry.service.QuarryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuarryController implements QuarryApi {

    private final QuarryService quarryService;

    @Override
    @GetMapping("/api/menhirs")
    public List<MenhirDto> getAllMenhirs() {
        return quarryService.getAllMenhirs();
    }

    @Override
    @GetMapping("/api/menhirs/{menhirId}")
    public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
        return quarryService.getMenhirById(menhirId);
    }

    @Override
    @DeleteMapping("/api/quarry/{menhirId}")
    public void deleteById(@PathVariable UUID menhirId) {
        quarryService.deleteById(menhirId);
    }

    @StandardException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UnknownMenhirException extends RuntimeException {}

}

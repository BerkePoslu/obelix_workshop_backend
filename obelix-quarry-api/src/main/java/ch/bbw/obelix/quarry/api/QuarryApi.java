package ch.bbw.obelix.quarry.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;

public interface QuarryApi {
    @GetMapping("/api/menhirs")
    public List<MenhirDto> getAllMenhirs();

    @GetMapping("/api/menhirs/{menhirId}")
    public MenhirDto getMenhirById(@PathVariable UUID menhirId);

    @DeleteMapping("/api/quarry/{menhirId}")
    public void deleteById(@PathVariable UUID menhirId);
}

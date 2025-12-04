package ch.bbw.obelix.webshop.service;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.dto.MenhirDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuarryWebClientService {

    @Value("${obelix.quarry.url}")
    private String quarryUrl;

    public QuarryApi newClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(quarryUrl)
                .defaultStatusHandler(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error from Quarry Service: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Error from Quarry Service: " + clientResponse.statusCode()));
                })
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(QuarryApi.class);
    }

    public List<MenhirDto> getAllMenhirs() {
        return newClient().getAllMenhirs();
    }

    public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
        return newClient().getMenhirById(menhirId);
    }


    public void deleteById(@PathVariable UUID menhirId) {
        newClient().deleteById(menhirId);
    }
}

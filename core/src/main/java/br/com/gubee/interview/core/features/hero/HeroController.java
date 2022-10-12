package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.RetriveHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetriveHeroRequest> findHeroById(@PathVariable(required = false) String id){
        final RetriveHeroRequest retriveHeroRequest = heroService.retriveById(UUID.fromString(id));
        return ok(retriveHeroRequest);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<RetriveHeroRequest>> findHeroByName(@RequestParam(required = false) String name){
        final List<RetriveHeroRequest> retriveHeroRequest = heroService.retriveByName(name);
        return retriveHeroRequest.size() > 0 ? ok(retriveHeroRequest) : ok().build();
    }

    @PutMapping()
    public ResponseEntity<String> updateHero(@RequestBody UpdateHeroRequest updateHeroRequest){
            heroService.update(updateHeroRequest);
        return ok().build();
    }
}

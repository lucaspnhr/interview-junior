package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.request.BattleHeroRequest;
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
import static org.springframework.http.ResponseEntity.*;

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
    public ResponseEntity<RetriveHeroRequest> findHeroById(@PathVariable(required = false) UUID id){
        final RetriveHeroRequest retriveHeroRequest = heroService.retriveById(id);
        return ok(retriveHeroRequest);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<RetriveHeroRequest>> findHeroByName(@RequestParam(required = false) String name){
        final List<RetriveHeroRequest> retriveHeroRequest = heroService.retriveByName(name);
        return retriveHeroRequest.size() > 0 ? ok(retriveHeroRequest) : ok().build();
    }

    @GetMapping("/battle")
    public ResponseEntity<BattleHeroRequest> compareToHeroes(@RequestParam(required = false) UUID firstHero,
                                                             @RequestParam(required = false) UUID secondHero){
        List<RetriveHeroRequest> herosToCompare =  heroService.retriveHerosByIds((firstHero), (secondHero));
        BattleHeroRequest battleHeroRequest = new BattleHeroRequest(herosToCompare.get(0), herosToCompare.get(1));
        return ok(battleHeroRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RetriveHeroRequest> updateHero(@PathVariable UUID id, @RequestBody UpdateHeroRequest updateHeroRequest){
        RetriveHeroRequest updatedHero = heroService.update(id, updateHeroRequest);
        return ok(updatedHero);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHeroById(@PathVariable UUID id){
        heroService.deleteById(id);
        return noContent().build();
    }


}

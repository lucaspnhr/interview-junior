package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.customException.NotFoundHeroException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.RetriveHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final PowerStatsService powerStatsService;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID powerStatsId = powerStatsService.create(new PowerStats(createHeroRequest));
        return heroRepository.create(new Hero(createHeroRequest, powerStatsId));
    }

    @Transactional
    public RetriveHeroRequest retriveById(UUID id){
        Hero retrivedHero = (heroRepository.retriveById(id)).orElseThrow(()->{throw new NotFoundHeroException(id);});
        PowerStats powerStats = powerStatsService.retriveById(retrivedHero.getPowerStatsId());
        return RetriveHeroRequest.builder()
                .heroId(retrivedHero.getId())
                .name(retrivedHero.getName())
                .race(retrivedHero.getRace())
                .strength(powerStats.getStrength())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence()).build();
    }
    @Transactional
    public List<RetriveHeroRequest> retriveByName(String name){
        if( name == null || name.isEmpty()){
            return Collections.emptyList();
        }
        List<Hero> retrivedHeros = heroRepository.retriveByName(name);
        return retrivedHeros.stream().filter((hero) -> hero.getName().contains(name)).map((hero) -> {
            PowerStats powerStats = powerStatsService.retriveById(hero.getPowerStatsId());
            return RetriveHeroRequest.builder()
                    .heroId(hero.getId())
                    .name(hero.getName())
                    .race(hero.getRace())
                    .strength(powerStats.getStrength())
                    .agility(powerStats.getAgility())
                    .dexterity(powerStats.getDexterity())
                    .intelligence(powerStats.getIntelligence()).build();
        }).collect(Collectors.toList());
    }
}

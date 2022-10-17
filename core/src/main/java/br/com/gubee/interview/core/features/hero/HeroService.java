package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.customException.NotFoundHeroException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.RetriveHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
        return createRetriveHero(retrivedHero);
    }
    @Transactional
    public List<RetriveHeroRequest> retriveByName(String name){
        if( name == null || name.isEmpty()){
            return Collections.emptyList();
        }
        List<Hero> retrivedHeros = heroRepository.retriveByName(name);
        return retrivedHeros.stream().filter((hero) -> hero.getName().toLowerCase().contains(name.toLowerCase())).map(this::createRetriveHero).collect(Collectors.toList());
    }

    @Transactional
    public RetriveHeroRequest update(UUID id, UpdateHeroRequest updateHeroRequest){
        RetriveHeroRequest retriveHero = new RetriveHeroRequest();
        Hero hero = (heroRepository.retriveById(id)).orElseThrow(() -> {
            throw new NotFoundHeroException(id);
        });
        powerStatsService.update(updateHeroRequest, hero.getPowerStatsId());
        int updateReturn = heroRepository.updateHero(id, updateHeroRequest);
        if(updateReturn > 0){
            retriveHero = heroRepository.retriveById(id).map(this::createRetriveHero).get();
        }
        return retriveHero;
    }
    @Transactional
    public void deleteById(UUID id){
        Hero hero = heroRepository.retriveById(id).orElseThrow(() -> new NotFoundHeroException(id));
        powerStatsService.deleteById(hero.getPowerStatsId());
        heroRepository.delete(hero.getId());
    }
    @Transactional
    public List<RetriveHeroRequest> retriveHerosByIds(UUID firstHero, UUID secondHero) {
        Hero firstRetrivedHero = (heroRepository.retriveById(firstHero)).orElseThrow(()->{throw new NotFoundHeroException(firstHero);});
        Hero secondRetrivedHero = (heroRepository.retriveById(secondHero)).orElseThrow(()->{throw new NotFoundHeroException(secondHero);});
        return List.of(createRetriveHero(firstRetrivedHero),createRetriveHero(secondRetrivedHero));
    }

    private RetriveHeroRequest createRetriveHero(Hero hero) {
        PowerStats powerStats = powerStatsService.retriveById(hero.getPowerStatsId());
        return RetriveHeroRequest.builder()
                .id(hero.getId())
                .name(hero.getName())
                .race(hero.getRace())
                .strength(powerStats.getStrength())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence()).build();
    }



}

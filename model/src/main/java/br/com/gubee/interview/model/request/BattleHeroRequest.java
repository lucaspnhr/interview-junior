package br.com.gubee.interview.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(access = PRIVATE)
public class BattleHeroRequest {
    private Map<String, UUID> heros = new HashMap<>();

    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

    public BattleHeroRequest(RetriveHeroRequest firstHero,
                             RetriveHeroRequest secondHero ) {
        heros.put(firstHero.getName(), firstHero.getId());
        heros.put(secondHero.getName(), secondHero.getId());
        this.strength = firstHero.getStrength() - secondHero.getStrength();
        this.agility = firstHero.getAgility() - secondHero.getAgility();
        this.dexterity = firstHero.getDexterity() - secondHero.getDexterity();
        this.intelligence = firstHero.getIntelligence() - secondHero.getIntelligence();
    }

}

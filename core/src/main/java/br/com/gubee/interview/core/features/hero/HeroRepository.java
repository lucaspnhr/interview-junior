package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.customException.NotFoundHeroException;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
        " (name, race, power_stats_id)" +
        " VALUES (:name, :race, :powerStatsId) RETURNING id";
    private static final String RETRIVE_HERO_BY_ID_QUERY = "SELECT * FROM hero WHERE id = :id";
    private static final String UPDATE_HERO_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, updated_at = now()" +
            " WHERE id = :id";
    private static final String RETRIVE_ALL_HERO_QUERY = "SELECT * FROM hero";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
            "race", hero.getRace().name(),
            "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
            CREATE_HERO_QUERY,
            params,
            UUID.class);
    }

    Optional<Hero> retriveById(UUID heroId){
        final Map<String, Object> params = Map.of("id", heroId);
        try{
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                    RETRIVE_HERO_BY_ID_QUERY,
                    params,
                    new BeanPropertyRowMapper<>(Hero.class)
            ));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    List<Hero> retriveByName(String name){
            return namedParameterJdbcTemplate.query(
                    RETRIVE_ALL_HERO_QUERY,
                    new BeanPropertyRowMapper<>(Hero.class)
            );
    }
    int updateHero(UpdateHeroRequest heroToUpdate){
        final Map<String, Object> params = Map.of("id", heroToUpdate.getId(),
                "name", heroToUpdate.getName(),
                "race", heroToUpdate.getRace().name());

        return namedParameterJdbcTemplate.update(
                UPDATE_HERO_QUERY,
                params
        );
    }


}

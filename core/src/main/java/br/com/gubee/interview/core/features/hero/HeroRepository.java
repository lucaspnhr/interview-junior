package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.customException.NotFoundHeroException;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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

    Optional<Hero> retriveById(UUID id){
        final Map<String, Object> params = Map.of("id", id);
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
}

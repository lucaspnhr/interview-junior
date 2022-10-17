package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";
    private static final String UPDATE_HERO_QUERY = "UPDATE power_stats" +
            " SET strength = :strength, agility = :agility, dexterity = :dexterity, intelligence = :intelligence, updated_at = now()" +
            " WHERE id = :id";
    private static final String RETRIVE_POWER_STATS_BY_ID_QUERY = "SELECT * FROM power_stats WHERE id = :id";
    public static final String DELETE_POWER_STATS_BY_ID = "DELETE FROM power_stats WHERE id = :id RETURNING id";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(powerStats),
            UUID.class);
    }

    PowerStats retriveById(UUID id){
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.query(
                RETRIVE_POWER_STATS_BY_ID_QUERY,
                params,
                new BeanPropertyRowMapper<>(PowerStats.class)
        ).get(0);
    }

    int updatePowerStats(UpdateHeroRequest heroToUpdate, UUID powerStatsId){
        final Map<String, Object> params = Map.of("id", powerStatsId,
                "strength", heroToUpdate.getStrength(),
                "agility", heroToUpdate.getAgility(),
                "dexterity", heroToUpdate.getDexterity(),
                "intelligence", heroToUpdate.getIntelligence());

        return namedParameterJdbcTemplate.update(
                UPDATE_HERO_QUERY,
                params
        );
    }

    UUID delete(UUID id){
        Map<String, UUID> param = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                DELETE_POWER_STATS_BY_ID,
                param,
                UUID.class
        );
    }
}

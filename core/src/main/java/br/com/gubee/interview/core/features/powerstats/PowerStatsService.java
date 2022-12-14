package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    @Transactional
    public PowerStats retriveById(UUID id){
        return powerStatsRepository.retriveById(id);
    }
    @Transactional
    public int update(UpdateHeroRequest updateHeroRequest, UUID powerStatsId) {
        return powerStatsRepository.updatePowerStats(updateHeroRequest, powerStatsId);
    }
    @Transactional
    public UUID deleteById(UUID powerStatsId) {
        return powerStatsRepository.delete(powerStatsId);
    }
}

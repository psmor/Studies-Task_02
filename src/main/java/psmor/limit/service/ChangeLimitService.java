package psmor.limit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psmor.limit.entity.ChangeLimits;
import psmor.limit.exception.NotChangeLimitException;
import psmor.limit.repositoriy.ChangeLimitsRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeLimitService {
    private final ChangeLimitsRepository changeLimitsRepository;

    // Вывести список резервов у пользователя
    public List<ChangeLimits> getChangeLimits(Long limitId){
        List<ChangeLimits> changeLimits = changeLimitsRepository.findByLimId(limitId);
        return changeLimits;
    }

    // Вывести сумму общего резерва у пользователя
    public double getChangeLimit(Long limitId){
        List<ChangeLimits> changeLimits = getChangeLimits(limitId);
        double resault = 0.0;
        for (ChangeLimits c : changeLimits) {
            resault = c.getChangeLimit();
        }
        return resault;
    }

    // Сохранить резерв
    public void saveChangeLimit(ChangeLimits changeLimits){
        changeLimitsRepository.save(changeLimits);
    }

    public ChangeLimits getChangeLimitTran(UUID idTransavtion){
        return changeLimitsRepository.findByTransactionId(idTransavtion)
                .orElseThrow(() -> new NotChangeLimitException());
    }

    public void deleteChangeLimit(ChangeLimits changeLimits){
        changeLimitsRepository.delete(changeLimits);
    }
}

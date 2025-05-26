package psmor.limit.sheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import psmor.limit.service.LimitsService;

@Service
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class ShedulerService {
    private final LimitsService limitsService;

    @Scheduled(cron = "0 0 0 * * *")         // каждый день в 00:00
    //@Scheduled(cron = "0 */1 * * * *")     // Раз в минуту (оставил для теста)
    public void resetLimits() {
        log.info("Старт шедулен");
        limitsService.resetLimit();
        log.info("Стоп шедулен");
    }
}

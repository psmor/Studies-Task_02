package psmor.limit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psmor.limit.dto.*;
import psmor.limit.entity.ChangeLimits;
import psmor.limit.entity.Limits;
import psmor.limit.exception.LimitNumberException;
import psmor.limit.exception.NotEnougtMoneyException;
import psmor.limit.exception.UserNumberException;
import psmor.limit.repositoriy.LimitsRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LimitsService {

    private final LimitsRepository limitsRepository;
    private final ChangeLimitService changeLimitService;

    @Value("${application-settings.limit-sitze}")
    double limitSize;

    // Проверка пользователей на соответствие диапазону
    private Boolean checkUser(Integer userId) {
        if (userId >= 1 && userId <= 100){
            return true;
        }
        return false;
    }

    // Найти лимит по его ID
    public Limits getLimits(Long id) {
        log.info("Запрос лимита по его ID="+id);
        return limitsRepository.findById(id).orElseThrow();   // NoSuchElementException - по умолчанию
    }

    // Получить лимит у определённого пользователя, если такого нет, создать лимит для нового пользователя.
    public Limits getByUserIdLim(Integer userId) {
        log.info("Запрос лимита по его userId="+userId);
        Optional<Limits> limitSql = limitsRepository.findByUserId(userId);
        Limits limit;
        if (limitSql.isPresent()) {              // если объект присутствует, получим его
            log.info("Пользователь существует");
            limit = limitSql.get();
        } else {                               // если нет, создадим нового
            log.info("Создаём нового пользователя");
            if (!checkUser(userId)){
                throw new UserNumberException("Номер клиента "+userId+" не соответствует диапазону 1-100");
            }
            limit = new Limits();
            limit.setUserId(userId);
            limit.setCurrentLimit(limitSize);     // Размер лимита берём из настроек
            limit = limitsRepository.save(limit); // Обновляем запись в БД
        }
        return limit;
    }

    // Получить лимит у определённого пользователя, если такого нет, создать лимит для нового пользователя.
    public LimitResponseDto getByUserId(Integer userId){
        Limits limits = getByUserIdLim(userId);
        return new LimitResponseDto(limits.getUserId(), limits.getCurrentLimit());
    }

    public void resetLimit() {
        limitsRepository.updateChangeLimitsAll(limitSize);
    }

    // Проверка на достаточность
    public void checkBalance(double currentBalance, double changeBalance){
        if (currentBalance - changeBalance < 0) { // Если сумма изменения будет отрицательной, значит применится вычитание
            throw new NotEnougtMoneyException("Недостаточно денег для выполнения операции. Текущий остаток = "+currentBalance);
        }
    }

    // Зарезервировать средства у пользователя
    public LimitChangeResponseDto changeLimit(LimitChangeRequestDto limitChangeRequestDto) {
        // Проверим, что сумму передали не отрицательную
        if (limitChangeRequestDto.summa() <= 0) {
            throw new LimitNumberException("Сумма на списание должна быть больше 0.");
        }
        // находим лимит для изменения
        Limits limits = getByUserIdLim(limitChangeRequestDto.clientId());
        // Находим остаток с учётом резервирования
        double balanceAll = limits.getCurrentLimit()-changeLimitService.getChangeLimit(limits.getId());
        log.info("Текущий остаток, с учетом зарезервированных сумм = "+balanceAll);
        // проверяем на достаточность средств. Если недостаточно - упадём.
        checkBalance(balanceAll, limitChangeRequestDto.summa());
        // сохраню транзакцию
        ChangeLimits changeLimits = new ChangeLimits();
        changeLimits.setTransactionId(UUID.randomUUID());
        changeLimits.setLimId(limits.getId());
        changeLimits.setChangeLimit(limitChangeRequestDto.summa());
        changeLimitService.saveChangeLimit(changeLimits);

        return new LimitChangeResponseDto(changeLimits.getTransactionId());
    }

    // Подтвердить изменение средств
    public LimitResponseDto confirm(LimitConfirmRequestDto limitConfirmRequestDto) {
        // находим транзакцию
        ChangeLimits changeLimits = changeLimitService.getChangeLimitTran(limitConfirmRequestDto.idTransaction());
        // находим лимит для изменения
        Limits limits = getLimits(changeLimits.getLimId());
        // Находим остаток с учётом резервирования
        double balanceAll = limits.getCurrentLimit()-changeLimitService.getChangeLimit(limits.getId());
        // проверяем на достаточность средств. Если недостаточно - упадём.
        checkBalance(balanceAll, changeLimits.getChangeLimit());
        // Изменим остаток
        limits.setCurrentLimit(limits.getCurrentLimit()-changeLimits.getChangeLimit());
        limitsRepository.save(limits);
        // удаляем транзакцию с изменениями
        changeLimitService.deleteChangeLimit(changeLimits);

        return new LimitResponseDto(limits.getUserId(), limits.getCurrentLimit());
    }

    // Откатить изменения
    public LimitRollBackResponceDto rollBack(LimitConfirmRequestDto limitConfirmRequestDto){
        // находим транзакцию для изменения
        ChangeLimits changeLimits = changeLimitService.getChangeLimitTran(limitConfirmRequestDto.idTransaction());
        // удаляем транзакцию с изменениями
        changeLimitService.deleteChangeLimit(changeLimits);
        return new LimitRollBackResponceDto("success");
    }
}

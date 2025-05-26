package psmor.limit.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psmor.limit.dto.*;
import psmor.limit.entity.Limits;
import psmor.limit.exception.LimitNumberException;
import psmor.limit.exception.NotChangeLimitException;
import psmor.limit.exception.NotEnougtMoneyException;
import psmor.limit.exception.UserNumberException;
import psmor.limit.service.LimitsService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/v1/limits")
@AllArgsConstructor
public class LimitsController {
    private LimitsService limitsService;

    // Поиск лимита у клиента
    @GetMapping(path = "/user/{id}")
    public LimitResponseDto getLimit(@PathVariable("id") Integer id) {
        return limitsService.getByUserId(id);
    }

    // Поиск лимита по его id
    @GetMapping(path = "/limit/{id}")
    public Limits getUserLimit(@PathVariable("id") Long id) {
        return limitsService.getLimits(id);
    }

    // Зарезервировать лимит
    @PostMapping(path = "/change")
    public LimitChangeResponseDto changeLimit(@RequestBody LimitChangeRequestDto limitChangeRequestDto) {
        return limitsService.changeLimit(limitChangeRequestDto);
    }

    // Подтвердить изменение лимита по его id транзакции
    @PostMapping(path = "/confirm")
    public LimitResponseDto confirmLimit(@RequestBody LimitConfirmRequestDto limitConfirmRequestDto) {
        return limitsService.confirm(limitConfirmRequestDto);
    }

    // Откатить изменение лимита по его id транзакции
    @PostMapping(path = "/rollback")
    public LimitRollBackResponceDto rollBackLimit(@RequestBody LimitConfirmRequestDto limitConfirmRequestDto) {
        return limitsService.rollBack(limitConfirmRequestDto);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LimitNumberException.class)
    public LimitResponseErrorDto handleException(LimitNumberException e) {    // Ошибка поиска лимита
        return new LimitResponseErrorDto("Сумма списания указана не корректно.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNumberException.class)
    public LimitResponseErrorDto handleException(UserNumberException e) {    // Ошибка поиска лимита
        return new LimitResponseErrorDto("Не верный номер клиента.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchElementException.class)
    public LimitResponseErrorDto handleException(NoSuchElementException e) {    // Ошибка поиска лимита
        return new LimitResponseErrorDto("Лимит не найден.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotEnougtMoneyException.class)
    public LimitResponseErrorDto handleException(NotEnougtMoneyException e) {    // Ошибка поиска лимита
        return new LimitResponseErrorDto("Недостаточно денег у клиента", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotChangeLimitException.class)
    public LimitResponseErrorDto handleException(NotChangeLimitException e) {    // Ошибка поиска лимита
        return new LimitResponseErrorDto("Не найден запрос на изменение лимита", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public LimitResponseErrorDto handleException(Exception e) {                // Другие ошибки
        return new LimitResponseErrorDto("Не известная ошибка.", e.getMessage());
    }
}

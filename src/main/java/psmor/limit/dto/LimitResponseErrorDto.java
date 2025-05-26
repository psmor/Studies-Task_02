package psmor.limit.dto;

public record LimitResponseErrorDto (
        String reasonMessage,
        String exceptionMessage
) {}

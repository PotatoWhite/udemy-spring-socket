package me.potato.udemyspringsocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.potato.udemyspringsocket.dto.error.ErrorEvent;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Response<T> {
    ErrorEvent errorResponse;
    T          successResponse;

    public Response(ErrorEvent errorResponse) {
        this.errorResponse = errorResponse;
    }

    public Response(T successResponse) {
        this.successResponse = successResponse;
    }

    public boolean hasError() {
        return Objects.nonNull(errorResponse);
    }

    public static <T> Response<T> with(T t) {
        return new Response<T>(t);
    }

    public static <T> Response<T> with(ErrorEvent errorEvent) {
        return new Response<T>(errorEvent);
    }
}

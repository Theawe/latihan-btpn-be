package example.bank.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DataResponseBuilder<T extends Object> extends BaseResponseBuilder {
    private T data;

    @Builder
    public DataResponseBuilder(HttpStatus status, String message, T data) {
        super(status, message);

        this.data = data;
    }
}

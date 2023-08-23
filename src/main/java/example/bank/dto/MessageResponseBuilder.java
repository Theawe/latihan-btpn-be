package example.bank.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;

public class MessageResponseBuilder extends BaseResponseBuilder {
    @Builder
    public MessageResponseBuilder(HttpStatus status, String message) {
        super(status, message);
    }

}

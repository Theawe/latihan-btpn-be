package example.bank.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionReq {

    private Integer accountId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;

    private String description;

    private String debitCreditStatus;

    private BigDecimal amount;
}

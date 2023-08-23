package example.bank.dto.response;

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
public class ReportRes {

    @JsonFormat(pattern = "YYYY/MM/dd", timezone = "Asia/Jakarta")
    private Date transactionDate;
    private String description;
    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal balance;
}

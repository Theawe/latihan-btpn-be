package example.bank.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @Column(name = "description")
    private String description;

    @Column(name = "debit_credit_status")
    private String debitCreditStatus;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}

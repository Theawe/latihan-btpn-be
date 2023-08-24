package example.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import example.bank.dto.request.AccountReq;
import example.bank.dto.request.TransactionReq;
import example.bank.dto.response.AccountRes;
import example.bank.dto.response.PointRes;
import example.bank.dto.response.ReportRes;
import example.bank.dto.DataResponseBuilder;
import example.bank.dto.MessageResponseBuilder;
import example.bank.model.Account;
import example.bank.model.Transaction;
import example.bank.repository.AccountRepo;
import example.bank.repository.TransactionRepo;

@Service
public class AccountService {

    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;

    public AccountService(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public ResponseEntity<DataResponseBuilder<AccountRes>> createAccount(AccountReq request) {
        Account newAccount = Account.builder()
                .accountId(request.getAccountId())
                .name(request.getName())
                .balance(BigDecimal.valueOf(0L))
                .build();
        System.out.println(newAccount.getAccountId());
        newAccount = accountRepo.save(newAccount);
        System.out.println(newAccount.getAccountId());
        AccountRes accountRes = AccountRes.builder()
                .accountId(newAccount.getAccountId())
                .name(newAccount.getName())
                .build();
        DataResponseBuilder<AccountRes> response = DataResponseBuilder.<AccountRes>builder()
                .status(HttpStatus.CREATED)
                .message("Success Created Nasabah")
                .data(accountRes)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // TODO Validasi
    public ResponseEntity<MessageResponseBuilder> inputTransaction(TransactionReq request) {
        Optional<Account> accountOpt = accountRepo.findById(request.getAccountId());

        if (accountOpt.isEmpty()) {
            MessageResponseBuilder response = MessageResponseBuilder
                    .builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Account " + request.getAccountId() + " Not Found")
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Account account = accountOpt.get();

        if (!request.getDebitCreditStatus().equalsIgnoreCase("C")
                && !request.getDebitCreditStatus().equalsIgnoreCase("D")) {
            MessageResponseBuilder response = MessageResponseBuilder
                    .builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Fail")
                    .build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Transaction transaction = Transaction.builder()
                .account(new Account(request.getAccountId()))
                .debitCreditStatus(request.getDebitCreditStatus())
                .transactionDate(request.getTransactionDate())
                .description(request.getDescription())
                .amount(request.getAmount())
                .build();

        // Todo Check balance

        // Todo add amount if "C"/Setor Tunai
        if (request.getDebitCreditStatus().equalsIgnoreCase("C")) {
            account.setBalance(account.getBalance().add(request.getAmount()));
            transaction.setBalance(account.getBalance());
        } else {
            // TODO Check balance
            account.setBalance(account.getBalance().subtract(request.getAmount()));
            transaction.setBalance(account.getBalance());
        }

        account = accountRepo.save(account);
        transaction = transactionRepo.save(transaction);
        MessageResponseBuilder response = MessageResponseBuilder
                .builder()
                .status(HttpStatus.OK)
                .message("Insert Transaction Success")
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<DataResponseBuilder<List<PointRes>>> getAllPoints() {

        List<Account> listAccount = accountRepo.findAll();
        List<PointRes> lPointRes = new ArrayList<>();
        for (Account account : listAccount) {
            List<Transaction> listTransaction = transactionRepo.selectDebitOnly(account.getAccountId());
            int totalPoint = 0;
            for (Transaction transaction : listTransaction) {
                if (transaction.getDescription().equalsIgnoreCase("Bayar Listrik")) {
                    totalPoint += calculatePointsForListrikPurchase(transaction.getAmount().doubleValue());
                } else if (transaction.getDescription().equalsIgnoreCase("Bayar Pulsa")) {
                    totalPoint += calculatePointsForPulsaPurchase(transaction.getAmount().doubleValue());
                }
            }
            PointRes pointRes = PointRes.builder()
                    .accountId(account.getAccountId())
                    .name(account.getName())
                    .points(totalPoint)
                    .build();
            lPointRes.add(pointRes);
        }
        DataResponseBuilder<List<PointRes>> response = DataResponseBuilder.<List<PointRes>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(lPointRes)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<Object> createReport(Integer accountId, Date starDate, Date endDate) {
        List<Transaction> lTransactions = transactionRepo.selectTransactionNonNative(accountId,
                starDate, endDate);
        List<ReportRes> lReportRes = new ArrayList<>();
        for (Transaction transaction : lTransactions) {
            ReportRes reportRes = ReportRes.builder()
                    .transactionDate(transaction.getTransactionDate())
                    .description(transaction.getDescription())
                    .balance(transaction.getBalance())
                    .build();
            if (transaction.getDebitCreditStatus().equalsIgnoreCase("C")) {
                reportRes.setCredit(transaction.getAmount());
            } else {
                reportRes.setDebit(transaction.getAmount());
            }
            lReportRes.add(reportRes);
        }
        DataResponseBuilder<List<ReportRes>> response = DataResponseBuilder.<List<ReportRes>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(lReportRes)
                .build();

        return ResponseEntity.ok(response);
    }

    private int calculatePointsForListrikPurchase(Double amount) {
        if (amount <= 50000) {
            return 0;
        } else if (amount <= 100000) {
            Double remainingAmount = (amount - 50000);
            int point = (int) Math.round(remainingAmount / 2000 * 1);
            return point + calculatePointsForListrikPurchase(amount - remainingAmount);
        } else {
            Double remainingAmount = (amount - 100000);
            int point = (int) Math.round(remainingAmount / 2000 * 2);
            return point + calculatePointsForListrikPurchase(amount - remainingAmount);
        }
    }

    private int calculatePointsForPulsaPurchase(Double amount) {
        if (amount <= 10000) {
            return 0;
        } else if (amount <= 30000) {
            Double remainingAmount = (amount - 10000);
            int point = (int) Math.round(remainingAmount / 1000 * 1);
            return point + calculatePointsForPulsaPurchase(amount - remainingAmount);
        } else {
            Double remainingAmount = (amount - 30000);
            int point = (int) Math.round(remainingAmount / 1000 * 2);
            return point + calculatePointsForPulsaPurchase(amount - remainingAmount);
        }
    }

    public ResponseEntity<DataResponseBuilder<List<AccountRes>>> getAllAccount() {
        List<Account> listAccount = accountRepo.findAll(Sort.by(Sort.Direction.ASC, "accountId"));
        List<AccountRes> lAccountRes = new ArrayList<>();
        for (Account account : listAccount) {
            AccountRes accountRes = AccountRes.builder()
                    .accountId(account.getAccountId())
                    .name(account.getName())
                    .balance(account.getBalance())
                    .build();
            lAccountRes.add(accountRes);
        }
        DataResponseBuilder<List<AccountRes>> response = DataResponseBuilder.<List<AccountRes>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(lAccountRes)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}

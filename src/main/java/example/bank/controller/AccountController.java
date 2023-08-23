package example.bank.controller;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.bank.dto.DataResponseBuilder;
import example.bank.dto.MessageResponseBuilder;
import example.bank.dto.request.AccountReq;
import example.bank.dto.request.TransactionReq;
import example.bank.dto.response.AccountRes;
import example.bank.dto.response.PointRes;
import example.bank.service.AccountService;

@RestController
@RequestMapping("api/v1/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<DataResponseBuilder<AccountRes>> createAccount(@RequestBody @Valid AccountReq request) {

        return accountService.createAccount(request);
    }

    @Transactional
    @PostMapping("/transaction")
    public ResponseEntity<MessageResponseBuilder> inputTransaction(
            @RequestBody @Valid TransactionReq request) {
        return accountService.inputTransaction(request);
    }

    @GetMapping("/transaction")
    public ResponseEntity<DataResponseBuilder<List<PointRes>>> getAllPoint() {
        return accountService.getAllPoints();
    }

    @GetMapping("/report")
    public ResponseEntity<Object> createReport(@RequestParam(name = "accountId") Integer accountId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "startDate") Date starDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "endDate") Date endDate) {
        return accountService.createReport(accountId, starDate, endDate);
    }
}

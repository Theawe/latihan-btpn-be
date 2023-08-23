package example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.bank.model.Account;

public interface AccountRepo extends JpaRepository<Account, Integer> {

}

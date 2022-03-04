package spring.security.oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.oauth2.model.Account;
import spring.security.oauth2.service.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAuthority('admin') and hasAuthority('manage-users')")
    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody Account account) throws Exception {
        return ResponseEntity.ok(accountService.create(account));
    }

}

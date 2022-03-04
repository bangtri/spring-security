package spring.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.security.oauth2.exception.PartialUpdateException;
import spring.security.oauth2.model.Account;
import spring.security.oauth2.model.User;
import spring.security.oauth2.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;

    private final GenericService genericService;

    private final AccountRepository accountRepository;

    public Account create(Account account) throws Exception {
        if(accountRepository.findByPhoneNumber(account.getPhoneNumber()).isPresent()) {
            throw new PartialUpdateException(genericService.message("error.account.phone.exists"));
        }
        if(accountRepository.findByPhoneNumber(account.getEmail()).isPresent()) {
            throw new PartialUpdateException(genericService.message("error.account.email.exists"));
        }
        User user = User.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .username(account.getPhoneNumber())
                .email(account.getEmail())
                .enabled(account.isEnable())
                .build();
        userService.create(user);
        return accountRepository.save(account);
    }

}

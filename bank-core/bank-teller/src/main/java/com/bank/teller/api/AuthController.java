package com.bank.teller.api;

import com.bank.common.result.Result;
import com.bank.teller.application.auth.LoginCommand;
import com.bank.teller.application.auth.AuthService;
import com.bank.teller.application.auth.LoginResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teller/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginCommand command) {
        return Result.success(authService.login(command));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", defaultValue = "") String token) {
        authService.logout(token);
        return Result.success();
    }

    @GetMapping("/me")
    public Result<LoginResponse> me(@RequestHeader(value = "Authorization", defaultValue = "") String token) {
        return Result.success(authService.validate(token));
    }
}
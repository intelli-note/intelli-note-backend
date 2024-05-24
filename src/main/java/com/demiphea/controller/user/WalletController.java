package com.demiphea.controller.user;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.po.user.WalletUpdate;
import com.demiphea.model.vo.user.Wallet;
import com.demiphea.service.inf.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * WalletController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final UserService userService;

    @GetMapping
    @Auth
    public ApiResponse getUserWallet(@AuthID Long id) {
        Wallet wallet = userService.getUserWallet(id);
        return ApiResponse.success(wallet);
    }

    @PutMapping
    @Auth
    public ApiResponse updateUserWallet(@AuthID Long id, @RequestBody @Validated WalletUpdate walletUpdate) {
        Wallet wallet = userService.updateUserWallet(id, walletUpdate);
        return ApiResponse.success(wallet);
    }

}

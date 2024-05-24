package com.demiphea.controller.user;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.service.inf.user.BillService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BillController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping
    @Auth
    public ApiResponse listBills(
            @AuthID
            Long id,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = billService.listBills(id, pageNum, pageSize);
        return ApiResponse.success(result);
    }
}

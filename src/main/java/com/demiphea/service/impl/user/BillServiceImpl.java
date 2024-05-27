package com.demiphea.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.BillDao;
import com.demiphea.entity.Bill;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.user.BillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BillServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BaseService baseService;
    private final BillDao billDao;

    @Override
    public Boolean checkPermission(@Nullable Long userId, @NotNull Long billId) {
        if (userId == null) {
            return false;
        }
        Bill bill = billDao.selectById(billId);
        if (bill == null) {
            throw new ObjectDoesNotExistException("账单不存在或已删除");
        }
        return checkPermission(userId, bill);
    }

    @Override
    public Boolean checkPermission(@Nullable Long userId, @NotNull Bill bill) {
        if (userId == null) {
            return false;
        }
        return userId.equals(bill.getUserId());
    }

    @Override
    public PageResult listBills(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Bill> bills = billDao.selectList(new LambdaQueryWrapper<Bill>().eq(Bill::getUserId, id).orderByDesc(Bill::getCreateTime));
        PageInfo<Bill> pageInfo = new PageInfo<>(bills);
        List<BillVo> list = bills.stream().map(bill -> baseService.convert(id, bill)).toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }

    @Override
    public void deleteBill(@NotNull Long id, @NotNull Long billId) {
        if (!checkPermission(id, billId)) {
            throw new PermissionDeniedException("用户无权限操作此账单");
        }
        billDao.deleteById(billId);
    }
}

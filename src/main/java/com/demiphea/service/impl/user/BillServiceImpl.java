package com.demiphea.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.BillDao;
import com.demiphea.entity.Bill;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.user.BillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    public PageResult listBills(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Bill> bills = billDao.selectList(new LambdaQueryWrapper<Bill>().eq(Bill::getUserId, id));
        PageInfo<Bill> pageInfo = new PageInfo<>(bills);
        List<BillVo> list = bills.stream().map(bill -> baseService.convert(id, bill)).toList();
        PageResult result = baseService.convert(pageInfo, list);
        page.close();
        return result;
    }
}

package com.demiphea.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demiphea.entity.Favorite;
import com.demiphea.model.po.FavoriteObject;

import java.util.List;

/**
 * 收藏夹表 Dao接口
 * @author demiphea
 * @since 17.0.9
 */
public interface FavoriteDao extends BaseMapper<Favorite> {
    List<FavoriteObject> listFavoriteContent(Long userId, Long favoriteId);
}

package com.demiphea.service.impl;

import com.demiphea.dao.BillDao;
import com.demiphea.dao.CollectionDao;
import com.demiphea.dao.FavoriteDao;
import com.demiphea.dao.NoteDao;
import com.demiphea.entity.Bill;
import com.demiphea.entity.Collection;
import com.demiphea.entity.Favorite;
import com.demiphea.entity.Note;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.service.inf.PermissionService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * PermissionServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final BillDao billDao;
    private final NoteDao noteDao;
    private final CollectionDao collectionDao;
    private final FavoriteDao favoriteDao;

    @Override
    public boolean checkBillAdminPermission(@Nullable Long userId, @NotNull Long billId) {
        if (userId == null) {
            return false;
        }
        Bill bill = billDao.selectById(billId);
        if (bill == null) {
            throw new ObjectDoesNotExistException("账单不存在或已删除");
        }
        return checkBillAdminPermission(userId, bill);
    }

    @Override
    public boolean checkBillAdminPermission(@Nullable Long userId, @NotNull Bill bill) {
        if (userId == null) {
            return false;
        }
        return userId.equals(bill.getUserId());
    }

    @Override
    public boolean checkNoteAdminPermission(@Nullable Long id, @NotNull Long noteId) {
        if (id == null) {
            return false;
        }
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        return checkNoteAdminPermission(id, note);
    }

    @Override
    public boolean checkNoteAdminPermission(@Nullable Long id, @NotNull Note note) {
        if (id == null) {
            return false;
        }
        return id.equals(note.getUserId());
    }

    @Override
    public boolean checkNoteReadPermission(@Nullable Long id, @NotNull Long noteId) {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        return checkNoteReadPermission(id, note);
    }

    @Override
    public boolean checkNoteReadPermission(@Nullable Long id, @NotNull Note note) {
        if (checkNoteAdminPermission(id, note)) {
            return true;
        }
        if (note.getOpenPublic() && (note.getPrice() == null || note.getPrice().compareTo(BigDecimal.ZERO) == 0)) {
            return true;
        }
        if (id == null) {
            return false;
        }
        return billDao.hasBuy(id, note.getId());
    }

    @Override
    public boolean checkCollectionAdminPermission(@Nullable Long id, @NotNull Long collectionId) {
        if (id == null) {
            return false;
        }
        Collection collection = collectionDao.selectById(collectionId);
        if (collection == null) {
            throw new ObjectDoesNotExistException("合集不存在或已删除");
        }
        return checkCollectionAdminPermission(id, collection);
    }

    @Override
    public boolean checkCollectionAdminPermission(@Nullable Long id, @NotNull Collection collection) {
        if (id == null) {
            return false;
        }
        return id.equals(collection.getUserId());
    }

    @Override
    public boolean checkFavoriteAdminPermission(@Nullable Long id, @NotNull Long favoriteId) {
        if (id == null) {
            return false;
        }
        Favorite favorite = favoriteDao.selectById(favoriteId);
        if (favorite == null) {
            throw new ObjectDoesNotExistException("收藏夹不存在或已删除");
        }
        return checkFavoriteAdminPermission(id, favorite);
    }

    @Override
    public boolean checkFavoriteAdminPermission(@Nullable Long id, @NotNull Favorite favorite) {
        if (id == null) {
            return false;
        }
        return id.equals(favorite.getUserId());
    }

    @Override
    public boolean checkFavoriteViewPermission(@Nullable Long id, @NotNull Long favoriteId) {
        Favorite favorite = favoriteDao.selectById(favoriteId);
        if (favorite == null) {
            throw new ObjectDoesNotExistException("收藏夹不存在或已删除");
        }
        return checkFavoriteViewPermission(id, favorite);
    }

    @Override
    public boolean checkFavoriteViewPermission(@Nullable Long id, @NotNull Favorite favorite) {
        if (favorite.getOptionPublic()) {
            return true;
        }
        return checkFavoriteAdminPermission(id, favorite);
    }
}

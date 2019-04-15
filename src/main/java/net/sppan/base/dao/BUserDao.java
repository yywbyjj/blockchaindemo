package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.BlockChainUser;
import org.springframework.stereotype.Repository;

@Repository
public interface BUserDao extends IBaseDao<BlockChainUser,String> {

    BlockChainUser findByUserId(String id);

    void deleteByUserId(String userId);
}

package net.sppan.base.service.impl;

import net.sppan.base.dao.BUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.BlockChainUser;
import net.sppan.base.service.BUserService;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BUserServiceImpl extends BaseServiceImpl<BlockChainUser,String> implements BUserService {

    @Autowired
    private BUserDao bUserDao;

    @Override
    public BlockChainUser findTradeIdByUserId(String id) {
        return bUserDao.findByUserId(id);
    }

    @Override
    public IBaseDao<BlockChainUser, String> getBaseDao() {
        return this.bUserDao;
    }
}

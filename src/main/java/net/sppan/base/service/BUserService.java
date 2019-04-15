package net.sppan.base.service;

import net.sppan.base.entity.BlockChainUser;
import net.sppan.base.service.support.IBaseService;

/**
 * @author yangkj
 * @since 2019/3/27
 */
public interface BUserService extends IBaseService<BlockChainUser,String> {

    /**
     * 根据id查找绑定的区块链id
     * @param id
     * @return
     */
    BlockChainUser findTradeIdByUserId(String id);
}

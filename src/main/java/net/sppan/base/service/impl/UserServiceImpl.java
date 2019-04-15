package net.sppan.base.service.impl;

import java.util.*;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import net.sppan.base.common.JsonResult;
import net.sppan.base.common.utils.HttpClientResult;
import net.sppan.base.common.utils.HttpClientUtils;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.common.utils.UuidUtils;
import net.sppan.base.dao.BUserDao;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.BlockChainUser;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.service.BUserService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.support.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author yangkj
 * @since 2019/3/27
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IRoleService roleService;

	@Autowired
	private BUserDao bUserDao;
	
	@Override
	public IBaseDao<User, String> getBaseDao() {
		return this.userDao;
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public void updateUser(User user) {
		User dbUser = find(user.getId());
		dbUser.setNickName(user.getNickName());
		dbUser.setSex(user.getSex());
		dbUser.setBirthday(user.getBirthday());
		dbUser.setTelephone(user.getTelephone());
		dbUser.setEmail(user.getEmail());
		dbUser.setAddress(user.getAddress());
		dbUser.setLocked(user.getLocked());
		dbUser.setDescription(user.getDescription());
		dbUser.setUpdateTime(new Date());
		update(dbUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUser(User user) throws Exception{
		if (user.getId()==null){
			BlockChainUser buser = new BlockChainUser();
			buser.setId(UuidUtils.generateUuid());
			buser.setTradeId(UuidUtils.generateUuid());
			buser.setUserId(UuidUtils.generateUuid());
			buser.setUserName(user.getUserName());
			buser.setNickName(user.getNickName());
			user.setId(buser.getUserId());
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setDeleteStatus(0);
			user.setPassword(MD5Utils.md5("111111"));
			bUserDao.save(buser);
			userDao.save(user);
			if (userDao.findOne(user.getId())==null||bUserDao.findOne(buser.getId())==null){
				throw new RuntimeException();
			}
			HttpClientResult result = saveBlockChainUser(buser);
			if (result.getCode()!=200){
				throw new RuntimeException();
			}
		}else {
			BlockChainUser buser = bUserDao.findByUserId(user.getId());
			buser.setUserName(user.getUserName());
			buser.setNickName(user.getNickName());
			bUserDao.save(buser);
			updateUser(user);
			updateBlockChainUser(buser);
		}
	}

	public HttpClientResult saveBlockChainUser(BlockChainUser buser) throws Exception{
		Map<String,String> params = new HashMap<>();
		params.put("$class","org.example.mynetwork.Trader");
		params.put("tradeId",buser.getTradeId());
		params.put("userName",buser.getUserName());
		params.put("nickName",buser.getNickName());
		HttpClientResult result = HttpClientUtils.doPost("http://193.112.47.47:3000/api/Trader",params);
		return result;
	}

	public HttpClientResult delBlockChainUser(String tradeId) throws Exception{
		HttpClientResult result = HttpClientUtils.doDelete("http://193.112.47.47:3000/api/Trader/"+tradeId);
		return result;
	}

	public void updateBlockChainUser(BlockChainUser buser) throws Exception{
		HttpClientResult result = HttpClientUtils.doGet("http://193.112.47.47:3000/api/Trader/"+buser.getTradeId());
		delBlockChainUser(buser.getTradeId());
		try {
			saveBlockChainUser(buser);
		} catch (Exception e){
			e.printStackTrace();
			Map map = (Map) JSON.parse(result.getContent());
			Map<String,String> params = new HashMap<>();
			params.put("$class","org.example.mynetwork.Trader");
			params.put("tradeId",map.get("tradeId").toString());
			params.put("userName",map.get("userName").toString());
			params.put("nickName",map.get("nickName").toString());
			HttpClientUtils.doPost("http://193.112.47.47:3000/api/Trader",params);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id) throws Exception{
		String tradeId = bUserDao.findByUserId(id).getTradeId();
		User user = find(id);
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能删除");
		super.delete(id);
		bUserDao.deleteByUserId(id);
		HttpClientResult result = delBlockChainUser(tradeId);
		if (result.getCode()!=204){
			throw new RuntimeException();
		}
	}

	@Override
	public void grant(String id, String[] roleIds) {
		User user = find(id);
		Assert.notNull(user, "用户不存在");
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能修改管理角色");
		Role role;
		Set<Role> roles = new HashSet<Role>();
		if(roleIds != null){
			for (int i = 0; i < roleIds.length; i++) {
				Integer rid = Integer.parseInt(roleIds[i]);
				role = roleService.find(rid);
				roles.add(role);
			}
		}
		user.setRoles(roles);
		update(user);
	}
	
}

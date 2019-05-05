package net.sppan.base.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import net.sppan.base.common.utils.HttpClientResult;
import net.sppan.base.common.utils.HttpClientUtils;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.common.utils.UuidUtils;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.enu.StateCode;
import net.sppan.base.service.*;
import net.sppan.base.service.support.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
			user.setId(UuidUtils.generateUuid());
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setDeleteStatus(0);
			user.setPassword(MD5Utils.md5("111111"));
			userDao.save(user);
			if (userDao.findOne(user.getId())==null){
				throw new NoUserException("没有查到改用户的信息");
			}
			HttpClientResult result = saveBlockChainUser(user);
			if (result.getCode()!= StateCode.SUCCESSCODE){
				throw new NoClientBlockChainException("连接区块链错误");
			}
		}else {
			updateUser(user);
			HttpClientResult result = updateBlockChainUser(user);
			if (result.getCode()!=StateCode.SUCCESSCODE){
				throw new NoClientBlockChainException("连接区块链错误");
			}
		}
	}

	public HttpClientResult saveBlockChainUser(User user) throws Exception{
		Map<String, String> params = getStringMap(user);
		HttpClientResult result = HttpClientUtils.doPost("http://193.112.47.47:3000/api/Trader",params);
		return result;
	}

	public HttpClientResult delBlockChainUser(String tradeId) throws Exception{
		HttpClientResult result = HttpClientUtils.doDelete("http://193.112.47.47:3000/api/Trader/"+tradeId);
		return result;
	}

	public HttpClientResult updateBlockChainUser(User user) throws Exception{
		String id = user.getId();
		Map<String, String> params = getStringMap(user);
		HttpClientResult result = HttpClientUtils.doPut("http://193.112.47.47:3000/api/Trader/"+id, params);
		return result;
	}

	private Map<String, String> getStringMap(User user) {
		Map<String, String> params = new HashMap<>();
		params.put("$class", "org.example.mynetwork.Trader");
		params.put("tradeId", user.getId());
		params.put("userName", user.getUserName());
		params.put("nickName", user.getNickName());
		return params;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id) throws Exception{
		User user = find(id);
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能删除");
		super.delete(id);
		HttpClientResult result = delBlockChainUser(id);
		if (result.getCode()!=StateCode.DELSUCCESSCODE){
			throw new NoClientBlockChainException("连接区块链错误");
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

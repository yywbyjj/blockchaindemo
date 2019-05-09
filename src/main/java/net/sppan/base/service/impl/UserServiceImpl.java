package net.sppan.base.service.impl;

import java.util.*;

import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.common.utils.UuidUtils;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
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
		dbUser.setIdNumber(user.getIdNumber());
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
				throw new NoUserException("新增用户的信息失败");
			}
		}else {
			updateUser(user);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id) throws Exception{
		User user = find(id);
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能删除");
		super.delete(id);
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

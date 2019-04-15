package net.sppan.base.service;

import net.sppan.base.entity.User;
import net.sppan.base.service.support.IBaseService;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author yangkj
 * @since 2019-3-28
 */
public interface IUserService extends IBaseService<User, String> {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	User findByUserName(String username);

	/**
	 * 新增用户
	 * @param user
	 */
	void saveUser(User user) throws Exception;

	/**
	 * 给用户分配角色
	 * @param id 用户ID
	 * @param roleIds 角色Ids
	 */
	void grant(String id, String[] roleIds);

	/**
	 * 修改用户信息
	 */
	void updateUser(User user);
}

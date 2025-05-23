package com.fly.auth.service.impl;

import cn.hutool.core.convert.Convert;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.TokenException;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.user.FlyUserDetailsService;
import com.fly.system.api.domain.common.UserInfo;
import com.fly.system.api.feign.ISysUserApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.fly.system.api.domain.SysUser;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 用户详情实现类
 *
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements FlyUserDetailsService {


	@Resource
	private ISysUserApi sysUserProvider;


	/**
	 * 根据用户名查询用户信息
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserInfo userInfo = sysUserProvider.getUserByUserName(userName).getCheckedData();
		if (userInfo == null) {
			throw new TokenException("该用户：" + userName + "不存在");
		}
		userInfo.setLoginType(Oauth2Constants.LOGIN_USERNAME_TYPE);
		userInfo.setUserName(userName);
		
		return this.handleUserDetails(userInfo);
	}


	/**
	 * 根据用户手机号查询用户信息
	 */
	@Override
	public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {

		UserInfo userInfo = sysUserProvider.getUserByMobile(mobile).getData();
		if (userInfo == null) {
			throw new TokenException("该用户：" + mobile + "不存在");
		}
		userInfo.setLoginType(Oauth2Constants.LOGIN_MOBILE_TYPE);
		userInfo.setUserName(mobile);

		return this.handleUserDetails(userInfo);
	}


	/**
	 * 根据社交账号登录
	 */
	@Override
	public UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException {

		String userName = "admin";
		UserInfo userInfo = sysUserProvider.getUserByUserName(userName).getData();
		if (userInfo == null) {
			throw new TokenException("该用户：" + userName + "不存在");
		}
		userInfo.setLoginType(Oauth2Constants.LOGIN_USERNAME_TYPE);
		userInfo.setUserName(userName);

		return this.handleUserDetails(userInfo);
	}


	// =============================================================================


	/**
	 * 处理用户信息
	 */
	private UserDetails handleUserDetails(UserInfo userInfo) {

		if (ObjectUtils.isEmpty(userInfo)) {
			log.info("该用户：{} 不存在！", userInfo.getUserName());
			throw new TokenException("该用户：" + userInfo.getUserName() + "不存在");
		} else if (StatusEnum.DISABLE.getStatus() == userInfo.getSysUser().getStatus()) {
			log.info("该用户：{} 已被停用!", userInfo.getUserName());
			throw new TokenException("对不起，您的账号：" + userInfo.getUserName() + " 已停用");
		}
		
		SysUser user = userInfo.getSysUser();
		log.info("账号：{}", userInfo.getSysUser().getAccount());
		
		Collection<? extends GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(Convert.toStrArray(userInfo.getPermissionList()));
		log.info("权限: {}", authorityList);


		// todo !!! 把spring security的User字段信息设置上，用于自身密码的自动判断和角色权限判断，拓展的字段用于业务实现 !!!
		return new FlyUser(
				user.getId()
				, userInfo.getSysUser().getUserType()
				, userInfo.getLoginType()
				, user.getDeptId().toString()
				, user.getTelephone()
				, user.getAvatar()
				, userInfo.getRoleIds()
				, userInfo.getUserName()
				, user.getPassword()
				, StatusEnum.ENABLE.getStatus() == user.getStatus(),
				true
				, true
				, true
				, authorityList
		);
	}

}

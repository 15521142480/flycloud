package com.fly.auth.social;

import com.fly.common.security.user.FlyUserDetailsService;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

/**
 * 社交登录验证提供者
 *
 */
@AllArgsConstructor
public class SocialAuthenticationProvider implements AuthenticationProvider {


	private final FlyUserDetailsService userDetailsService;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;

		/**
		 * 调用 {@link UserDetailsService}
		 */
		UserDetails user = userDetailsService.loadUserBySocial(((AuthUser) authenticationToken.getPrincipal()).getUsername());

		if (Objects.isNull(user)) {
			throw new InternalAuthenticationServiceException("社交登录错误");
		}

		SocialAuthenticationToken authenticationResult = new SocialAuthenticationToken(user, user.getAuthorities());

		authenticationResult.setDetails(authenticationToken.getDetails());

		return authenticationResult;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SocialAuthenticationToken.class.isAssignableFrom(authentication);
	}

}

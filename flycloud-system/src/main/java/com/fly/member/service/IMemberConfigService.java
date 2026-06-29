package com.fly.member.service;

import com.fly.system.api.member.domain.bo.MemberConfigBo;
import com.fly.system.api.member.domain.vo.MemberConfigVo;

/**
 * 会员配置 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberConfigService {
    Boolean saveConfig(MemberConfigBo bo);
    MemberConfigVo getConfig();
}

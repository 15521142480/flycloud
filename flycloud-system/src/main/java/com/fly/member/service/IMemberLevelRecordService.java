package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.MemberLevelRecord;
import com.fly.system.api.member.domain.bo.MemberLevelRecordBo;
import com.fly.system.api.member.domain.vo.MemberLevelRecordVo;

/**
 * 会员等级记录 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IMemberLevelRecordService {
    PageVo<MemberLevelRecordVo> queryPageList(MemberLevelRecordBo bo, PageBo pageBo);
    MemberLevelRecordVo queryById(Long id);
    void createLevelRecord(MemberLevelRecord levelRecord);
}

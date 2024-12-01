package com.fly.bpm.flowable.convert;

import com.fly.system.api.domain.bo.send.SmsSendSingleToUserReqBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface BpmMessageConvert {

    BpmMessageConvert INSTANCE = Mappers.getMapper(BpmMessageConvert.class);

    @Mapping(target = "mobile", ignore = true)
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "templateCode", target = "templateCode")
    @Mapping(source = "templateParams", target = "templateParams")
    SmsSendSingleToUserReqBo convert(Long userId, String templateCode, Map<String, Object> templateParams);

}

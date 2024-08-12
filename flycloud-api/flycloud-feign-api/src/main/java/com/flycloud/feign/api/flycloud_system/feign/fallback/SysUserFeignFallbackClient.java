//package com.flycloud.feign.api.flycloud_system.feign.fallback;
//
//import com.fly.common.model.ApiCode;
//import com.fly.common.model.R;
//import com.flycloud.feign.api.flycloud_system.feign.SysUserFeignClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
///**
// * 系统用户调用回调
// *
// * @author lxs
// * @date 2023/5/2
// */
//@Component
//@Slf4j
//public class SysUserFeignFallbackClient implements SysUserFeignClient {
//
//
//    @Override
//    public R getUserInfoByName(String username) {
//        log.error("feign远程调用系统用户服务异常后的降级方法!");
//        return R.failed(ApiCode.DEGRADATION);
//    }
//}

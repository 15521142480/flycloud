package com.fly.api.flycloud_system.feign;

import com.fly.common.constant.ServerNames;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 系统
 *
 * @author lxs
 * @date 2023/2/13
 */
@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME)
public interface SystemFeignClient {



/*    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ordgetOrdersAmountAndIfNoCanceler")
    ServerResponseEntity<OrderAmountVO> getOrdersAmountAndIfNoCancel(@RequestParam("orderIds") List<Long> orderIds);


    @PostMapping(value = "api/storage/upload", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<StorageDTO> upload(@RequestPart("file") MultipartFile file) throws IOException;

    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }*/



    @GetMapping(value = "/feign/system/getUserInfo")
    String getUserInfo(@RequestParam("id") int id);




}

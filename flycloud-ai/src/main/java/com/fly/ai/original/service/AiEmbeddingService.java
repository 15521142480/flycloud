package com.fly.ai.original.service;

import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 文本向量化服务。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Service
@RequiredArgsConstructor
public class AiEmbeddingService {

    private final AiModelProviderRouter aiModelProviderRouter;

    /**
     * 根据当前供应商配置执行文本向量化。
     *
     * @param request 向量化请求
     * @return 文本向量响应
     */
    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        return aiModelProviderRouter.embed(request);
    }

}

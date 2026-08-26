package com.fly.ai.service;

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

    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        return aiModelProviderRouter.embed(request);
    }

}

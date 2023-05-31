package com.fly.test.controller;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.fly.common.model.R;
import com.fly.test.domain.Document;
import com.fly.test.esmapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索引擎 crud 演示案例
 *
 * @author lxs
 * @date 2023/5/20
 */
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
@RestController
@RequiredArgsConstructor
public class EsCrudController {


    private final DocumentMapper documentMapper;


    /**
     * 查询(指定)
     *
     * @param title 标题
     */
    @GetMapping("/select")
    public R<Document> select(String title) {

        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(Document::getTitle, title);
        return R.ok(documentMapper.selectOne(wrapper));
    }


    /**
     * 搜索(模糊)
     *
     * @param key 搜索关键字
     */
    @GetMapping("/search")
    public R<List<Document>> search(String key) {

        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.like(Document::getTitle, key);
        return R.ok(documentMapper.selectList(wrapper));
    }


    /**
     * 插入
     *
     */
    @PostMapping("/insert")
    public R<Void> insert(@RequestBody Document document) {

        return R.ok(documentMapper.insert(document));
    }


    /**
     * 更新
     *
     */
    @PutMapping("/update")
    public R<Void> update(@RequestBody Document document) {

        // 测试更新 更新有两种情况 分别演示如下:
        // case1: 已知id, 根据id更新 (为了演示方便,此id是从上一步查询中复制过来的,实际业务可以自行查询)
        documentMapper.updateById(document);

        // case2: id未知, 根据条件更新
//        LambdaEsUpdateWrapper<Document> wrapper = new LambdaEsUpdateWrapper<>();
//        wrapper.like(Document::getTitle, document.getTitle());
//        Document document2 = new Document();
//        document2.setTitle(document.getTitle());
//        document2.setContent(document.getContent());
//        documentMapper.update(document2, wrapper);

        return R.ok();
    }


    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable String id) {

        // 测试删除数据 删除有两种情况:根据id删或根据条件删
        return R.ok(documentMapper.deleteById(id));
    }



}

package com.fly.common.report.jmreport.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.drag.service.IOnlDragExternalService;
import org.jeecg.modules.drag.vo.DragDictModel;
import org.jeecg.modules.drag.vo.DragLogDTO;

import java.util.List;
import java.util.Map;

/**
 * 积木仪表盘扩展服务。
 *
 * @author lxs
 * @date 2026-07-12
 */
public class JmOnlDragExternalServiceImpl implements IOnlDragExternalService {

    @Override
    public Map<String, List<DragDictModel>> getManyDictItems(List<String> codeList, List<JSONObject> tableDictList) {
        return IOnlDragExternalService.super.getManyDictItems(codeList, tableDictList);
    }

    @Override
    public List<DragDictModel> getDictItems(String dictCode) {
        return IOnlDragExternalService.super.getDictItems(dictCode);
    }

    @Override
    public List<DragDictModel> getTableDictItems(String dictTable, String dictText, String dictCode) {
        return IOnlDragExternalService.super.getTableDictItems(dictTable, dictText, dictCode);
    }

    @Override
    public List<DragDictModel> getCategoryTreeDictItems(List<String> ids) {
        return IOnlDragExternalService.super.getCategoryTreeDictItems(ids);
    }

    @Override
    public List<DragDictModel> getUserDictItems(List<String> ids) {
        return IOnlDragExternalService.super.getUserDictItems(ids);
    }

    @Override
    public List<DragDictModel> getDeptsDictItems(List<String> ids) {
        return IOnlDragExternalService.super.getDeptsDictItems(ids);
    }

    @Override
    public void addLog(DragLogDTO dto) {
        IOnlDragExternalService.super.addLog(dto);
    }

    @Override
    public void addLog(String logMsg, int logType, int operateType) {
        IOnlDragExternalService.super.addLog(logMsg, logType, operateType);
    }

}

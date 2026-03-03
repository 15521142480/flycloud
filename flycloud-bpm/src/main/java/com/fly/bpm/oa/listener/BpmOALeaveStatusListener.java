//package com.fly.bpm.oa.listener;
//
//import com.fly.bpm.flowable.event.BpmProcessInstanceStatusEvent;
//import com.fly.bpm.flowable.listener.BpmProcessInstanceStatusEventListener;
//import com.fly.bpm.oa.service.IBpmOaLeaveService;
//import com.fly.bpm.oa.service.impl.BpmOaLeaveServiceImpl;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * OA 请假单的结果的监听器实现类
// *
// * @author: lxs
// * @date: 2026/3/3
// */
//@Component
//public class BpmOALeaveStatusListener extends BpmProcessInstanceStatusEventListener {
//
//    @Resource
//    private IBpmOaLeaveService leaveService;
//
//    @Override
//    protected String getProcessDefinitionKey() {
//        return BpmOaLeaveServiceImpl.PROCESS_KEY;
//    }
//
//    @Override
//    protected void onEvent(BpmProcessInstanceStatusEvent event) {
//        leaveService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
//    }
//
//}

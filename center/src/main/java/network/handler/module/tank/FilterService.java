package network.handler.module.tank;
//package com.game.module;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.GameError;
//import com.game.dataMgr.StaticFunctionPlanDataMgr;
//import com.game.message.handler.ClientHandler;
//
///**
// * @author ChenKui
// * @version 创建时间：2016-3-25 下午2:44:48
// * @declare
// */
//@Service
//public class FilterService {
//
//	@Autowired
//	private StaticFunctionPlanDataMgr planDataMgr;
//
//	/**
//	 * 
//	 * @param funId查看FuncType类
//	 * @param handler
//	 * @return
//	 */
//	public boolean filter(int funId, ClientHandler handler) {
//		boolean flag = planDataMgr.filter(funId);
//		if (!flag) {// 提示功能暂未开放
//			handler.sendErrorMsgToPlayer(GameError.FUNCTION_NO_OPEN);
//		}
//		return flag;
//	}
//}

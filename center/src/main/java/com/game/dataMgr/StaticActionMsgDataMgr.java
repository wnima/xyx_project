package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONArray;

import config.bean.ConfEndConditionItem;
import config.bean.ConfActionMsg;

/**
 * 行为推送消息表
 * 
 * 
 */
public class StaticActionMsgDataMgr extends BaseDataMgr {

	private List<ConfActionMsg> sActionMsgList;

	/**
	 * actionMsg: 行为类型:前置条件id,后置条件list
	 */
	private Map<Integer, Map<Integer, List<ConfEndConditionItem>>> actionMsgMap = new HashMap<Integer, Map<Integer, List<ConfEndConditionItem>>>();

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		sActionMsgList = staticDataDao.selectStaticActionMsg();
//		for (StaticActionMsg msg : sActionMsgList) {
//			Map<Integer, List<EndConditionItem>> map = actionMsgMap.get(msg.getType());
//			if (map == null) {
//				map = new HashMap<Integer, List<EndConditionItem>>();
//				actionMsgMap.put(msg.getType(), map);
//			}
//
//			String[] preConditions = msg.getPreCondition().split(",");
//			List<EndConditionItem> list = desrEndCondition(msg.getEndCondition(),msg.getChatId());
//
//			for (String str : preConditions) {
//				map.put(Integer.parseInt(str), list);
//			}
//		}
	}

	/**
	 * Method: desrEndCondition
	 * 
	 * @Description: TODO
	 * @param endCondition
	 * @return
	 * @return List<EndConditionItem>
	 * @throws
	 */
	private List<ConfEndConditionItem> desrEndCondition(String endCondition,int chatId) {
		List<ConfEndConditionItem> list = new ArrayList<ConfEndConditionItem>();
		if (endCondition == null || endCondition.isEmpty()) {
			return list;
		}
		try {
			JSONArray arrays = JSONArray.parseArray(endCondition);
			for (int i = 0; i < arrays.size(); i++) {
				ConfEndConditionItem  item = new ConfEndConditionItem();
				JSONArray array = arrays.getJSONArray(i);
				item.setItemType(array.getIntValue(0));
				item.setItemId(array.getIntValue(1));
				item.setQuality(array.getIntValue(2));
				item.setStar(array.getIntValue(3));
				item.setChatId(chatId);
				list.add(item);
			}
		} catch (Exception e) {
			System.out.println("ListEndConditionTypeHandler parse:" + endCondition);
			throw e;
		}

		return list;
	}
	

	/**
	 * 通过类型获取前置条件map
	* Method: getMsgMap    
	* @Description: TODO    
	* @param actionType
	* @return    
	* @return Map<Integer,List<EndConditionItem>>    
	* @throws
	 */
	public Map<Integer, List<ConfEndConditionItem>> getMsgMap(int actionType){
		return actionMsgMap.get(actionType);
	}

	public static void main(String[] args) {
		String aaa = "[[10,1,2,4],[3,4,2,1]]";
		
//		List<EndConditionItem> list = desrEndCondition(aaa);
//		System.out.println(list.size());
	}
}

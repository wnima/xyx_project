package service.handler.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import packet.CocoPacket;
import protocol.c2s.RequestCode;
import service.GateApp;
import util.NettyUtil;
import util.PBUtilHelper;

public class AgentManager {

	private static Logger logger = LoggerFactory.getLogger(AgentManager.class);

	private static AgentManager instance = new AgentManager();

	private Map<Long, CocoAgent> agentMap = new ConcurrentHashMap<>();

	private Map<String, CocoAgent> sessionMap = new ConcurrentHashMap<>();

	private AgentManager() {
	}

	public static AgentManager getInst() {
		return instance;
	}

	private void synGateFactor() {
		int factor = agentMap.size();
		GateApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.CENTER_GATE_FACTOR, PBUtilHelper.pbInt(factor), 1));
	}

	public void registerAgent(CocoAgent agent) {
		if (agent.getPlayerId() != 0) {
			agentMap.put(agent.getPlayerId(), agent);
		}
		sessionMap.put(agent.getSessionId(), agent);
		synGateFactor();
	}

	public void removeAgent(CocoAgent agent) {
		agentMap.remove(agent.getPlayerId());
		sessionMap.remove(agent.getSessionId());
		synGateFactor();
	}

	public void removeSession(String sessionId, long playerId) {
		CocoAgent sessionAgent = sessionMap.remove(sessionId);
		CocoAgent playerAgent = agentMap.get(playerId);
		if (playerAgent == null) {
			logger.info("player agent is nul when remove session ");
			return;
		}
		agentMap.remove(playerId, sessionAgent);
	}

	public void kickAgent(long id) {
		CocoAgent agent = agentMap.remove(id);
		if (agent == null) {
			logger.debug("can't find the agent and the player id is {}", id);
			return;
		}
		// 避免此连接断开给center发送logout
		NettyUtil.setAttribute(agent.getCtx(), "agent", null);
		agent.closeAgent();
		synGateFactor();
	}

	public CocoAgent getCocoAgent(long playerId) {
		return agentMap.get(playerId);
	}

	public CocoAgent getCocoAgent(String sessionId) {
		return sessionMap.get(sessionId);
	}

	public CocoAgent getCocoAgentByOpenId(String openId) {
		Optional<CocoAgent> opt = sessionMap.values().stream().filter(e -> e.getOpenid() != null && e.getOpenid().equals(openId)).findFirst();
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	public void closeAgent(int playerId) {
		CocoAgent agent = agentMap.get(playerId);
		if (agent == null) {
			logger.debug("can't find the agent and the player id is {}", playerId);
			return;
		}
		agent.closeAgent();
	}

	public void closeAll() {
		getAllAgents().forEach(e -> {
			if (e != null) {
				e.closeAgent();
			}
		});
	}

	public List<CocoAgent> getAllAgents() {
		return new ArrayList<>(agentMap.values());
	}

	public void writeMessage(CocoPacket packet) {
		CocoAgent agent = agentMap.get(packet.getPlayerId());
		if (agent == null) {
			logger.debug("writeMessage can't find the agent and the player playerId:{}", packet.getPlayerId());
			return;
		}
		if (!agent.isValid()) {
			logger.debug("writeMessage agent not valid playerId:{} agentId:{} ip:{}", packet.getPlayerId(), agent.getSessionId(),agent.remoteIp());
			return;
		}
		logger.info("send msg to playerId:{} cmd:{} agentId:{} ip:{} currentTime:{}", packet.getPlayerId(), packet.getReqId(), agent.getSessionId(), agent.remoteIp(),System.currentTimeMillis());
		agent.writeMessage(packet.getReqId(), packet.getBytes());
	}

	public void onUpdate() {
		CocoPacket packet = new CocoPacket(RequestCode.CENTER_GATE_FACTOR, PBUtilHelper.pbPairInt(1, getAllAgents().size()));
		GateApp.getInst().getClient().sendRequest(packet);
//		logger.info("gate在线人数:{} ", getAllAgents().size());
	}
}

package service.handler.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import packet.CocoPacket;
import protocol.c2s.RequestCode;
import service.RobotApp;
import util.PBHelper;

public class AgentManager {
	private static AgentManager instance = new AgentManager();

	private static Logger logger = LoggerFactory.getLogger(AgentManager.class);

	private AgentManager() {
	}

	public static AgentManager getInst() {
		return instance;
	}

	private Map<Long, CocoAgent> agentMap = new ConcurrentHashMap<>();

	public void registerAgent(CocoAgent agent) {
		CocoAgent originalAgent = agentMap.get(agent.getPlayerId());
		if (originalAgent != null) {
			removeAgent(agent.getPlayerId(), originalAgent.getSessionId());
			originalAgent.closeAgent();
		}
		agentMap.put(agent.getPlayerId(), agent);
		synGateFactor();
	}

	private void synGateFactor() {
		int factor = agentMap.size();
		RobotApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.CENTER_GATE_FACTOR, PBHelper.pbInt(factor), 1));
	}

	public boolean removeAgent(long id, String sessionId) {
		CocoAgent agent = agentMap.get(id);
		if (agent == null) {
			logger.debug(" can't find the agent and the player id is {}", id);
			return false;
		}
		if (agent.getSessionId().equals(sessionId)) {
			return agentMap.remove(id) != null;
		}
		logger.info(" remove session {} current session {}", sessionId, agent.getSessionId());
		return false;
	}

	public CocoAgent getCocoAgent(long playerId) {
		return agentMap.get(playerId);
	}

	public void closeAgent(int playerId) {
		CocoAgent agent = agentMap.get(playerId);
		if (agent == null) {
			logger.debug(" can't find the agent and the player id is {}", playerId);
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
			logger.debug(" can't find the agent and the player id is {}", packet.getPlayerId());
			return;
		}
		agent.writeMessage(packet.getReqId(), packet.getBytes());
	}
}

package network.handler.http;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Path;

@Path("/player")
public class PlayerHandler {

	private Lock lock = new ReentrantLock();

	private static final Logger logger = LoggerFactory.getLogger(PlayerHandler.class);

	
}

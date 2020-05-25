package network;

import com.google.protobuf.MessageLite;

import protocol.s2c.ResponseCode;

public interface MessageBean {
	
	void write(ResponseCode code, MessageLite lite);

	void write(int code, MessageLite lite);

}

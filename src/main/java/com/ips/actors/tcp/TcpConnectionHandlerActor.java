package com.ips.actors.tcp;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.actors.router.RouterActor;
import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.util.ByteString;


public class TcpConnectionHandlerActor extends AbstractActor {
private ActorRef sender;
private ActorRef router;
private final static Logger log = LogManager.getLogger(TcpConnectionHandlerActor.class); 
	public TcpConnectionHandlerActor() {
	}

	public static Props props() {
		return Props.create(TcpConnectionHandlerActor.class);
	}
	
	@Override
	public void preStart() {
	    log.trace("started handler");
		router = getContext().actorOf(RouterActor.props());
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Received.class, msg->{
				    sender = getSender();
					String messageX = msg.data().utf8String();
					 log.trace("received-> "+messageX);
					 router.tell(messageX, getSelf());
					
				}).match(String.class, s->{
                    log.info("sent-> "+s);
                    sender.tell(TcpMessage.write(ByteString.fromString(s)), getSelf());
                })
				.match(Protocol37Wrapper.class, p->{
				    log.info("sent-> "+p.getMessage());
				    sender.tell(TcpMessage.write(ByteString.fromString(p.getMessage())), getSelf());
				})
				.match(ConnectionClosed.class, closed->{
				    log.trace("connection closed");
					getContext().stop(getSelf());
				})
				.match(CommandFailed.class, conn->{
					getContext().stop(getSelf());
				})
				.build();
	}
	
	@Override
	public void postStop() {
	    log.trace("stopped handler");
	}
	
}
package com.ips.actors.tcp;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.*;
import akka.actor.*;
import akka.io.TcpMessage;
import akka.io.Tcp.*;

public class TcpServerActor  extends AbstractActor {
	private final ActorRef manager;
	private final static Logger log = LogManager.getLogger(TcpServerActor.class); 
	private static  int connectionCount = 0; 
	 private TcpServerActor(ActorRef manager,InetSocketAddress serverAddress) {
	        this.manager = manager;
	        	manager.tell(TcpMessage.bind(getSelf(),serverAddress,100), getSelf());
	        	
	    }

	  
	  
	  public static Props props(ActorRef tcpMnager, InetSocketAddress serverAddress) {
	    return Props.create(TcpServerActor.class, tcpMnager, serverAddress);
	  }


	  @Override
	  public Receive createReceive() {
	    return receiveBuilder()
	    	.match(Bound.class, msg -> {
	    	    log.trace(msg.toString());
	      })
	      .match(CommandFailed.class, msg -> {
	    	  getContext().stop(getSelf());
	      
	      })
	      .match(Connected.class, conn -> {
	    	  connectionCount ++;
	    	  log.trace(conn.toString());
	          
	    	  final ActorRef handler = getContext().actorOf(TcpConnectionHandlerActor.props(connectionCount),"TCP-handler-"+ connectionCount);
	                /**
	                 * !!NB:
	                 * telling the aforesaid akka internal connection actor that the actor "handler"
	                 * is the one that shall receive its (the internal actor) messages.
	                 */
	                sender().tell(TcpMessage.register(handler), self());
	      })
	      .build();
	  }
	
}
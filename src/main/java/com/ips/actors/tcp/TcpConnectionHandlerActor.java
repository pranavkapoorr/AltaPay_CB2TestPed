package com.ips.actors.tcp;


import org.apache.logging.log4j.*;
import com.ips.actors.router.RouterActor;
import com.ips.resources.Protocol37Wrapper;
import akka.actor.*;
import akka.io.Tcp.*;
import akka.io.TcpMessage;
import akka.util.ByteString;


public class TcpConnectionHandlerActor extends AbstractActor {
private ActorRef sender;
private ActorRef router;
private int counter = 0;
private final static Logger log = LogManager.getLogger(TcpConnectionHandlerActor.class); 
	public TcpConnectionHandlerActor(int counter) {
		this.counter = counter;
	}

	public static Props props(int counter) {
		return Props.create(TcpConnectionHandlerActor.class, counter);
	}
	
	@Override
	public void preStart() {
	    log.trace(getSelf().path()+" started handler");
		router = getContext().actorOf(RouterActor.props(counter),"router-"+counter);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Received.class, msg->{
				    sender = getSender();
					String messageX = msg.data().utf8String();
					 log.trace(getSelf().path().name()+" received-> "+messageX);
					 router.tell(messageX, getSelf());
					
				}).match(String.class, s->{
                    log.info(getSelf().path().name()+" sent-> "+s);
                    sender.tell(TcpMessage.write(ByteString.fromString(s)), getSelf());
                })
				.match(Protocol37Wrapper.class, p->{
				    log.info(getSelf().path().name()+" sent-> "+p.getMessage());
				    sender.tell(TcpMessage.write(ByteString.fromString(p.getMessage())), getSelf());
				})
				.match(ConnectionClosed.class, closed->{
				    log.trace(getSelf().path().name()+" connection closed");
					getContext().stop(getSelf());
				})
				.match(CommandFailed.class, conn->{
					log.trace(getSelf().path().name()+" connection failed");
					getContext().stop(getSelf());
				})
				.build();
	}
	
	@Override
	public void postStop() {
	    log.trace(getSelf().path().name()+" stopped handler");
	}
	
}
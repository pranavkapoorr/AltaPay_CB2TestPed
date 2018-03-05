package com.ips.actors;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public static boolean printOption;
private ActorRef sender;
private ActorRef commmunication;
private final static Logger log = LogManager.getLogger(TcpConnectionHandlerActor.class); 
	public TcpConnectionHandlerActor() {
	}

	public static Props props() {
		return Props.create(TcpConnectionHandlerActor.class);
	}
	
	@Override
	public void preStart() {
	    log.trace("started handler");
		printOption = false;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Received.class, msg->{
				    sender = getSender();
					String messageX = msg.data().utf8String();
					 log.trace("received-> "+messageX);
					if(messageX.charAt(0)==(char)02 && messageX.charAt(messageX.length()-2)==(char)03){
					  
    					sender.tell(TcpMessage.write(ByteString.fromString(ACK())), sender);
    					
					    messageX = messageX.substring(1,messageX.length()-2);
    					if(messageX.contains("0E1")){
                            printOption = true;
                        }
    					else if(messageX.contains("0P0")){
    					    commmunication = getContext().actorOf(PaymentActor.props());
    					    commmunication.tell(messageX, getSelf());
    					}
    					else if(messageX.contains("0A0")){
                            commmunication = getContext().actorOf(RefundActor.props());
                            commmunication.tell(messageX, getSelf());
                        }
    					else if(messageX.contains("0S0")){
                            commmunication = getContext().actorOf(ReversalActor.props());
                            commmunication.tell(messageX, getSelf());
                        }
    					else if(messageX.contains("0U0")){
                            commmunication.tell(messageX, getSelf());
                        }
    					else if(messageX.contains("0D0")){
                            commmunication = getContext().actorOf(DllActor.props());
                            commmunication.tell(messageX, getSelf());
                        }
    					else if(messageX.contains("0T0")||messageX.contains("0C0")){
                            commmunication = getContext().actorOf(ReportActor.props());
                            commmunication.tell(messageX, getSelf());
                        }
    					else if(messageX.contains("00s")||messageX.contains("00R100")){
                            commmunication = getContext().actorOf(ReprintAndPedConfigActor.props());
                            commmunication.tell(messageX, getSelf());
                        }
    					
    				}
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
	public static String ACK(){
        char[] msg={(char)06,(char)03,'z'};
        return new String(msg);
}
}
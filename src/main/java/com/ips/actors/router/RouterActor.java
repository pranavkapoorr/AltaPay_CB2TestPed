package com.ips.actors.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.actors.functions.DllActor;
import com.ips.actors.functions.PaymentActor;
import com.ips.actors.functions.RefundActor;
import com.ips.actors.functions.ReportActor;
import com.ips.actors.functions.ReprintAndPedConfigActor;
import com.ips.actors.functions.ReversalActor;
import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;

public class RouterActor extends AbstractActor{
    private ActorRef commmunication;
    public static boolean printOption;
    private ActorRef sender;
    private final static Logger log = LogManager.getLogger(RouterActor.class); 
    public static Props props(){
        return Props.create(RouterActor.class);
    }
    
    @Override
    public void preStart() throws Exception {
        log.trace("ROUTER STARTED");
        printOption = false;
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, messageX->{
                    sender = getSender();
                    if(messageX.charAt(0)==(char)02 && messageX.charAt(messageX.length()-2)==(char)03){
                        
                        sender.tell(ACK(), getSelf());
                        
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
                        
                        
                    }else if(messageX.equals(ACK())){
                        log.info("ack");   
                     }
                    else{
                        sender.tell(NACK(), getSelf());
                    }
                })
                .match(Protocol37Wrapper.class, p->context().parent().tell(p, getSelf()))
                .build();
    }
    private static String ACK(){
        char[] msg={(char)06,(char)03,'z'};
        return new String(msg);
    }
    private static String NACK(){
        char[] msg={(char)21,(char)03,'z'};
        return new String(msg);
    }
    @Override
    public void postStop() throws Exception {
       log.trace("ROUTER STOPPED");
    }

}

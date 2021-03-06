package com.ips.actors.router;

import org.apache.logging.log4j.*;
import com.ips.actors.functions.*;
import com.ips.resources.Protocol37Wrapper;
import akka.actor.*;

public class RouterActor extends AbstractActor{
    private ActorRef commmunication;
    private static volatile boolean printOption;
    private ActorRef sender;
    private int counter = 0;
    private final static Logger log = LogManager.getLogger(RouterActor.class);
    public RouterActor(int counter) {
		this.counter = counter;
	}
    public static Props props(int counter){
        return Props.create(RouterActor.class,counter);
    }
    
    @Override
    public void preStart() throws Exception {
        log.trace(getSelf().path().name()+" ROUTER STARTED");
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
                            log.trace(getSelf().path().name()+" setting print option true");
                        }
                        else if(messageX.contains("0P0")){
                            commmunication = getContext().actorOf(PaymentActor.props(printOption),"payment-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("0A0")){
                            commmunication = getContext().actorOf(RefundActor.props(printOption),"refund-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("0S0")){
                            commmunication = getContext().actorOf(ReversalActor.props(printOption),"reversal-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("0U0")){
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("0D0")){
                            commmunication = getContext().actorOf(DllActor.props(printOption),"dll-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("0T0")||messageX.contains("0C0")){
                            commmunication = getContext().actorOf(ReportActor.props(printOption),"report-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        else if(messageX.contains("00s")||messageX.contains("00R100")){
                            commmunication = getContext().actorOf(ReprintAndPedConfigActor.props(),"reprint-"+counter);
                            commmunication.tell(messageX, getSelf());
                        }
                        
                        
                    }else if(messageX.equals(ACK())){
                        //log.info(getSelf().path().name()+" ack");   
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
       log.trace(getSelf().path().name()+" ROUTER STOPPED");
    }

}

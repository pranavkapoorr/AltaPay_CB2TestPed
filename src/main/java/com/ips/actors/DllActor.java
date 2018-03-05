package com.ips.actors;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;

public class DllActor  extends AbstractActor{
   private String terminalId = "00000000";
   private final static Logger log = LogManager.getLogger(DllActor.class); 
   
   public static Props props(){
        return Props.create(DllActor.class);
    }
   @Override
    public void preStart() throws Exception {
        log.trace("DLL ACTOR STARTED");
    } 
   
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.contains("0D00")){
                        terminalId = s.substring(0,8);
                        getSender().tell(new Protocol37Wrapper("OPERATION IN PROGRESS   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(10);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0D0000000200002306110300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",false), getSelf());
                        if(TcpConnectionHandlerActor.printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S     DLL PARAMETERS           Point Elavon           Elavon - Demo                              Date 02/03/18 Time 10:30TML 00000071 STAN 000002CAUSA",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0SL  861 N.OP. 000022Conferment   PAR.TEC  OK                             CARDS HANDLED      Elavon                                            OPERATION",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S SUCCEEDED   }}",false), getSelf());
                        }
                        getContext().getParent().tell(PoisonPill.getInstance(), getSelf());
                    }
                    
                })
                .build();
    }
    @Override
        public void postStop() throws Exception {
           log.trace("DLL ACTOR STOPPED");
        }

}

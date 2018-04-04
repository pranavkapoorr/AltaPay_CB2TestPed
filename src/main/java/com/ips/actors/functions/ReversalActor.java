package com.ips.actors.functions;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.actors.router.RouterActor;
import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;

public class ReversalActor extends AbstractActor{
    private String terminalId = "00000000";
    private final static Logger log = LogManager.getLogger(ReversalActor.class); 
    private boolean printOption;
    public ReversalActor(boolean printOption) {
       this.printOption = printOption;
    }
    public static Props props(boolean printOption){
        return Props.create(ReversalActor.class, printOption);
    }
    @Override
    public void preStart() throws Exception {
        log.trace(getSelf().path().name()+" REVERSAL ACTOR STARTED");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.contains("0S00")){
                        log.info(getSelf().path().name()+s);
                        //terminalId = s.substring(0,8);
                    }else{
                        getSender().tell(new Protocol37Wrapper("INSERT CARD   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("REMOVE CARD  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0E00000476073******0004ICC00000006110452000000000010000090000330000000000000",false), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0U000000021476073GRXGSHLTP0004||0000000000",false), getSelf());
                        if(printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S         Elavon              Visa Chip DCC         PURCHASE REVERSAL          Point Elavon           Elavon - Demo                              Merch",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S.        1122334455A.I.I.C.     00000000001Date 02/03/18 Time 10:45TML 00000071 STAN 000009PAN     ************0004A.ID      A0000000031010AUT. 474327",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S                                     LOCAL AMOUNT:                           GBP 0.10EXCHANGE RATE:             1 GBP = 12.683821 HKD      Final Amoun",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0St              HKD 1.27                                  TRANSACTION APPROVED  }}"+ (char)27,false), getSelf());
                        }
                        TimeUnit.SECONDS.sleep(3);
                        getSender().tell(new Protocol37Wrapper("CARD REMOVED   ",true), getSelf());
                        getContext().getSystem().stop(getContext().getParent());
                    }
                    
                })
                .build();
    }
    @Override
    public void postStop() throws Exception {
        log.trace(getSelf().path().name()+" REVERSAL ACTOR STOPPED");
    }
}

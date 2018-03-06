package com.ips.actors.functions;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.actors.router.RouterActor;
import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;

public class RefundActor extends AbstractActor{
    private String terminalId = "00000000";
    private final static Logger log = LogManager.getLogger(RefundActor.class); 
    public static Props props(){
        return Props.create(RefundActor.class);
    }
    
    @Override
    public void preStart() throws Exception {
        log.trace("REFUND ACTOR STARTED");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.contains("0A00")){
                        log.info(s);
                       // terminalId = s.substring(0,8);
                    }else{
                        log.info(s);
                        getSender().tell(new Protocol37Wrapper("INSERT CARD   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("DCC BEING OFFERED  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("REMOVE CARD  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0A00000476073******0004ICC018425000000000010611049",false), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"000000710U000000021476073GRXGSHLTP0004||0000000000",false), getSelf());
                        if(RouterActor.printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S         Elavon              Visa Chip DCC               REFUND               Point Elavon           Elavon - Demo                              Merch",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S.        1122334455A.I.I.C.     00000000001Date 02/03/18 Time 10:49TML 00000071 STAN 000011Mod. Online     B.C. ICCAUT. 018425             PAN     ***",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S*********0004                        LOCAL AMOUNT:                           GBP 0.10EXCHANGE RATE:             1 GBP = 12.683821 HKD      Final Amoun",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0St              HKD 1.27                                 I have been offered a  choice of currencies and have chosen to accept   DCC and pay in HKD at ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S    the exchange rate    provided by U.S. Bancorp with an exchange rate     mark-up of 3.50 %          as of today.        More information on     Ela",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0Svon Best Rate at   www.elavon.eu/bestrate.                                                   TRANSACTION APPROVED                                 THAN",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0SK YOU        }}",false), getSelf());
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
       log.trace("REFUND ACTOR STOPPED");
    }

}

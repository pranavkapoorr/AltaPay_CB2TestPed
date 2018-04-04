package com.ips.actors.functions;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ReportActor extends AbstractActor{
    private String terminalId = "00000000";
    private final static Logger log = LogManager.getLogger(ReportActor.class); 
    private boolean printOption;
    public ReportActor(boolean printOption) {
       this.printOption = printOption;
    }
    public static Props props(boolean printOption){
        return Props.create(ReportActor.class, printOption);
    }
    
    @Override
    public void preStart() throws Exception {
        log.trace(getSelf().path().name()+" REPORT ACTOR STARTED");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    
                    if(s.contains("0C00")){
                        log.info(getSelf().path().name()+ s);
                      //  terminalId = s.substring(0,8);
                        getSender().tell(new Protocol37Wrapper("OPERATION IN PROGRESS   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0C0000000000000000000000000000000000500",false), getSelf());
                        if(printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S       END OF DAY             Point Elavon           Elavon - Demo                              TML             00000071Date 02/03/18 Time 10:33STAN ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S000004                                     POS BALANCE             GBP                 0.00                        BANK BALANCE            GBP      ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S           0.00                          OPERATION SUCCEEDED   }}"+ (char)27,false), getSelf());
                        }
                        TimeUnit.MILLISECONDS.sleep(1000);
                        getContext().getSystem().stop(getContext().getParent());
                    }else if(s.contains("0T00")){
                        log.info(getSelf().path().name()+s);
                        //terminalId = s.substring(0,8);
                        getSender().tell(new Protocol37Wrapper("OPERATION IN PROGRESS   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0T000000000000000000500000000",false), getSelf());
                        if(printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S      HOST BALANCE            Point Elavon           Elavon - Demo                              TML             00000071Date 02/03/18 Time 10:32STAN ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S000003                                     POS BALANCE             GBP                 0.00                        BANK BALANCE            GBP      ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S           0.00                          OPERATION SUCCEEDED   }}"+ (char)27,false), getSelf());
                        }
                        TimeUnit.MILLISECONDS.sleep(1000);
                        getContext().getSystem().stop(getContext().getParent());
                    }
                    
                })
                .build();
    }
    @Override
    public void postStop() throws Exception {
        log.trace(getSelf().path().name()+" REPORT ACTOR STOPPED");
    }

}

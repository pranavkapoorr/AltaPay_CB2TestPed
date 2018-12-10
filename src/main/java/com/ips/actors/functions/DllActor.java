package com.ips.actors.functions;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.*;
import com.ips.resources.Protocol37Wrapper;
import akka.actor.*;

public class DllActor  extends AbstractActor{
   private final String terminalId = "00000000";
   private final static Logger log = LogManager.getLogger(DllActor.class); 
   private final boolean printOption;
   public DllActor(boolean printOption) {
      this.printOption = printOption;
   }
   public static Props props(boolean printOption){
       return Props.create(DllActor.class, printOption);
   }
   @Override
    public void preStart() throws Exception {
        log.trace(getSelf().path().name()+" DLL ACTOR STARTED");
    } 
   
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.contains("0D00")){
                        getSender().tell(new Protocol37Wrapper("OPERATION IN PROGRESS   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(10);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0D0000000200002306110300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",false), getSelf());
                        if(printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S     DLL PARAMETERS           Point Elavon           Elavon - Demo                              Date 02/03/18 Time 10:30TML 00000071 STAN 000002CAUSA",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0SL  861 N.OP. 000022Conferment   PAR.TEC  OK                             CARDS HANDLED      Elavon                                            OPERATION",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S SUCCEEDED   }}"+ (char)27,false), getSelf());
                        }
                        getContext().getSystem().stop(getContext().getParent());
                    }
                    
                })
                .build();
    }
    @Override
        public void postStop() throws Exception {
           log.trace(getSelf().path().name()+" DLL ACTOR STOPPED");
        }

}

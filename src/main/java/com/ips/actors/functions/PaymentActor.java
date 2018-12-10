package com.ips.actors.functions;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.*;
import com.ips.resources.Protocol37Wrapper;
import akka.actor.*;

public class PaymentActor extends AbstractActor{
    private final String terminalId = "00000000";
    private final static Logger log = LogManager.getLogger(PaymentActor.class); 
    private final boolean printOption;
    public PaymentActor(boolean printOption) {
       this.printOption = printOption;
    }
    public static Props props(boolean printOption){
        return Props.create(PaymentActor.class, printOption);
    }
    
    @Override
    public void preStart() throws Exception {
        log.trace(getSelf().path().name()+" PAYMENT ACTOR STARTED");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.contains("0P00")){
                        log.info(getSelf().path().name()+ s);
                        //terminalId = s.substring(0,8);
                    }else{
                        log.info(getSelf().path().name()+ s);
                        getSender().tell(new Protocol37Wrapper("INSERT CARD   ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("ENTER PIN AND CONF.",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("AUTH IN PROGRESS ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper("TRANSACTION COMPLETE  ",true), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0E00000476173******0010ICC1614090571613210000000301000009000034000",false), getSelf());
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0U000000147476173JPDLNRZTD0010|0eiI2xZ3HOWqCYlYVSkJGgkPqwcmp0jUiQyo19YlGQhgiaf1xHXaZIzUMgnrgf0VF/YMJkl0XXwLy40c88tQrA==+1|d5949370-acb9-25e0-8010-640190a4ed770000000000",false), getSelf());
                        if(printOption){
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S          Visa               Visa Electron              PURCHASE           Bishops Stortford        Stanstead House                             Merch",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S.   298623318011002A.I.I.C.     10000000301Date 26/02/18 Time 16:13TML 10000114 STAN 000009Mod. Online     B.C. ICCAUT. 161409 OPER. 000034AUTH.RESP.C",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0SODE        00PAN     ************0010A.ID      A0000000032010APPL    ELECTRON DE VISAATC 0001  TCC 826  TT 00TrCC 826     UN 5B11EDB5TVR           028",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S0008000T.C.    F854F8E111BB9867IAD       06010A03600000PAN SEQ.N             01                        AMOUNT  GBP         0.10                      ",false), getSelf());
                            TimeUnit.MILLISECONDS.sleep(120);
                            getSender().tell(new Protocol37Wrapper(terminalId+"0S    TRANSACTION APPROVED                                 THANK YOU                                      PIN VERIFIED      }}"+ (char)27,false), getSelf());
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
        log.trace(getSelf().path().name()+" PAYMENT ACTOR STOPPED");
    }

}

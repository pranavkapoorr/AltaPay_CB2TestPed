package com.ips.actors;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ips.resources.Protocol37Wrapper;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;

public class ReprintAndPedConfigActor extends AbstractActor{
    private String terminalId = "00000000";
    private final static Logger log = LogManager.getLogger(ReprintAndPedConfigActor.class); 
    public static Props props(){
        return Props.create(ReprintAndPedConfigActor.class);
    }
    @Override
    public void preStart() throws Exception {
       log.trace("REPRINT ACTOR STARTED");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s->{
                    if(s.equals("000000000R100")){
                        log.info(s);
                        //terminalId = s.substring(0,8);
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0S          Visa               Visa Electron              PURCHASE           Bishops Stortford        Stanstead House                             Merch",false), getSelf());
                        TimeUnit.MILLISECONDS.sleep(120);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0S.   298623318011002A.I.I.C.     10000000301Date 26/02/18 Time 16:13TML 10000114 STAN 000009Mod. Online     B.C. ICCAUT. 161409 OPER. 000034AUTH.RESP.C",false), getSelf());
                        TimeUnit.MILLISECONDS.sleep(120);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0SODE        00PAN     ************0010A.ID      A0000000032010APPL    ELECTRON DE VISAATC 0001  TCC 826  TT 00TrCC 826     UN 5B11EDB5TVR           028",false), getSelf());
                        TimeUnit.MILLISECONDS.sleep(120);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0S0008000T.C.    F854F8E111BB9867IAD       06010A03600000PAN SEQ.N             01                        AMOUNT  GBP         0.10                      ",false), getSelf());
                        TimeUnit.MILLISECONDS.sleep(120);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0S    TRANSACTION APPROVED                                 THANK YOU                                      PIN VERIFIED      }}",false), getSelf());
                        getContext().getParent().tell(PoisonPill.getInstance(), getSelf());
                    }else if(s.equals("000000000s")){
                        log.info(s);
                        TimeUnit.SECONDS.sleep(1);
                        getSender().tell(new Protocol37Wrapper(terminalId+"0s000000000002031810259SYS40.42MAN84.21EMV173V0ECR10.63SSL01.47EXT05.37EDL04.90P/NIPP350                        S/N11245PP70614902",false), getSelf());
                        getContext().getParent().tell(PoisonPill.getInstance(), getSelf());
                        
                    }
                    
                })
                .build();
    }
    @Override
    public void postStop() throws Exception {
        log.trace("REPRINT ACTOR STOPPED");
    }
}

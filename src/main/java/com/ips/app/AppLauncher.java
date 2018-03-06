package com.ips.app;

import java.net.InetSocketAddress;

import com.ips.actors.tcp.TcpServerActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.io.Tcp;

public class AppLauncher {
    public static void main(String[] args){
        if(isValidIP(args[0]) && isValidPort(args[1])){
            ActorSystem system= ActorSystem.create("testPed");
            ActorRef tcpMnager =Tcp.get(system).manager();
            ActorRef tcpServer= system.actorOf(TcpServerActor.props(tcpMnager ,new InetSocketAddress(args[0], Integer.parseInt(args[1]))),"SERVER");
        }else{
            System.err.println("Check the Server address properly..!!");
        }
    }
    private static boolean isValidPort(String value){
        boolean result = false;
        result = !value.isEmpty() && value!= null && value.matches("[0-9]*") && value.length() < 6;
        return result;
    }
    public static boolean isValidIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }
            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }
            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
    

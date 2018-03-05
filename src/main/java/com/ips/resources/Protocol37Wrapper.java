package com.ips.resources;

final public class Protocol37Wrapper {
    private final String message;
    private final boolean isStatusMessage;
    
    public Protocol37Wrapper(String message, boolean isStatusMessage) {
        this.message = message;
        this.isStatusMessage = isStatusMessage;
    }
    public String getMessage() {
        return addLrc(this.message);
    }
    private String addLrc(String message){
        String messageX;
        if(isStatusMessage){
           messageX = (char)01 + message + (char)04;
           return messageX;
       }else{ 
           messageX = (char)02 + message + (char)03;
           return messageX + calcLRC_P37(messageX);
       }
    }
    /**calcLRC 
     * calculates LRC by Xoring all the bits of message 
     * @param msg is the message for which lrc is calculated;
     * @return Lrc character;
     */
    public static char calcLRC_P37(String msg) {
        int checksum=127;
        for (int i = 0; i < msg.length(); i++){
         checksum ^= msg.charAt(i);
        }
        return (char)checksum;
    }
}

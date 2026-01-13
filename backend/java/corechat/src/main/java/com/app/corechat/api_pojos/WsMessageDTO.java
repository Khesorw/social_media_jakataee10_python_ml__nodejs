package com.app.corechat.api_pojos;

public class WsMessageDTO {
    

    public String type;


    public static class Meta {
        public Long conversationId;
        public Long senderId;
        public Long timestamp;
        public Long callId;
    }

    public static class Payload {
        public String text;
        public String callType;
        public String sdp;
        public String candidate;
        public String reason;
    }
}

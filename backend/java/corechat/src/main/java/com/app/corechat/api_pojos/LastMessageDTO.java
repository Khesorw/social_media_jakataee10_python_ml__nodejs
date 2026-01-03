package com.app.corechat.api_pojos;

public class LastMessageDTO {
    

    private Long senderId;

    private Long convId;
    private String lastMsg;

    public LastMessageDTO(Long convId, String lastMsg, Long senderId) {
        this.convId = convId;
        this.lastMsg = lastMsg;
        this.senderId = senderId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getConvId() {
        return convId;
    }

    public String getLastMsg() {
        return lastMsg;
    }




}

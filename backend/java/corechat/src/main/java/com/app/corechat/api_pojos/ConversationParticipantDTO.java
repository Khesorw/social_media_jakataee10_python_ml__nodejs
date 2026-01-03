package com.app.corechat.api_pojos;

public class ConversationParticipantDTO {

    private Long conversationId;
    private Long userId;

    public ConversationParticipantDTO(Long conversationId, Long userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getUserId() {
        return userId;
    }


    
}

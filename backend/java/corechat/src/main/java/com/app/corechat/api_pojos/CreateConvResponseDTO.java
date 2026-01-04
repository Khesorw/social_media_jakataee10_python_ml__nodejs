package com.app.corechat.api_pojos;

public class CreateConvResponseDTO {
    private Long conversationId;

    public CreateConvResponseDTO() {
        
    }

    public CreateConvResponseDTO(Long conversationId ) {
        this.conversationId = conversationId;
    }

    public Long getMyUserId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    


}

package com.app.corechat.api_pojos;

public class FeedItemDTO {
    public ConversationParticipantDTO participant;
    public LastMessageDTO lastMessage;

    public FeedItemDTO(ConversationParticipantDTO participant, LastMessageDTO lastMessage) {
        this.lastMessage = lastMessage;
        this.participant = participant;
    }

    
}

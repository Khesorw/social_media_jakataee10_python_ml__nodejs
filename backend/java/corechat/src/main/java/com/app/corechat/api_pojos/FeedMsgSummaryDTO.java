package com.app.corechat.api_pojos;

import java.time.OffsetDateTime;

public class FeedMsgSummaryDTO {
    private Long conversationId;
    private String otherUserName;
    private String lastMessageText;
    private Long senderId;
    private OffsetDateTime createdAt;

    public FeedMsgSummaryDTO(Long conversationId, String otherUserName, String lastMessageText, Long senderId, OffsetDateTime createdAt ) {
        this.conversationId = conversationId;
        this.createdAt = createdAt;
        this.lastMessageText = lastMessageText;
        this.otherUserName = otherUserName;
        this.senderId = senderId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public Long getSenderId() {
        return senderId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }


}

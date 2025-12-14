package com.app.corechat.entities;

import java.io.Serializable;
import java.util.Objects;

public class ConversationParticipantId implements Serializable{

    private Long conversation;
    private Long user;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConversationParticipantId))
            return false;
        ConversationParticipantId that = (ConversationParticipantId) o;

        return Objects.equals(conversation, that.conversation) &&
                Objects.equals(user, that.user);
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(conversation, user);
    }



    
}

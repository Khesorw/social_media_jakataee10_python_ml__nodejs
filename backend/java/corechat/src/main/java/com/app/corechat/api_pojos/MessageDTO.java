package com.app.corechat.api_pojos;

import java.time.OffsetDateTime;

public class MessageDTO {

    public Long id;
    public String text;
    public Long senderId;
    public String senderEmail;
    public OffsetDateTime createdAt;
}

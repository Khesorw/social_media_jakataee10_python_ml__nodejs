package com.app.corechat.api_pojos;

public class CreateConvDTO {
    

    private Long otherUserid;

    public CreateConvDTO() {
        
    }
    public CreateConvDTO(Long otherUserid) {
        this.otherUserid = otherUserid;
    }

    public Long getOtherUserid() {
        return otherUserid;
    }

    public void setOtherUserid(Long otherUserid) {
        this.otherUserid = otherUserid;
    }


    

    
}

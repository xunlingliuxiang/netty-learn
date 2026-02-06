package com.cosco.chat.session;

import lombok.Data;

@Data
public class Session {
    private String userId;
    private String userName;

    @Override
    public String toString(){
        return userId + ":" + userName;
    }
    public Session(String userId,String userName){
        this.userName = userName;
        this.userId = userId;
    }
}

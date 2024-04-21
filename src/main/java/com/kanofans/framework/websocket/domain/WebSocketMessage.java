package com.kanofans.framework.websocket.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WebSocketMessage {
    private Long userId;

    private String nickName;

    private String avatar;

    private String message;

    /**
     * 消息类型 0 用户消息 1 系统消息 2 用户进入群聊 3 用户离开群聊 4 在线用户列表
     */
    private Integer messageType;

    private Long groupId;

    public WebSocketMessage() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", userId)
                .append("nickName", nickName)
                .append("avatar", avatar)
                .append("message", message)
                .append("messageType", messageType)
                .append("groupId", groupId)
                .toString();
    }
}

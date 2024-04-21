package com.kanofans.project.slp.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SlpUserMessage {
    private Long fromUserId;

    private Long toUserId;

    /**
     * 消息类型 0: 群组申请消息
     */
    private Integer messageType;

    /**
     * 消息内容 若为群组申请消息则为群组id
     */
    private String message;

    public SlpUserMessage() {
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fromUserId", fromUserId)
                .append("toUserId", toUserId)
                .append("messageType", messageType)
                .append("message", message)
                .toString();
    }
}

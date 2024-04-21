package com.kanofans.framework.websocket;

import com.alibaba.fastjson2.JSON;
import com.kanofans.framework.websocket.domain.WebSocketMessage;
import com.kanofans.framework.websocket.domain.WebSocketUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 客户端用户集
 */
public class WebSocketUsers {
    /**
     * WebSocketUsers 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUsers.class);

    /**
     * 用户集
     */
    private final Map<Long, Set<Session>> GROUP = new ConcurrentHashMap<Long, Set<Session>>();

    /**
     * 存储用户
     *
     * @param subjectId     唯一键
     * @param session 用户信息
     */
    public void put(Long subjectId, Session session) {
//        USERS.put(key, session);
        if (GROUP.containsKey(subjectId)) {
            GROUP.get(subjectId).add(session);
        } else {
            Set<Session> subject = ConcurrentHashMap.newKeySet();
            subject.add(session);
            GROUP.put(subjectId, subject);
        }
    }

    /**
     * 移出用户
     *
     * @param subjectId 键
     */
    public boolean remove(Long subjectId, Session session) {
        if (GROUP.get(subjectId) == null) {
            return true;
        }
        boolean remove = GROUP.get(subjectId).remove(session);
        LOGGER.info("\n 移出结果 - {}", !remove ? "失败" : "成功");
        return remove;
    }

    /**
     * 获取在线用户列表
     *
     * @return 返回用户集合
     */
    public Set<Session> getUsers(Long subjectId) {
        return GROUP.get(subjectId);
    }

    /**
     * 群发消息文本消息
     *
     * @param webSocketMessage 消息内容
     */
    public void sendMessageToUsersByText(Long subjectId, WebSocketMessage webSocketMessage, Integer messageType) {
        GROUP.get(subjectId).forEach(session -> {
            sendMessageToUserByText(session, webSocketMessage, messageType);
        });
    }

    /**
     * 发送文本消息
     *
     * @param webSocketMessage 消息内容
     */
    public void sendMessageToUserByText(Session session, WebSocketMessage webSocketMessage, Integer messageType) {
        if (session != null) {
            try {
                webSocketMessage.setMessageType(messageType);
                session.getBasicRemote().sendText(JSON.toJSONString(webSocketMessage));
            } catch (IOException e) {
                LOGGER.error("\n[发送消息异常]", e);
            }
        } else {
            LOGGER.info("\n[你已离线]");
        }
    }

    /**
     * @param messageType 消息类型 0 用户消息 1 系统消息 2 用户进入群聊 3 用户离开群聊 4 在线用户列表
     */
    public void sendMessageToUserByText(Session session, String message, Integer messageType) {
        if (session != null) {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setMessage(message);
            webSocketMessage.setMessageType(messageType);
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(webSocketMessage));
            } catch (IOException e) {
                LOGGER.error("\n[发送消息异常]", e);
            }
        } else {
            LOGGER.info("\n[你已离线]");
        }
    }

    public void sendOnlineUsers(Long subjectId, Session session) {
        Set<Session> users = getUsers(subjectId);
        List<WebSocketUser> webSocketUsers = new ArrayList<>();
        users.forEach(ss -> {
            webSocketUsers.add(buildWebsocketUser(ss));
        });
        sendMessageToUserByText(session, JSON.toJSONString(webSocketUsers), 4);
    }

    public void broadcastOnlineUsers(Long subjectId){
        Set<Session> users = getUsers(subjectId);
        List<WebSocketUser> webSocketUsers = new ArrayList<>();
        if (users != null){
            users.forEach(ss -> {
                webSocketUsers.add(buildWebsocketUser(ss));
            });
        }

        Set<Session> sessions = GROUP.get(subjectId);
        if (sessions != null) {
            sessions.forEach(ss -> {
                sendMessageToUserByText(ss, JSON.toJSONString(webSocketUsers), 4);
            });
        }
    }

    public void broadcastUserConnect(Long subjectId, Session session){
        WebSocketUser webSocketUser = buildWebsocketUser(session);
        Set<Session> sessions = GROUP.get(subjectId);
        if (sessions != null) {
            sessions.forEach(ss -> {
                sendMessageToUserByText(ss, JSON.toJSONString(webSocketUser), 2);
            });
        }
    }

    public void broadcastUserDisconnect(Long subjectId, Session session){
        WebSocketUser webSocketUser = buildWebsocketUser(session);
        Set<Session> sessions = GROUP.get(subjectId);
        if (sessions != null) {
            sessions.forEach(ss -> {
                sendMessageToUserByText(ss, JSON.toJSONString(webSocketUser), 3);
            });
        }
    }

    private WebSocketUser buildWebsocketUser(Session session) {
        WebSocketUser webSocketUser = new WebSocketUser();
        Map<String, Object> userProperties = session.getUserProperties();
        webSocketUser.setUserId((Long) userProperties.get("userId"));
        webSocketUser.setNickName((String) userProperties.get("nickName"));
        webSocketUser.setAvatar((String) userProperties.get("avatar"));
        return webSocketUser;
    }
}

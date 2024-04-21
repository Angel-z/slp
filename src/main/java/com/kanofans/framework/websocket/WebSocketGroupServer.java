package com.kanofans.framework.websocket;

import com.kanofans.framework.security.LoginUser;
import com.kanofans.framework.security.service.PermissionService;
import com.kanofans.framework.security.service.TokenService;
import com.kanofans.framework.websocket.domain.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;

/**
 * websocket 消息处理
 * 
 *
 */
@Component
@ServerEndpoint(value = "/websocket/group/{groupId}", configurator = WebSocketConfig.class)
public class WebSocketGroupServer
{
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketGroupServer.class);

    private static TokenService tokenService;

    private static PermissionService permissionService;

    private static final WebSocketUsers webSocketUsers = new WebSocketUsers();

    @Autowired
    public void setTokenService(TokenService tokenService) {
        WebSocketGroupServer.tokenService = tokenService;
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        WebSocketGroupServer.permissionService = permissionService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("groupId") Long groupId,Session session) throws Exception
    {
        Set<Session> users = webSocketUsers.getUsers(groupId);
        String token = (String) session.getUserProperties().get("token");
        LoginUser loginUser = tokenService.getLoginUserByToken(token);


        if (loginUser == null)
        {
            LOGGER.error("\n 用户未登录");
            webSocketUsers.sendMessageToUserByText(session, "用户未登录", 1);
            session.close();
            return;
        } else if (!permissionService.isInGroup(groupId, loginUser.getUserId())) {
            LOGGER.error("\n 用户不在群组中");
            webSocketUsers.sendMessageToUserByText(session, "非法连接", 1);
            session.close();
            return;
        } else {
            final Map<String, Object> userProperties = session.getUserProperties();
            userProperties.put("userId", loginUser.getUserId());
            userProperties.put("nickName", loginUser.getUser().getNickName());
            userProperties.put("avatar", loginUser.getUser().getAvatar());
        }

        if (users != null && users.size() >= 100)
        {
            LOGGER.error("\n 当前在线人数超过限制数");
            webSocketUsers.sendMessageToUserByText(session, "当前在线人数超过限制数", 1);
            session.close();
        }
        else
        {
            // 添加用户
            webSocketUsers.put(groupId, session);
            LOGGER.info("\n 建立连接 - {}", session);
            LOGGER.info("\n 当前人数 - {}", webSocketUsers.getUsers(groupId).size());
            webSocketUsers.sendMessageToUserByText(session, "连接成功", 1);
//            webSocketUsers.sendOnlineUsers(groupId, session);
            webSocketUsers.broadcastOnlineUsers(groupId);
            webSocketUsers.broadcastUserConnect(groupId, session);
        }
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(@PathParam("groupId") Long groupId, Session session)
    {
        LOGGER.info("\n 关闭连接 - {}", session);
        // 移除用户
        if (webSocketUsers.remove(groupId, session))
        {
            webSocketUsers.broadcastOnlineUsers(groupId);
            webSocketUsers.broadcastUserDisconnect(groupId, session);
        }
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(@PathParam("groupId") Long groupId, Session session, Throwable exception) throws Exception
    {
        if (session.isOpen())
        {
            // 关闭连接
            session.close();
        }
        String sessionId = session.getId();
        LOGGER.info("\n 连接异常 - {}", sessionId);
        LOGGER.info("\n 异常信息 - {}", exception);
        // 移出用户
        webSocketUsers.remove(groupId, session);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(@PathParam("groupId") Long groupId,String message, Session session)
    {
        final Map<String, Object> userProperties = session.getUserProperties();
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setUserId((Long) userProperties.get("userId"));
        webSocketMessage.setNickName((String) userProperties.get("nickName"));
        webSocketMessage.setAvatar((String) userProperties.get("avatar"));
        webSocketMessage.setMessage(message);
        webSocketUsers.sendMessageToUsersByText(groupId, webSocketMessage, 0);
    }
}

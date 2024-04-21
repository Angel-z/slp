package com.kanofans.framework.websocket;

import com.kanofans.framework.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

/**
 * websocket 配置
 * 
 *
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator
{
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }

    @Autowired
    private TokenService tokenService;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 这个userProperties 可以通过 session.getUserProperties()获取
        final Map<String, Object> userProperties = sec.getUserProperties();
        Map<String, List<String>> headers = request.getHeaders();
        List<String> protocol = headers.get("Sec-Websocket-Protocol");
        // 存放自己想要的header信息
        if(protocol != null){
            userProperties.put("token", protocol.get(0));
        }
    }
}

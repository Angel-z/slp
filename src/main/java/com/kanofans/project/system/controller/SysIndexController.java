package com.kanofans.project.system.controller;

import com.kanofans.common.utils.StringUtils;
import com.kanofans.framework.config.SlpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 *
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private SlpConfig slpConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", slpConfig.getName(), slpConfig.getVersion());
    }
}

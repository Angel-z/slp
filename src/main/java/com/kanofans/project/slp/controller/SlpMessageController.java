package com.kanofans.project.slp.controller;

import com.kanofans.common.constant.CacheConstants;
import com.kanofans.framework.redis.RedisCache;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.project.slp.domain.SlpGroup;
import com.kanofans.project.slp.domain.SlpGroupMember;
import com.kanofans.project.slp.domain.SlpUserMessage;
import com.kanofans.project.slp.service.ISlpGroupMemberService;
import com.kanofans.project.slp.service.ISlpGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class SlpMessageController extends BaseController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISlpGroupService groupService;

    @Autowired
    private ISlpGroupMemberService groupMemberService;

    @GetMapping("/sent")
    public AjaxResult getSentMessages() {
        AjaxResult ajax = AjaxResult.success();
        List<SlpUserMessage> list = redisCache.getCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString());
        List<SlpUserMessage> sentList = list.stream().filter(m -> m.getFromUserId().equals(getUserId())).collect(Collectors.toList());
        ajax.put(AjaxResult.DATA_TAG, sentList);
        return ajax;
    }

    @GetMapping("/received")
    public AjaxResult getReceivedMessages() {
        AjaxResult ajax = AjaxResult.success();
        List<SlpUserMessage> list = redisCache.getCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString());
        List<SlpUserMessage> receivedList = list.stream().filter(m -> m.getToUserId().equals(getUserId())).collect(Collectors.toList());
        ajax.put(AjaxResult.DATA_TAG, receivedList);
        return ajax;
    }

    @PostMapping("/group/apply/{groupId}")
    public AjaxResult applyGroup(@PathVariable Long groupId) {
        SlpGroup group = groupService.selectGroupById(groupId);
        if (group == null || group.getStatus() == 1){
            return AjaxResult.error("群组不存在");
        } else if (group.getGroupType() == 1) {
            return AjaxResult.warn("群组为公共群组");
        }

        SlpUserMessage userMessage = new SlpUserMessage();
        userMessage.setFromUserId(getUserId());
        userMessage.setToUserId(Long.valueOf(group.getCreateBy()));
        userMessage.setMessageType(0);
        userMessage.setMessage(groupId.toString());

        if (redisCache.checkFromCacheList(CacheConstants.USER_MESSAGE_KEY + group.getCreateBy(), userMessage)) {
            return AjaxResult.warn("已经申请过了");
        }

        redisCache.addToCacheList(CacheConstants.USER_MESSAGE_KEY + group.getCreateBy(), userMessage);
        redisCache.addToCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString(), userMessage);

        return AjaxResult.success("申请成功");
    }

    @PostMapping("/group/accept")
    public AjaxResult acceptGroup(@RequestBody SlpUserMessage userMessage) {
        SlpGroup group = groupService.selectGroupById(Long.valueOf(userMessage.getMessage()));
        if (group == null || group.getStatus() == 1){
            return AjaxResult.error("群组不存在");
        }

        if (redisCache.checkFromCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString(), userMessage)) {
            SlpGroupMember slpGroupMember = new SlpGroupMember();
            slpGroupMember.setGroupId(Long.valueOf(userMessage.getMessage()));
            slpGroupMember.setUserId(userMessage.getFromUserId());
            groupMemberService.insertGroupMember(slpGroupMember);

            redisCache.removeFromCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString(), userMessage);
            redisCache.removeFromCacheList(CacheConstants.USER_MESSAGE_KEY + userMessage.getFromUserId().toString(), userMessage);
            return AjaxResult.success("已同意");
        }

        return AjaxResult.error("非法参数");
    }

    @PostMapping("/group/reject")
    public AjaxResult rejectGroup(@RequestBody SlpUserMessage userMessage) {
        if (redisCache.checkFromCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString(), userMessage)) {
            redisCache.removeFromCacheList(CacheConstants.USER_MESSAGE_KEY + getUserId().toString(), userMessage);
            redisCache.removeFromCacheList(CacheConstants.USER_MESSAGE_KEY + userMessage.getFromUserId().toString(), userMessage);
            return AjaxResult.success("已拒绝");
        }

        return AjaxResult.error("非法参数");
    }
}

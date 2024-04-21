package com.kanofans.project.slp.controller;

import com.kanofans.framework.aspectj.lang.annotation.Log;
import com.kanofans.framework.aspectj.lang.enums.BusinessType;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.framework.web.page.TableDataInfo;
import com.kanofans.project.slp.domain.SlpGroup;
import com.kanofans.project.slp.domain.SlpGroupMember;
import com.kanofans.project.slp.domain.SlpUserPublicInfo;
import com.kanofans.project.slp.service.ISlpGroupMemberService;
import com.kanofans.project.slp.service.ISlpGroupService;
import com.kanofans.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class SlpGroupController extends BaseController {

    @Autowired
    private ISlpGroupService groupService;

    @Autowired
    private ISlpGroupMemberService groupMemberService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取公开群组列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SlpGroup group) {
        group.setGroupType(1);
        group.setStatus(0);
        startPage();
        List<SlpGroup> list = groupService.selectGroupList(group);
        return getDataTable(list);
    }

    /**
     * 通过群组id获取群组
     */
    @GetMapping("/{groupId}")
    public AjaxResult getGroup(@PathVariable Long groupId) {
        AjaxResult ajax = AjaxResult.success();
        if (groupId != null) {
            SlpGroup slpGroup = groupService.selectGroupById(groupId);
            if (slpGroup == null) {
                return AjaxResult.error("群组不存在");
            }
            ajax.put(AjaxResult.DATA_TAG, slpGroup);
        }
        return ajax;
    }

    /**
     * 获取我的群组
     */
    @GetMapping("/mine")
    public TableDataInfo getMyGroups() {
        SlpGroup group = new SlpGroup();
        group.setCreateBy(getUserId().toString());
        startPage();
        List<SlpGroup> list = groupService.selectGroupList(group);
        return getDataTable(list);
    }

    /**
     * 获取我加入的群组
     */
    @GetMapping("/joined")
    public TableDataInfo getJoinedGroups() {
        startPage();
        List<SlpGroupMember> groupIds = groupMemberService.selectBelongGroupList(getUserId());
        Long[] ids = groupIds.stream().map(SlpGroupMember::getGroupId).toArray(Long[]::new);
        System.out.println(groupIds);
        if (ids.length == 0) {
            return getDataTable(groupIds);
        }
        List<SlpGroup> list = groupService.selectGroupListByIds(ids);
        list.removeIf(group -> group.getCreateBy().equals(getUserId().toString()) || group.getStatus() == 1);
        return getDataTable(list);
    }

    /**
     * 新增群组
     */
    @Log(title = "群组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addGroup(@RequestBody SlpGroup group) {
        group.setCreateBy(getUserId().toString());
        group.setStatus(0);
        int count = groupService.insertGroup(group);
        if (count > 0) {
            SlpGroupMember groupMember = new SlpGroupMember();
            groupMember.setGroupId(group.getGroupId());
            groupMember.setUserId(getUserId());
            groupMemberService.insertGroupMember(groupMember);
        }
        return toAjax(count);
    }

    /**
     * 解散群组
     */
    @PreAuthorize("@ss.hasGroupPermission(#groupId)")
    @Log(title = "群组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupId}")
    public AjaxResult deleteGroup(@PathVariable Long groupId) {
        return toAjax(groupService.deleteGroup(groupId));
    }

    /**
     * 获取群组成员列表
     */
    @PreAuthorize("@ss.isInGroup(#groupId)")
    @GetMapping("/member/list/{groupId}")
    public AjaxResult getGroupMemberList(@PathVariable Long groupId) {
        AjaxResult ajax = AjaxResult.success();
        List<SlpGroupMember> list = groupMemberService.selectGroupMemberList(groupId);
        List<SlpUserPublicInfo> userPublicInfos = list.stream().map(groupMember -> userService.selectUserPublicInfoById(groupMember.getUserId())).collect(Collectors.toList());
        ajax.put(AjaxResult.DATA_TAG, userPublicInfos);
        return ajax;
    }

    /**
     * 判断用户是否在群组中
     */
    @GetMapping("/member/isInGroup/{groupId}")
    public AjaxResult isInGroup(@PathVariable Long groupId) {
        AjaxResult ajax;
        SlpGroupMember groupMember = new SlpGroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(getUserId());
        List<SlpGroupMember> list = groupMemberService.selectGroupMember(groupMember);
        if (!list.isEmpty()) {
            ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, true);
        } else {
            ajax = AjaxResult.error("非群组成员");
            ajax.put(AjaxResult.DATA_TAG, false);
        }
        return ajax;
    }

    /**
     * 加入公共群组
     */
    @PostMapping("/member/join/{groupId}")
    public AjaxResult joinGroup(@PathVariable Long groupId) {
        SlpGroupMember groupMember = new SlpGroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(getUserId());
        SlpGroup group = groupService.selectGroupById(groupId);
        if (group == null || group.getStatus() == 1) {
            return AjaxResult.error("群组不存在或已被封禁");
        } else if (group.getGroupType() == 0) {
            return AjaxResult.error("群组为私有群组");
        } else if (!groupMemberService.selectGroupMember(groupMember).isEmpty()) {
            return AjaxResult.warn("已经加入该群组");
        }
        return toAjax(groupMemberService.insertGroupMember(groupMember));
    }

    /**
     * 退出群组
     */
    @PreAuthorize("@ss.isInGroup(#groupId)")
    @DeleteMapping("/member/quit/{groupId}")
    public AjaxResult quitGroup(@PathVariable Long groupId) {
        SlpGroupMember groupMember = new SlpGroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(getUserId());
        return toAjax(groupMemberService.deleteGroupMember(groupMember));
    }
}

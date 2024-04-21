package com.kanofans.framework.security.service;

import com.kanofans.common.constant.Constants;
import com.kanofans.common.utils.SecurityUtils;
import com.kanofans.common.utils.StringUtils;
import com.kanofans.framework.security.LoginUser;
import com.kanofans.framework.security.context.PermissionContextHolder;
import com.kanofans.project.slp.domain.SlpGroup;
import com.kanofans.project.slp.domain.SlpGroupMember;
import com.kanofans.project.slp.domain.SlpIssue;
import com.kanofans.project.slp.domain.SlpIssueReply;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.service.ISlpGroupMemberService;
import com.kanofans.project.slp.service.ISlpGroupService;
import com.kanofans.project.slp.service.ISlpIssueService;
import com.kanofans.project.slp.service.ISlpSubjectService;
import com.kanofans.project.system.domain.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 自定义权限实现，SpringSecurity
 * 
 *
 */
@Service("ss")
public class PermissionService
{
    @Autowired
    private ISlpSubjectService slpSubjectService;

    @Autowired
    private ISlpIssueService slpIssueService;

    @Autowired
    private ISlpGroupService slpGroupService;

    @Autowired
    private ISlpGroupMemberService slpGroupMemberService;

    /**
     * 验证用户是否具备某权限
     * 
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission)
    {
        return hasPermi(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isEmpty(permissions))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(Constants.PERMISSION_DELIMETER))
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     * 
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role)
    {
        if (StringUtils.isEmpty(role))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles())
        {
            String roleKey = sysRole.getRoleKey();
            if (Constants.SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role)
    {
        return hasRole(role) != true;
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles)
    {
        if (StringUtils.isEmpty(roles))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (String role : roles.split(Constants.ROLE_DELIMETER))
        {
            if (hasRole(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     * 
     * @param permissions 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

    /**
     * 判断用户是否有对某个主题的操作权限
     * @param subjectId 主题ID
     * @return 用户是否有对某个主题的操作权限
     */
    public boolean hasSubjectPermission(Long subjectId)
    {
        if (subjectId == null) {
            return false;
        }
        SlpSubject slpSubject = slpSubjectService.selectSubjectById(subjectId);
        if (slpSubject == null) {
            return false;
        } else {
            return slpSubject.getCreateBy().equals(SecurityUtils.getLoginUser().getUserId().toString());
        }
    }

    /**
     * 判断用户是否有对某个问题的操作权限
     * @param issueId 问题ID
     * @return 用户是否有对某个问题的操作权限
     */
    public boolean hasIssuePermission(Long issueId)
    {
        if (issueId == null) {
            return false;
        }
        SlpIssue slpIssue = slpIssueService.selectIssueById(issueId);
        if (slpIssue == null) {
            return false;
        } else {
            return slpIssue.getCreateBy().equals(SecurityUtils.getLoginUser().getUserId().toString()) || this.hasPermi("system:issue:remove");
        }
    }

    /**
     * 判断用户是否有对某个问题回复的操作权限
     * @param replyId 问题回复ID
     * @return 用户是否有对某个问题回复的操作权限
     */
    public boolean hasReplyPermission(Long replyId)
    {
        if (replyId == null) {
            return false;
        }
        SlpIssueReply slpIssueReply = slpIssueService.selectIssueReplyById(replyId);
        if (slpIssueReply == null) {
            return false;
        } else {
            return slpIssueReply.getCreateBy().equals(SecurityUtils.getLoginUser().getUserId().toString()) || this.hasPermi("system:issueReply:remove");
        }
    }

    /**
     * 判断用户是否有对某个群组的操作权限
     * @param groupId 群组ID
     * @return 用户是否有对某个群组的操作权限
     */
    public boolean hasGroupPermission(Long groupId)
    {
        if (groupId == null) {
            return false;
        }
        SlpGroup slpGroup = slpGroupService.selectGroupById(groupId);
        if (slpGroup == null) {
            return false;
        } else {
            return slpGroup.getCreateBy().equals(SecurityUtils.getLoginUser().getUserId().toString());
        }
    }

    /**
     * 判断用户是否在某个群组中
     * @param groupId 群组ID
     * @return 用户是否在某个群组中
     */
    public boolean isInGroup(Long groupId)
    {
        if (groupId == null) {
            return false;
        }
        SlpGroupMember slpGroupMember = new SlpGroupMember();
        slpGroupMember.setGroupId(groupId);
        slpGroupMember.setUserId(SecurityUtils.getLoginUser().getUserId());
        return !slpGroupMemberService.selectGroupMember(slpGroupMember).isEmpty();
    }

    public boolean isInGroup(Long groupId, Long userId)
    {
        if (groupId == null) {
            return false;
        }
        SlpGroup slpGroup = slpGroupService.selectGroupById(groupId);
        if (slpGroup == null || slpGroup.getStatus() == 1) {
            return false;
        }
        SlpGroupMember slpGroupMember = new SlpGroupMember();
        slpGroupMember.setGroupId(groupId);
        slpGroupMember.setUserId(userId);
        return !slpGroupMemberService.selectGroupMember(slpGroupMember).isEmpty();
    }
}

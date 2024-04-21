package com.kanofans.project.slp.service;

import com.kanofans.project.slp.domain.SlpGroupMember;

import java.util.List;

public interface ISlpGroupMemberService {
    public List<SlpGroupMember> selectGroupMemberList(Long groupId);

    public List<SlpGroupMember> selectBelongGroupList(Long userId);

    public List<SlpGroupMember> selectGroupMember(SlpGroupMember groupMember);

    public int insertGroupMember(SlpGroupMember groupMember);

    public int deleteGroupMember(SlpGroupMember groupMember);
}

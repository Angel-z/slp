package com.kanofans.project.slp.service.impl;

import com.kanofans.project.slp.domain.SlpGroupMember;
import com.kanofans.project.slp.mapper.SlpGroupMemberMapper;
import com.kanofans.project.slp.service.ISlpGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlpGroupMemberServiceImpl implements ISlpGroupMemberService {
    @Autowired
    private SlpGroupMemberMapper groupMemberMapper;

    @Override
    public List<SlpGroupMember> selectGroupMemberList(Long groupId) {
        return groupMemberMapper.selectGroupMemberList(groupId);
    }

    @Override
    public List<SlpGroupMember> selectBelongGroupList(Long userId) {
        return groupMemberMapper.selectBelongGroupList(userId);
    }

    @Override
    public List<SlpGroupMember> selectGroupMember(SlpGroupMember groupMember) {
        return groupMemberMapper.selectGroupMember(groupMember);
    }

    @Override
    public int insertGroupMember(SlpGroupMember groupMember) {
        return groupMemberMapper.insertGroupMember(groupMember);
    }

    @Override
    public int deleteGroupMember(SlpGroupMember groupMember) {
        return groupMemberMapper.deleteGroupMember(groupMember);
    }
}

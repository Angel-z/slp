package com.kanofans.project.slp.service.impl;

import com.kanofans.project.slp.domain.SlpGroup;
import com.kanofans.project.slp.mapper.SlpGroupMapper;
import com.kanofans.project.slp.service.ISlpGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlpGroupServiceImpl implements ISlpGroupService {
    @Autowired
    private SlpGroupMapper groupMapper;

    @Override
    public List<SlpGroup> selectGroupList(SlpGroup group) {
        return groupMapper.selectGroupList(group);
    }

    @Override
    public SlpGroup selectGroupById(Long groupId) {
        return groupMapper.selectGroupById(groupId);
    }

    @Override
    public List<SlpGroup> selectGroupListByIds(Long[] groupIds) {
        return groupMapper.selectGroupListByIds(groupIds);
    }

    @Override
    public int insertGroup(SlpGroup group) {
        return groupMapper.insertGroup(group);
    }

    @Override
    public int updateGroup(SlpGroup group) {
        return groupMapper.updateGroup(group);
    }

    @Override
    public int deleteGroup(Long groupId) {
        return groupMapper.deleteGroup(groupId);
    }

    @Override
    public int deleteGroupByIds(Long[] groupIds) {
        return groupMapper.deleteGroupByIds(groupIds);
    }
}

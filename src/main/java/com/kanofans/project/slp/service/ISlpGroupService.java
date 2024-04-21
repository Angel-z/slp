package com.kanofans.project.slp.service;

import com.kanofans.project.slp.domain.SlpGroup;

import java.util.List;

public interface ISlpGroupService {
    public List<SlpGroup> selectGroupList(SlpGroup group);

    public SlpGroup selectGroupById(Long groupId);

    public List<SlpGroup> selectGroupListByIds(Long[] groupIds);

    public int insertGroup(SlpGroup group);

    public int updateGroup(SlpGroup group);

    public int deleteGroup(Long groupId);

    public int deleteGroupByIds(Long[] groupIds);
}

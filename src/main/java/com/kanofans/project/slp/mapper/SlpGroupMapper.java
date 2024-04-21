package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpGroupMapper {
    public List<SlpGroup> selectGroupList(SlpGroup group);

    public SlpGroup selectGroupById(@Param("groupId")Long groupId);

    public List<SlpGroup> selectGroupListByIds(Long[] groupIds);

    public int insertGroup(SlpGroup group);

    public int updateGroup(SlpGroup group);

    public int deleteGroup(@Param("groupId")Long groupId);

    public int deleteGroupByIds(Long[] groupIds);
}

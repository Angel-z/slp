package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpGroupMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpGroupMemberMapper {
    public List<SlpGroupMember> selectGroupMemberList(@Param("groupId") Long groupId);

    public List<SlpGroupMember> selectBelongGroupList(@Param("userId") Long userId);

    public List<SlpGroupMember> selectGroupMember(SlpGroupMember groupMember);

    public int insertGroupMember(SlpGroupMember groupMember);

    public int deleteGroupMember(SlpGroupMember groupMember);
}

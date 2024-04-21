package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpIssue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpIssueMapper {
    public List<SlpIssue> selectIssueList(SlpIssue issue);

    public SlpIssue selectIssueById(@Param("issueId")Long issueId);

    public int insertIssue(SlpIssue issue);

    public int updateIssue(SlpIssue issue);

    public int deleteIssue(@Param("issueId")Long issueId);
}

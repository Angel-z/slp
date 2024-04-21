package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpIssueReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpIssueReplyMapper {
    public List<SlpIssueReply> selectIssueReplyList(@Param("issueId")Long issueId);

    public SlpIssueReply selectIssueReplyById(@Param("replyId")Long replyId);

    public int insertIssueReply(SlpIssueReply issueReply);

    public int updateIssueReply(SlpIssueReply issueReply);

    public int deleteIssueReply(@Param("replyId")Long replyId);
}

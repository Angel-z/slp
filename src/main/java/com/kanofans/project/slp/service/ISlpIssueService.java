package com.kanofans.project.slp.service;

import com.kanofans.project.slp.domain.SlpIssue;
import com.kanofans.project.slp.domain.SlpIssueReply;

import java.util.List;

public interface ISlpIssueService {
    public List<SlpIssue> selectIssueList(SlpIssue issue);

    public SlpIssue selectIssueById(Long issueId);

    public int insertIssue(SlpIssue issue);

    public int updateIssue(SlpIssue issue);

    public int deleteIssue(Long issueId);

    public List<SlpIssueReply> selectIssueReplyList(Long issueId);

    public SlpIssueReply selectIssueReplyById(Long replyId);

    public int insertIssueReply(SlpIssueReply issueReply);

    public int updateIssueReply(SlpIssueReply issueReply);

    public int deleteIssueReply(Long replyId);
}

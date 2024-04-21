package com.kanofans.project.slp.service.impl;

import com.kanofans.project.slp.domain.SlpIssue;
import com.kanofans.project.slp.domain.SlpIssueReply;
import com.kanofans.project.slp.mapper.SlpIssueMapper;
import com.kanofans.project.slp.mapper.SlpIssueReplyMapper;
import com.kanofans.project.slp.service.ISlpIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlpIssueServiceImpl implements ISlpIssueService {
    @Autowired
    private SlpIssueMapper issueMapper;

    @Autowired
    private SlpIssueReplyMapper issueReplyMapper;

    @Override
    public List<SlpIssue> selectIssueList(SlpIssue issue) {
        return issueMapper.selectIssueList(issue);
    }

    @Override
    public SlpIssue selectIssueById(Long issueId) {
        return issueMapper.selectIssueById(issueId);
    }

    @Override
    public int insertIssue(SlpIssue issue) {
        return issueMapper.insertIssue(issue);
    }

    @Override
    public int updateIssue(SlpIssue issue) {
        return issueMapper.updateIssue(issue);
    }

    @Override
    public int deleteIssue(Long issueId) {
        return issueMapper.deleteIssue(issueId);
    }

    @Override
    public List<SlpIssueReply> selectIssueReplyList(Long issueId) {
        return issueReplyMapper.selectIssueReplyList(issueId);
    }

    @Override
    public SlpIssueReply selectIssueReplyById(Long replyId) {
        return issueReplyMapper.selectIssueReplyById(replyId);
    }

    @Override
    public int insertIssueReply(SlpIssueReply issueReply) {
        return issueReplyMapper.insertIssueReply(issueReply);
    }

    @Override
    public int updateIssueReply(SlpIssueReply issueReply) {
        return issueReplyMapper.updateIssueReply(issueReply);
    }

    @Override
    public int deleteIssueReply(Long replyId) {
        return issueReplyMapper.deleteIssueReply(replyId);
    }
}

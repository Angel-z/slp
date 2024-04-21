package com.kanofans.project.slp.controller;

import com.kanofans.framework.aspectj.lang.annotation.Log;
import com.kanofans.framework.aspectj.lang.enums.BusinessType;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.framework.web.page.TableDataInfo;
import com.kanofans.project.slp.domain.SlpIssue;
import com.kanofans.project.slp.domain.SlpIssueReply;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.service.ISlpIssueService;
import com.kanofans.project.slp.service.ISlpSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/issue")
public class SlpIssueController extends BaseController {

    @Autowired
    private ISlpIssueService issueService;

    @Autowired
    private ISlpSubjectService subjectService;

    /**
     * 问题
     * */
    @GetMapping("/list")
    public TableDataInfo list(Long subjectId){
        SlpIssue issue = new SlpIssue();
        issue.setSubjectId(subjectId);
        startPage();
        List<SlpIssue> list = issueService.selectIssueList(issue);
        return getDataTable(list);
    }

    @GetMapping("/{issueId}")
    public AjaxResult getIssue(@PathVariable Long issueId) {
        AjaxResult ajax = AjaxResult.success();
        SlpIssue slpIssue = issueService.selectIssueById(issueId);
        ajax.put(AjaxResult.DATA_TAG, slpIssue);
        return ajax;
    }

    @GetMapping("/myIssues")
    public TableDataInfo getMyIssues(){
        SlpIssue issue = new SlpIssue();
        issue.setCreateBy(getUserId().toString());
        startPage();
        List<SlpIssue> list = issueService.selectIssueList(issue);
        return getDataTable(list);
    }

    @Log(title = "问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addIssue(@RequestBody SlpIssue issue) {
        SlpSubject subject = subjectService.selectSubjectById(issue.getSubjectId());
        if (subject == null || subject.getStatus() == 0){
            return AjaxResult.error("文章不存在");
        }
        issue.setCreateBy(getUserId().toString());
        return toAjax(issueService.insertIssue(issue));
    }

    @Log(title = "问题", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasIssuePermission(#issueId)")
    @DeleteMapping("/{issueId}")
    public AjaxResult deleteIssue(@PathVariable Long issueId) {
        return toAjax(issueService.deleteIssue(issueId));
    }

    /**
     * 问题回复
     * */
    @GetMapping("/reply/list")
    public TableDataInfo listReply(Long issueId){
        startPage();
        List<SlpIssueReply> list = issueService.selectIssueReplyList(issueId);
        return getDataTable(list);
    }

    @PostMapping("/reply")
    public AjaxResult addReply(@RequestBody SlpIssueReply issueReply){
        SlpIssue issue = issueService.selectIssueById(issueReply.getIssueId());
        if (issue == null){
            return AjaxResult.error("问题不存在");
        }
        issueReply.setCreateBy(getUserId().toString());
        return toAjax(issueService.insertIssueReply(issueReply));
    }

    @PreAuthorize("@ss.hasReplyPermission(#replyId)")
    @DeleteMapping("/reply/{replyId}")
    public AjaxResult deleteReply(@PathVariable Long replyId){
        return toAjax(issueService.deleteIssueReply(replyId));
    }

}

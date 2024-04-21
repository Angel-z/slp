package com.kanofans.project.system.controller;

import com.kanofans.framework.aspectj.lang.annotation.Log;
import com.kanofans.framework.aspectj.lang.enums.BusinessType;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.framework.web.page.TableDataInfo;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.service.ISlpSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/subject")
public class SysSubjectController extends BaseController {

    @Autowired
    private ISlpSubjectService subjectService;

    @PreAuthorize("@ss.hasPermi('system:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(SlpSubject subject){
        startPage();
        List<SlpSubject> list = subjectService.selectSubjectList(subject);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:subject:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{subjectIds}")
    public AjaxResult remove(@PathVariable Long[] subjectIds){
        return toAjax(subjectService.deleteSubjectByIds(subjectIds));
    }
}

package com.kanofans.project.system.controller;

import com.kanofans.framework.aspectj.lang.annotation.Log;
import com.kanofans.framework.aspectj.lang.enums.BusinessType;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.framework.web.page.TableDataInfo;
import com.kanofans.project.slp.domain.SlpGroup;
import com.kanofans.project.slp.service.ISlpGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/group")
public class SysGroupController extends BaseController {

    @Autowired
    private ISlpGroupService groupService;

    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(SlpGroup group) {
        startPage();
        List<SlpGroup> list = groupService.selectGroupList(group);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:group:edit')")
    @Log(title = "群组管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SlpGroup group) {
        return toAjax(groupService.updateGroup(group));
    }

    @PreAuthorize("@ss.hasPermi('system:group:remove')")
    @Log(title = "群组管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds) {
        return toAjax(groupService.deleteGroupByIds(groupIds));
    }
}

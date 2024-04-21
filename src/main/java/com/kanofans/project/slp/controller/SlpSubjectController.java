package com.kanofans.project.slp.controller;

import com.kanofans.common.utils.StringUtils;
import com.kanofans.framework.aspectj.lang.annotation.Log;
import com.kanofans.framework.aspectj.lang.enums.BusinessType;
import com.kanofans.framework.web.controller.BaseController;
import com.kanofans.framework.web.domain.AjaxResult;
import com.kanofans.framework.web.page.TableDataInfo;
import com.kanofans.project.slp.domain.SlpLearningList;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.domain.SlpSubjectChapter;
import com.kanofans.project.slp.domain.SlpSubjectContent;
import com.kanofans.project.slp.service.ISlpSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/subject")
public class SlpSubjectController extends BaseController {

    @Autowired
    private ISlpSubjectService subjectService;

    /**
     * 课程
     * */
    @GetMapping("/list")
    public TableDataInfo list(SlpSubject subject){
        subject.setStatus(1);
        startPage();
//        startPage(1, 8);
        List<SlpSubject> list = subjectService.selectSubjectList(subject);
        return getDataTable(list);
    }

    @GetMapping(value = {"/", "/{subjectId}"})
    public AjaxResult getSubject(@PathVariable(value = "subjectId") Long subjectId){
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(subjectId)){
            SlpSubject slpSubject = subjectService.selectSubjectById(subjectId);
            if (slpSubject != null && slpSubject.getStatus() == 0){
                if (!Objects.equals(slpSubject.getCreateBy(), getUserId().toString())){
                    slpSubject = null;
                }
            }
            ajax.put(AjaxResult.DATA_TAG, slpSubject);
        }
        return ajax;
    }

    @GetMapping("/mySubjects")
    public TableDataInfo getMySubjects(){
        SlpSubject subject = new SlpSubject();
        subject.setCreateBy(getUserId().toString());
        startPage();
        List<SlpSubject> list = subjectService.selectSubjectList(subject);
        return getDataTable(list);
    }

    @Log(title = "课程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addSubject(@RequestBody SlpSubject subject){
        subject.setCreateBy(getUserId().toString());
        AjaxResult ajax = toAjax(subjectService.insertSubject(subject));
        ajax.put(AjaxResult.DATA_TAG, subject.getSubjectId());
        return ajax;
    }

    @Log(title = "课程", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasSubjectPermission(#subject.subjectId)")
    @PutMapping
    public AjaxResult updateSubject(@RequestBody SlpSubject subject){
        return toAjax(subjectService.updateSubject(subject));
    }

    @Log(title = "课程", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasSubjectPermission(#subjectId)")
    @DeleteMapping("/{subjectId}")
    public AjaxResult deleteSubject(@PathVariable Long subjectId){
        return toAjax(subjectService.deleteSubjectById(subjectId));
    }

    @GetMapping("/search")
    public TableDataInfo searchSubject(String subjectTitle){
        startPage();
        SlpSubject subject = new SlpSubject();
        subject.setSubjectTitle(subjectTitle);
        subject.setStatus(1);
        List<SlpSubject> list = subjectService.selectSubjectList(subject);
        return getDataTable(list);
    }

    /**
     * 章节
     * */
    @GetMapping("/chapter")
    public AjaxResult getSubjectChapter(Long subjectId){
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(subjectId)){
            SlpSubject slpSubject = subjectService.selectSubjectById(subjectId);
            if (slpSubject != null && slpSubject.getStatus() == 0){
                if (!Objects.equals(slpSubject.getCreateBy(), getUserId().toString())){
                    return ajax;
                }
            }

            SlpSubjectChapter subjectChapter = new SlpSubjectChapter();
            subjectChapter.setSubjectId(subjectId);
            List<SlpSubjectChapter> list = subjectService.selectSubjectChapterList(subjectChapter);
            ajax.put(AjaxResult.DATA_TAG, list);
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectChapter.subjectId)")
    @PostMapping("/chapter")
    public AjaxResult addSubjectChapter(@RequestBody SlpSubjectChapter subjectChapter){
        int chapter = subjectService.insertSubjectChapter(subjectChapter);
        SlpSubjectContent subjectContent = new SlpSubjectContent();
        subjectContent.setChapterId(subjectChapter.getChapterId());
        subjectContent.setSubjectId(subjectChapter.getSubjectId());
        int content = subjectService.insertSubjectContent(subjectContent);
        return toAjax(chapter & content);
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectChapter.subjectId)")
    @PutMapping("/chapter")
    public AjaxResult updateSubjectChapter(@RequestBody SlpSubjectChapter subjectChapter){
        return toAjax(subjectService.updateSubjectChapter(subjectChapter));
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectId)")
    @DeleteMapping("/chapter/{chapterId}")
    public AjaxResult deleteSubjectChapter(@PathVariable Long chapterId, Long subjectId){
        return toAjax(subjectService.deleteSubjectChapter(chapterId));
    }

    /**
     * 内容
     * */
    @GetMapping("/content")
    public TableDataInfo getSubjectContent(SlpSubjectContent subjectContent){
        SlpSubject slpSubject = subjectService.selectSubjectById(subjectContent.getSubjectId());
        if (slpSubject != null && slpSubject.getStatus() == 0){
            if (!Objects.equals(slpSubject.getCreateBy(), getUserId().toString())){
                return getDataTable(null);
            }
        }
        startPage(1);
        List<SlpSubjectContent> list = subjectService.selectSubjectContentList(subjectContent);
        return getDataTable(list);
    }

    @GetMapping("/content/file")
    public AjaxResult getSubjectContentFile(SlpSubjectContent subjectContent){
        AjaxResult ajax = AjaxResult.success();
        SlpSubject slpSubject = subjectService.selectSubjectById(subjectContent.getSubjectId());
        if (slpSubject != null && slpSubject.getStatus() == 0){
            if (!Objects.equals(slpSubject.getCreateBy(), getUserId().toString())){
                return ajax;
            }
        }
        List<SlpSubjectContent> list = subjectService.selectSubjectContentFileList(subjectContent);
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectContent.subjectId)")
    @PostMapping("/content")
    public AjaxResult addSubjectContent(@RequestBody SlpSubjectContent subjectContent){
        return toAjax(subjectService.insertSubjectContent(subjectContent));
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectContent.subjectId)")
    @PutMapping("/content")
    public AjaxResult updateSubjectContent(@RequestBody SlpSubjectContent subjectContent){
        return toAjax(subjectService.updateSubjectContent(subjectContent));
    }

    @PreAuthorize("@ss.hasSubjectPermission(#subjectId)")
    @DeleteMapping("/content/{subjectContentId}")
    public AjaxResult deleteSubjectContent(@PathVariable Long subjectContentId,Long subjectId){
        return toAjax(subjectService.deleteSubjectContent(subjectContentId));
    }

    /**
     * 学习中列表
     * */
    @GetMapping("/learning")
    public TableDataInfo getLearningList(Long subjectId){
        SlpLearningList learningList = new SlpLearningList();
        learningList.setUserId(getUserId());
        learningList.setSubjectId(subjectId);
        startPage();
        List<SlpLearningList> list = subjectService.selectLearningList(learningList);
        return getDataTable(list);
    }

    @PostMapping("/learning")
    public AjaxResult addLearningList(@RequestBody SlpLearningList learningList) {
        learningList.setUserId(getUserId());
        return toAjax(subjectService.insertLearningList(learningList));
    }

    @PutMapping("/learning")
    public AjaxResult updateLearningList(@RequestBody SlpLearningList learningList) {
        learningList.setUserId(getUserId());
        return toAjax(subjectService.updateLearningList(learningList));
    }

    @DeleteMapping("/learning/{subjectId}")
    public AjaxResult deleteLearningList(@PathVariable Long subjectId) {
        SlpLearningList learningList = new SlpLearningList();
        learningList.setUserId(getUserId());
        learningList.setSubjectId(subjectId);
        return toAjax(subjectService.deleteLearningList(learningList));
    }
}

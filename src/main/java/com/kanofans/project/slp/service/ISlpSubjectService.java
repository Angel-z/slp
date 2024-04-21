package com.kanofans.project.slp.service;

import com.kanofans.project.slp.domain.SlpLearningList;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.domain.SlpSubjectChapter;
import com.kanofans.project.slp.domain.SlpSubjectContent;

import java.util.List;

public interface ISlpSubjectService {
    public List<SlpSubject> selectSubjectList(SlpSubject subject);

    public SlpSubject selectSubjectById(Long subjectId);

    public SlpSubject selectSubjectByTitle(String subjectTitle);

    public int insertSubject(SlpSubject subject);

    public int updateSubject(SlpSubject subject);

    public int deleteSubjectById(Long subjectId);

    public int deleteSubjectByIds(Long[] subjectIds);

    public List<SlpSubjectChapter> selectSubjectChapterList(SlpSubjectChapter subjectChapter);

    public int insertSubjectChapter(SlpSubjectChapter subjectChapter);

    public int updateSubjectChapter(SlpSubjectChapter subjectChapter);

    public int deleteSubjectChapter(Long subjectChapterId);

    public List<SlpSubjectContent> selectSubjectContentList(SlpSubjectContent subjectContent);

    public List<SlpSubjectContent> selectSubjectContentFileList(SlpSubjectContent subjectContent);

    public int insertSubjectContent(SlpSubjectContent subjectContent);

    public int updateSubjectContent(SlpSubjectContent subjectContent);

    public int deleteSubjectContent(Long subjectContentId);

    public List<SlpLearningList> selectLearningList(SlpLearningList learningList);

    public int insertLearningList(SlpLearningList learningList);

    public int updateLearningList(SlpLearningList learningList);

    public int deleteLearningList(SlpLearningList learningList);
}

package com.kanofans.project.slp.service.impl;

import com.kanofans.project.slp.domain.SlpLearningList;
import com.kanofans.project.slp.domain.SlpSubject;
import com.kanofans.project.slp.domain.SlpSubjectChapter;
import com.kanofans.project.slp.domain.SlpSubjectContent;
import com.kanofans.project.slp.mapper.SlpLearningListMapper;
import com.kanofans.project.slp.mapper.SlpSubjectChapterMapper;
import com.kanofans.project.slp.mapper.SlpSubjectContentMapper;
import com.kanofans.project.slp.mapper.SlpSubjectMapper;
import com.kanofans.project.slp.service.ISlpSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlpSubjectServiceImpl implements ISlpSubjectService {

    @Autowired
    private SlpSubjectMapper subjectMapper;

    @Autowired
    private SlpSubjectChapterMapper subjectChapterMapper;

    @Autowired
    private SlpSubjectContentMapper subjectContentMapper;

    @Autowired
    private SlpLearningListMapper learningListMapper;

    @Override
    public List<SlpSubject> selectSubjectList(SlpSubject subject) {
        return subjectMapper.selectSubjectList(subject);
    }

    @Override
    public SlpSubject selectSubjectById(Long subjectId) {
        return subjectMapper.selectSubjectById(subjectId);
    }

    @Override
    public SlpSubject selectSubjectByTitle(String subjectTitle) {
        return subjectMapper.selectSubjectByTitle(subjectTitle);
    }

    @Override
    public int insertSubject(SlpSubject subject) {
        if (subject.getSubjectDescription() != null && subject.getSubjectDescription().length() > 250) {
            subject.setSubjectDescription(subject.getSubjectDescription().substring(0, 250));
        }
        return subjectMapper.insertSubject(subject);
    }

    @Override
    public int updateSubject(SlpSubject subject) {
        if (subject.getSubjectDescription() != null && subject.getSubjectDescription().length() > 250) {
            subject.setSubjectDescription(subject.getSubjectDescription().substring(0, 250));
        }
        return subjectMapper.updateSubject(subject);
    }

    @Override
    public int deleteSubjectById(Long subjectId) {
        return subjectMapper.deleteSubjectById(subjectId);
    }

    @Override
    public int deleteSubjectByIds(Long[] subjectIds) {
        return subjectMapper.deleteSubjectByIds(subjectIds);
    }

    @Override
    public List<SlpSubjectChapter> selectSubjectChapterList(SlpSubjectChapter subjectChapter) {
        return subjectChapterMapper.selectSubjectChapterList(subjectChapter);
    }

    @Override
    public int insertSubjectChapter(SlpSubjectChapter subjectChapter) {
        return subjectChapterMapper.insertSubjectChapter(subjectChapter);
    }

    @Override
    public int updateSubjectChapter(SlpSubjectChapter subjectChapter) {
        return subjectChapterMapper.updateSubjectChapter(subjectChapter);
    }

    @Override
    public int deleteSubjectChapter(Long subjectChapterId) {
        return subjectChapterMapper.deleteSubjectChapter(subjectChapterId);
    }

    @Override
    public List<SlpSubjectContent> selectSubjectContentList(SlpSubjectContent subjectContent) {
        return subjectContentMapper.selectSubjectContentList(subjectContent);
    }

    @Override
    public List<SlpSubjectContent> selectSubjectContentFileList(SlpSubjectContent subjectContent) {
        return subjectContentMapper.selectSubjectContentFileList(subjectContent);
    }

    @Override
    public int insertSubjectContent(SlpSubjectContent subjectContent) {
        return subjectContentMapper.insertSubjectContent(subjectContent);
    }

    @Override
    public int updateSubjectContent(SlpSubjectContent subjectContent) {
        return subjectContentMapper.updateSubjectContent(subjectContent);
    }

    @Override
    public int deleteSubjectContent(Long subjectContentId) {
        return subjectContentMapper.deleteSubjectContent(subjectContentId);
    }

    @Override
    public List<SlpLearningList> selectLearningList(SlpLearningList learningList) {
        return learningListMapper.selectLearningList(learningList);
    }

    @Override
    public int insertLearningList(SlpLearningList learningList) {
        return learningListMapper.selectLearningList(learningList).isEmpty() ? learningListMapper.insertLearningList(learningList) : 0;
    }

    @Override
    public int updateLearningList(SlpLearningList learningList) {
        return learningListMapper.updateLearningList(learningList);
    }

    @Override
    public int deleteLearningList(SlpLearningList learningList) {
        return learningListMapper.deleteLearningList(learningList);
    }
}

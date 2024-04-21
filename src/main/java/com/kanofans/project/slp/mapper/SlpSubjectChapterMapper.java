package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpSubjectChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpSubjectChapterMapper {
    public List<SlpSubjectChapter> selectSubjectChapterList(SlpSubjectChapter subjectChapter);

    public int insertSubjectChapter(SlpSubjectChapter subjectChapter);

    public int updateSubjectChapter(SlpSubjectChapter subjectChapter);

    public int deleteSubjectChapter(@Param("subjectChapterId") Long subjectChapterId);
}

package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpSubjectContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpSubjectContentMapper {
    public List<SlpSubjectContent> selectSubjectContentList(SlpSubjectContent subjectContent);

    public List<SlpSubjectContent> selectSubjectContentFileList(SlpSubjectContent subjectContent);

    public int insertSubjectContent(SlpSubjectContent subjectContent);

    public int updateSubjectContent(SlpSubjectContent subjectContent);

    public int deleteSubjectContent(@Param("subjectContentId") Long subjectContentId);
}

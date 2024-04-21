package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlpSubjectMapper {
    public List<SlpSubject> selectSubjectList(SlpSubject subject);

    public SlpSubject selectSubjectById(@Param("subjectId") Long subjectId);

    public SlpSubject selectSubjectByTitle(@Param("subjectTitle") String subjectTitle);

    public int insertSubject(SlpSubject subject);

    public int updateSubject(SlpSubject subject);

    public int deleteSubjectById(@Param("subjectId") Long subjectId);

    public int deleteSubjectByIds(Long[] subjectIds);
}

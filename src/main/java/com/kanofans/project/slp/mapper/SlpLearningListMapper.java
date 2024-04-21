package com.kanofans.project.slp.mapper;

import com.kanofans.project.slp.domain.SlpLearningList;

import java.util.List;

public interface SlpLearningListMapper {
    public List<SlpLearningList> selectLearningList(SlpLearningList learningList);

    public int insertLearningList(SlpLearningList learningList);

    public int updateLearningList(SlpLearningList learningList);

    public int deleteLearningList(SlpLearningList learningList);
}

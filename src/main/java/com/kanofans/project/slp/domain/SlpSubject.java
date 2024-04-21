package com.kanofans.project.slp.domain;

import com.kanofans.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SlpSubject extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long subjectId;

    private String subjectTitle;

    private String subjectDescription;

    private Integer status;

    public SlpSubject() {
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("subjectId", getSubjectId())
                .append("subjectTitle", getSubjectTitle())
                .append("subjectDescription", getSubjectDescription())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}

package com.kanofans.project.slp.domain;

import com.kanofans.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SlpGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long groupId;

    private String groupName;

    private Integer groupType;

    private Integer status;

    public SlpGroup() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("groupId", groupId)
                .append("groupName", groupName)
                .append("groupType", groupType)
                .append("status", status)
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}

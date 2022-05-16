package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_PROJECT")
@Setter
@Getter
public class SysProject  extends WtxBasePojo {
    @Id
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "PROJECT_DESC")
    private String projectDesc;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_DATE")
    private Date createDate;
}
package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import com.wtx.common.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "SYS_AREA")
@Setter
@Getter
public class SysArea extends WtxBasePojo {
    @Id
    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "area_ak")
    private String areaAk;

    @Column(name = "level")
    private String level;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "merger_name")
    private String mergerName;

    @Column(name = "ing")
    private String ing;

    @Column(name = "lat")
    private String lat;

    @Column(name = "pinyin")
    private String pinyin;

    @Column(name = "wubi")
    private String wubi;

    @Column(name = "update_time")
    private String update_time;

    private List<SysArea> childs;


}
package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "TZ_ORGAN_DICT")
@Setter
@Getter
public class DictOrganization extends WtxBasePojo {
    @Id
    @Column(name = "ORG_CODE")
    private String orgCode;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "area_code_short")
    private String areaCodeshort;

    @Column(name = "area_code")
    private String areaCode;
    @Column(name = "area_name")
    private String areaName;

    /*@Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "ORG_LEVEL")
    private String orgLevel;

    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "PROVINCE_NAME")
    private String provinceName;

    @Column(name = "CITY_CODE")
    private String cityCode;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "COUNTY_CODE")
    private String countyCode;

    @Column(name = "COUNTY_NAME")
    private String countyName;

    @Column(name = "TOWN_CODE")
    private String townCode;

    @Column(name = "TOWN_NAME")
    private String townName;

    @Column(name = "VILLAGE_CODE")
    private String villageCode;

    @Column(name = "VILLAGE_NAME")
    private String villageName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ORG_ADDRESS")
    private String orgAddress;

    @Column(name = "ORG_WEBSITE")
    private String orgWebsite;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_DATE")
    private Date createDate;*/
}
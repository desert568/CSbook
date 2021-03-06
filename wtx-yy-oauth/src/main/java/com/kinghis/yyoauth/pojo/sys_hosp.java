package com.kinghis.yyoauth.pojo;/**
 * Created by zzw on 2020-03-19.
 */

import com.kinghis.common.model.WtxBasePojo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Desc
 * @Date 2019/6/18
 * @Author gq
 */
@Data
@Table(name = "sys_hosp")
public class sys_hosp extends WtxBasePojo {



    @Id
    private String hosp_code;

    private String hosp_name;

    private String org_code;

}

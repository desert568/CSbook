package com.kinghis.yyoauth.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.T_ftp_info;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FtpMapper extends WtxBaseMapper<T_ftp_info> {


    List<T_ftp_info> queryPage(Page page, @Param("ftp") T_ftp_info ftp);
}

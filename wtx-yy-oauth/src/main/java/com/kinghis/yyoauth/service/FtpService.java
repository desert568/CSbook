package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.T_ftp_info;

public interface FtpService {

    DataSet<T_ftp_info> queryPage(T_ftp_info ftp, Page page);

    T_ftp_info getFtpInfo(String id);

    void save(T_ftp_info ftp);

    void update(T_ftp_info ftp);

    void deleteByIds(String ids);
}

package com.kinghis.emri.util;

import com.kinghis.emri.pojo.T_emri_source;
import com.wtx.common.exception.CommonException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-09 15:50
 */
public class ConnUtil {

    public static Connection getConnection(T_emri_source source) {
        Connection con = null; // 创建用于连接数据库的Connection对象
        String jdbcurl = "";
        try {
            Class.forName(source.getSource_type());// 加载数据驱动
            if (source.getSource_type().contains("oracle")) {
                jdbcurl = "jdbc:oracle:thin:@//" + source.getSource_url();
            } else if (source.getSource_type().contains("sqlserver")) {
                jdbcurl = "jdbc:sqlserver://" + source.getSource_url();
            } else if (source.getSource_type().contains("mysql")) {
                jdbcurl = "jdbc:mysql://" + source.getSource_url() + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&noAccessToProcedureBodies=true";
            } else if (source.getSource_type().contains("db2")) {
                jdbcurl = "jdbc:db2://" + source.getSource_url();
            } else if (source.getSource_type().contains("postgresql")) {
                jdbcurl = "jdbc:postgresql://" + source.getSource_url();
            }else if (source.getSource_type().contains("Cache")) {
                jdbcurl = "jdbc:Cache://" + source.getSource_url();
            }
            con = DriverManager.getConnection(jdbcurl, source.getUser_name(), source.getPassword());// 创建数据连接
        } catch (Exception e) {
            throw new CommonException("数据库连接失败!" + e);
        }
        return con; // 返回所建立的数据库连接
    }

    public static void colseConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void colsePs(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

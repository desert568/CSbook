package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_FTP_INFO")
@Setter
@Getter
public class T_ftp_info extends WtxBasePojo {
    /**
     * 服务器ID
     */
    @Id
    @ApiModelProperty(value = "服务器ID")
    private String id;

    /**
     * 服务器IP
     */
    @Column(name = "server_ip")
    @ApiModelProperty(value = "服务器IP")
    private String serverIp;

    /**
     * 服务器账号
     */
    @Column(name = "server_user")
    @ApiModelProperty(value = "服务器账号")
    private String serverUser;

    /**
     * 服务器密码
     */
    @Column(name = "server_pwd")
    @ApiModelProperty(value = "服务器密码")
    private String serverPwd;

    /**
     * ftp端口
     */
    @Column(name = "ftp_port")
    @ApiModelProperty(value = "ftp端口")
    private Integer ftpPort;

    /**
     * ftp账号
     */
    @Column(name = "ftp_user")
    @ApiModelProperty(value = "ftp账号")
    private String ftpUser;

    /**
     * ftp密码
     */
    @Column(name = "ftp_pwd")
    @ApiModelProperty(value = "ftp密码")
    private String ftpPwd;

    /**
     * 0运行、1闲置、2超标
     */
    @ApiModelProperty(value = "0运行、1闲置、2超标")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 0：临时服务器、1：正式服务器
     */
    @ApiModelProperty(value = "0：临时服务器、1：正式服务器")
    private String type;

    /**
     * 磁盘大小(GB)
     */
    @ApiModelProperty(value = "磁盘大小(GB)")
    private String size;

    @Column(name = "file_root_path")
    private String fileRootPath;   //文件服务器根目录
}
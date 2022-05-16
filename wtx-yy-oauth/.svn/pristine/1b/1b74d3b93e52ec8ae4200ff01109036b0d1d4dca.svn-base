package com.kinghis.yyoauth.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_file_info")
@Setter
@Getter
public class T_file_info {
    @Id
    private String id;

    @Column(name = "files_name")
    private String filesName;

    private String remark;

    @Column(name = "save_files_name")
    private String saveFilesName;


    @Column(name = "root_directory")
    private String rootDirectory;

    @Column(name = "files_save_directory")
    private String filesSaveDirectory;

    @Column(name = "server_id")
    private String serverId;

    @Column(name = "upload_date")
    private Date uploadDate;
}
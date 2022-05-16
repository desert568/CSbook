package com.kinghis.yyoauth.service;

import com.kinghis.yyoauth.model.FileModel;

import java.util.Map;

public interface FileApiService {


    Map<String,Object> uploadFile(FileModel model);


    Map<String,Object> checkSuccess(String fileID);

    Map<String,Object> downloadFile(FileModel model);

    Map<String,Object> deleteFile(String fileID);

    Map<String,Object> tempToFormal(String fileID);

    Map<String,Object> formalToTemp(String fileID);
}

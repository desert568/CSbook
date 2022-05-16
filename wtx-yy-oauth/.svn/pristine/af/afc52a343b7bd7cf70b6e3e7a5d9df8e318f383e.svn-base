package com.kinghis.yyoauth.model.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-11-04 16:54
 */
@Data
public class MessageModel implements Serializable {

    private Message message;

    private TopicConfig topicConfig;


    @Data
    public class Message{
        private String content;

        private String messageId;

        private String msgType;
    }

    @Data
    public class TopicConfig{
        private String topicCode;
    }

}

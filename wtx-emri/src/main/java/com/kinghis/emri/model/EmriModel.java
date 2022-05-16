package com.kinghis.emri.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmriModel implements Serializable {
    private String start_date;

    private String end_date;

    private String patient_id;

    private String table_name;

    private String userName;

}

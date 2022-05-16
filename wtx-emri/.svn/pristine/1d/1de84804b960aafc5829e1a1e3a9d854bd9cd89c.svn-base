package com.kinghis.emri.util;

import com.github.pagehelper.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname DataConversion
 * @Description TODO
 * @Date 2020-12-27 15:48
 * @Created by Sunny
 */
public class DataConversion {

    public static List<Map<String, Object>> convertDiagListToArrayList(List<Map<String, Object>> ls) {
        ArrayList tempList = new ArrayList();
        try {
            HashMap tempHash = null;
            System.out.println("List<PAT_DIAG>大小：==" + ls.size());
            for (int i = 0; i < ls.size(); i++) {
                Map<String, Object> d = ls.get(i);
                tempHash = new HashMap();
                tempHash.put("patientId".toUpperCase(), d.get("Patient_id".toUpperCase()));
                tempHash.put("visitId".toUpperCase(), d.get("Visit_id".toUpperCase()));
                tempHash.put("icdcode".toUpperCase(), d.get("Icdcode".toUpperCase()));
                tempHash.put("transCode".toUpperCase(), d.get("Trans_code".toUpperCase()));
                tempHash.put("transCodeXh".toUpperCase(), d.get("Trans_code_xh".toUpperCase()));
                tempHash.put("diagType".toUpperCase(), d.get("Diag_type".toUpperCase()));
                tempHash.put("icdName".toUpperCase(), ((String) d.get("Icd_name".toUpperCase())).replace("'", "’"));
                tempHash.put("transBl".toUpperCase(), d.get("Trans_bl".toUpperCase()));
                tempHash.put("transX".toUpperCase(), d.get("Trans_x".toUpperCase()));
                tempHash.put("transCount".toUpperCase(), d.get("Trans_count".toUpperCase()));
                tempHash.put("admissThing".toUpperCase(), d.get("Admiss_thing".toUpperCase()));

                tempList.add(tempHash);
                for (int j = 2; j <= 19; j++) {
                    tempHash = new HashMap();
                    if (d.size() > 0 && StringUtil.isNotEmpty(String.valueOf(d.get("Icd_name".toUpperCase() + j)))) {
                        tempHash.put("patientId".toUpperCase(), d.get("Patient_id".toUpperCase()));
                        tempHash.put("visitId".toUpperCase(), d.get("Visit_id".toUpperCase()));
                        tempHash.put("icdcode".toUpperCase(), d.get("icdcode".toUpperCase() + j) == null ? "" : String.valueOf(d.get("icdcode" + j)));
                        tempHash.put("transCode".toUpperCase(), d.get("Trans_code".toUpperCase() + j).toString().trim() == "1" ? "2" : d.get("Trans_code".toUpperCase() + j).toString());
                        tempHash.put("transCodeXh".toUpperCase(), d.get("Trans_code_xh".toUpperCase() + j) == null ? "" : d.get("Trans_code_xh".toUpperCase() + j).toString());
                        tempHash.put("diagType".toUpperCase(), d.get("Diag_type".toUpperCase() + j) == null ? "" : d.get("Diag_type".toUpperCase() + j).toString());
                        tempHash.put("transBl".toUpperCase(), d.get("Trans_bl".toUpperCase() + j) == null ? "" : d.get("Trans_bl".toUpperCase() + j).toString());
                        tempHash.put("transX".toUpperCase(), d.get("Trans_x".toUpperCase() + j) == null ? "" : d.get("Trans_x".toUpperCase() + j).toString());
                        tempHash.put("transCount".toUpperCase(), d.get("Trans_count".toUpperCase() + j) == null ? "" : d.get("Trans_count".toUpperCase() + j).toString());
                        tempHash.put("admissThing".toUpperCase(), d.get("Admiss_thing".toUpperCase() + j) == null ? "" : d.get("Admiss_thing".toUpperCase() + j).toString());
                        tempHash.put("icdName".toUpperCase(), (String.valueOf(d.get("Icd_name".toUpperCase() + j))).replace("'", "’"));
                        tempList.add(tempHash);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return tempList;
    }


    public static List<Map<String, Object>> convertOperateListToArrayList(List<Map<String, Object>> ls)
            throws Exception {
        ArrayList tempList = new ArrayList();
        HashMap tempHash = null;

        for (int i = 0; i < ls.size(); i++) {
            Map<String, Object> d = ls.get(i);
            tempHash = new HashMap();
            if (d.get("Operate_name".toUpperCase()) != "") {
                tempHash.put("patientId".toUpperCase(), d.get("Patient_id".toUpperCase()));
                tempHash.put("visitId".toUpperCase(), d.get("Visit_id".toUpperCase()));
                tempHash.put("operateNo".toUpperCase(), d.get("Operate_no".toUpperCase()));
                tempHash.put("operateIcd".toUpperCase(), d.get("Operate_icd".toUpperCase()));
                tempHash.put("operateDate".toUpperCase(), d.get("Operate_date".toUpperCase()));
                tempHash.put("operateName".toUpperCase(), String.valueOf(d.get("Operate_name".toUpperCase())).replace("'", "’"));
                tempHash.put("operateDoctor1".toUpperCase(), d.get("Zoperatedoctor".toUpperCase()));
                tempHash.put("operateDoctor2".toUpperCase(), d.get("Foperatedoctor".toUpperCase()));
                tempHash.put("operateDoctor3".toUpperCase(), d.get("Ffoperatedoctor".toUpperCase()));
                tempHash.put("operateMz".toUpperCase(), d.get("Operate_mz".toUpperCase()));
                tempHash.put("operateQk".toUpperCase(), d.get("Operate_qk".toUpperCase()));
                tempHash.put("operateDoctorMz".toUpperCase(), d.get("Operate_doctor_mz".toUpperCase()));
                tempHash.put("mzFlag".toUpperCase(), d.get("Mz_flag".toUpperCase()));
                tempHash.put("id".toUpperCase(), d.get("Id".toUpperCase()));
                tempHash.put("operateType".toUpperCase(), d.get("Operate_type".toUpperCase()));
                tempHash.put("isFirst".toUpperCase(), d.get("Is_first".toUpperCase()));
                tempHash.put("surgeryOrOperate".toUpperCase(), d.get("Surgery_or_operate".toUpperCase()));
                tempList.add(tempHash);

                for (int j = 2; j <= 10; j++) {
                    tempHash = new HashMap();
                    if (d.size() > 0 && StringUtil.isNotEmpty(String.valueOf(d.get("Operate_name".toUpperCase() + j)))) {
                        tempHash.put("patientId".toUpperCase(), d.get("Patient_id".toUpperCase()));
                        tempHash.put("visitId".toUpperCase(), d.get("Visit_id".toUpperCase()));
                        tempHash.put("operateNo".toUpperCase(), d.get("Operate_no".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateIcd".toUpperCase(), d.get("Operate_icd".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateDate".toUpperCase(), d.get("Operate_date".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateName".toUpperCase(), d.get("Operate_name".toUpperCase() + j)
                                .toString().replace("'", "’"));
                        tempHash.put("operateDoctor1".toUpperCase(), d.get("Zoperatedoctor".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateDoctor2".toUpperCase(), d.get("Foperatedoctor".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateDoctor3".toUpperCase(), d.get("Ffoperatedoctor".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateMz".toUpperCase(), d.get("Operate_mz".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateQk".toUpperCase(), d.get("Operate_qk".toUpperCase() + j)
                                .toString());
                        tempHash.put("operateDoctorMz".toUpperCase(), d.get("Operate_doctor_mz".toUpperCase() + j)
                                .toString());
                        tempHash.put("mzFlag".toUpperCase(), d.get("Mz_flag".toUpperCase() + j)
                                .toString());
                        tempHash.put("id".toUpperCase(), d.get("Id".toUpperCase() + j).toString());
                        tempHash.put("operateType".toUpperCase(), d.get("Operate_type".toUpperCase() + j)
                                .toString());
                        tempHash.put("isFirst".toUpperCase(), d.get("Is_first".toUpperCase() + j)
                                .toString());
                        tempHash.put("surgeryOrOperate".toUpperCase(), d.get("Surgery_or_operate".toUpperCase() + j)
                                .toString());
                        tempList.add(tempHash);
                    }

                }
            }
        }
        return tempList;
    }
}

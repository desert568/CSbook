/**
 * 全局设置ajax请求后的操作，如果session已经超时 则获取filter中的参数进行跳转
 */
$.ajaxSetup({
    complete:function(d){
        if ("REDIRECT" == d.getResponseHeader("REDIRECT")){
            window.top.location.href = d.getResponseHeader("CONTENTPATH");
        }
    }
})

wtx = window.wtx || {};

/**
 * form表单数据转json
 * @param formID
 * @returns {*}
 */
wtx.formToJson = function(formID){
    var data = $("#" + formID).serialize();
    var $chks = $("#" + formID).find(":checkbox:not(:checked)");
    if($chks.length == 0) {
        return data;
    }
    var nameArr = [];
    var tempStr = "";
    $chks.each(function() {
        var chkName = $(this).attr("name");
        if($.inArray(chkName, nameArr) == -1 && $("#" + formID).find(":checkbox[name='" + chkName + "']:checked").length == 0) {
            nameArr.push(chkName);
            tempStr += "&" + chkName + "=";
        }
    });
    data += tempStr;
    data = decodeURIComponent(data, true);
    data = data.replace(/&/g, "\",\"");
    data = data.replace(/=/g, "\":\"");
    data = "{\"" + data + "\"}";
    data = eval("(" + data + ")");
    return data;
}

/**
 * 获取表单数据
 * @param formID
 * @returns {*}
 */
wtx.getFormValue = function(formID){
    var formData = {};
    var data = wtx.formToJson(formID);
    if(data != '' && data != null) {
        //判断是不是Json格式
        if(typeof(data) == "object" && Object.prototype.toString.call(data).toLowerCase() == "[object object]" && !data.length){
            return  data;
        }
        var objArray = data.split("&");
        $.each(objArray, function(i, o) {
            if(o != undefined && o != '') {
                var paramArray = o.split("=");
                var key = decodeURIComponent(paramArray[0], true);
                var val = decodeURIComponent(paramArray[1], true);
                formData[key] = $.trim(val);
            }
        });
    }
    return formData || {};
}

/**
 * 设置表单数据
 * @param formID
 * @param obj
 */
wtx.setFormValue = function(formID, obj) {
    var form = $("#" + formID);
    if(obj==null){
        return;
    }
    $.each(obj, function (name, val) {
        var formElement = form.find("[name='" + name + "']");
        if (val == 0){  //js中0会标记为false
            val = val + "";
        }
        if (val != "" && formElement != undefined && formElement[0] != undefined && val != null) {

            var tagType = formElement[0].tagName.toLowerCase(); //toUpperCase
            if (tagType == 'input') {
                var inputType = formElement.attr("type");
                val = (val + "").trim();

                if (inputType == "text" || inputType == "hidden" || inputType == "password" || inputType == "number") {
                    if ($(formElement).hasClass("time")) {
                        val = wtx.dateFtt(val,"yyyy-MM-dd hh:mm:ss");
                    }
                    $("[name='" + name + "']").val(wtx.isNull(val));
                } else if (inputType == "radio") {
                    $("input[type=radio][name='" + name + "'][value='" + val + "']").attr("checked", true);
                } else if (inputType == "checkbox") {
                    var vals = val.split(",");
                    for (var i = 0; i < vals.length; i++) {
                        $("input[type=checkbox][name='" + name + "'][value='" + vals[i] + "']").attr("checked", true);
                    }
                }
            } else if (tagType == "select") {
                formElement.val(val);
            } else if (tagType == "textarea") {
                formElement.html(val);
            } else {
                formElement.html(val);
            }
        }
    });
}

/** @Desc 空字符串替换
  * @author liub
  * @Date 2019/3/6 17:00
  */
wtx.isNull = function (v) {
    if (v == null) {
        return "";
    }
    return v;
}

/** @Desc 空字符串替换
 * @author cp
 * @Date 2019/3/6 17:00
 */
wtx.isNullForZero = function (v) {
    if (v == null) {
        return 0;
    }
    return v;
}
/**
 * 重置表单
 * @param formID
 */
wtx.resetForm = function(formID) {
    $(':input', '#' + formID)
        .not(':button, :submit, :reset')
        .val('')
        .removeAttr('checked')
        .removeAttr('selected');
}
/**
 * jsTree 树加载
 * @param modal 对象
 * @param dataUrl 数据请求URL
 * @param param 参数
 * @param plugins 插件
 * @param callback 点击回调方法
 */
wtx.initSelectTree = function (modal, dataUrl, param, plugins, callback) {
    plugins = plugins || [];
    param = param || {};
    if (!modal) {
        wtx.error("modal为空");
        return;
    }
    if (!dataUrl) {
        wtx.error("dataUrl为空");
        return;
    }
    $(modal).data('jstree', false).empty().jstree({
        'core': {
            'data': function (obj, cb) {
                //  获取节点数据信息
                var id = obj.id;
                //  处理根节点
                if (id === '#') {
                    //  根节点请求节点数据
                    wtx.post(dataUrl, {text: param.text}, function (data) {
                        if (data && data[0]) {
                            cb.call(this, data);
                        } else {
                            $(modal).html("暂无相关数据");
                        }
                    }, function (err) {
                        wtx.error(err);
                    });
                } else {
                    //  请求节点数据
                    wtx.post(dataUrl, {id: id}, function (data) {
                        if (data) {
                            cb.call(this, data);
                        }
                    }, function (err) {
                        wtx.error(err);
                    });
                }
            }
        },
        "plugins": plugins
    });
    if (callback) {
        $(modal).on("select_node.jstree", function (e, data) {
            callback(e, data);
        });
    }
}
/**
 * 身份证校验
 * @param idCard
 * @returns {boolean}
 */
wtx.checkIdCard=function(idCard) {
    // 1 "验证通过!", 0 //校验不通过
    var format = /^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\d{4}(([1][9]\d{2})|([2]\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\d{3}[0-9xX]$/;
    //号码规则校验
    if(!format.test(idCard)){
        return false;
    }
    //区位码校验
    //出生年月日校验   前正则限制起始年份为1900;
    var year = idCard.substr(6,4),//身份证年
        month = idCard.substr(10,2),//身份证月
        date = idCard.substr(12,2),//身份证日
        time = Date.parse(month+'-'+date+'-'+year),//身份证日期时间戳date
        now_time = Date.parse(new Date()),//当前时间戳
        dates = (new Date(year,month,0)).getDate();//身份证当月天数
    if(time>now_time||date>dates){
        return false;
    }
    //校验码判断
    var c = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2);   //系数
    var b = new Array('1','0','X','9','8','7','6','5','4','3','2');  //校验码对照表
    var id_array = idCard.split("");
    var sum = 0;
    for(var k=0;k<17;k++){
        sum+=parseInt(id_array[k])*parseInt(c[k]);
    }
    if(id_array[17].toUpperCase() != b[sum%11].toUpperCase()){
        return false;
    }
    return true;
}

/**
 * 根据身份证获取性别名称
 * @param idcard
 * @returns {string}
 */
wtx.getSexByIdcard = function(idcard) {
    if (idcard) {
        if (parseInt(idcard.substr(16, 1)) % 2 == 1) {
            return "男";
        } else {
            return "女";
        }
    }
    return "";
}

/**
 * 根据身份证获取性别代码
 * @param idcard
 * @returns {string}
 */
wtx.getSexCodeByIdcard = function (idcard) {
    if (idcard) {
        if (parseInt(idcard.substr(16, 1)) % 2 == 1) {
            return "1";
        } else {
            return "2";
        }
    }
    return "";
}

/**
 * 根据身份证获取年龄
 * @param idcard
 * @returns {number}
 */
wtx.getAgeByIdCard = function(idcard) {
    if(!idcard){
        return 0;
    }
    var myDate = new Date();
    var age = myDate.getFullYear() - Number(idcard.substring(6, 10));
    return Number(age);
}

/**
 * 根据出生日期获取年龄
 * @param strBirthday
 * @returns {number}
 */
wtx.getAgeByBirth = function (strBirthday){
    var returnAge;
    var strBirthdayArr=strBirthday.split("-");
    var birthYear = strBirthdayArr[0];
    var birthMonth = strBirthdayArr[1];
    var birthDay = strBirthdayArr[2];

    var d = new Date();
    var nowYear = d.getFullYear();
    var nowMonth = d.getMonth() + 1;
    var nowDay = d.getDate();

    if(nowYear == birthYear){
        returnAge = 0;//同年 则为0岁
    }else {
        var ageDiff = nowYear - birthYear ; //年之差
        if(ageDiff > 0) {
            if(nowMonth == birthMonth) {
                var dayDiff = nowDay - birthDay;//日之差
                if(dayDiff < 0) {
                    returnAge = ageDiff - 1;
                } else {
                    returnAge = ageDiff ;
                }
            }
            else {
                var monthDiff = nowMonth - birthMonth;//月之差
                if(monthDiff < 0) {
                    returnAge = ageDiff - 1;
                }else {
                    returnAge = ageDiff ;
                }
            }
        }
        else {
            returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
        }
    }
    return returnAge;//返回周岁年龄
}

/**
 * 检查固定电话号码格式是否正确
 * @param tel
 * @returns {boolean}
 */
wtx.checkTel = function(tel){
    var reg = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/;
    if (!reg.test(tel)) {
        return false;
    } else {
        return true;
    }
}

/**
 * 检查手机号是否正确
 * @param phone
 * @returns {boolean}
 */
wtx.checkPhone = function(phone){
    var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(phone)) {
        return false;
    } else {
        return true;
    }
}

/**
 *  校验邮箱地址是否正确
 * @param mail
 * @returns {boolean}
 */
wtx.checkMail = function(mail){
    var reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
    if (!reg.test(mail)) {
        return false;
    } else {
        return true;
    }
}

/**
 * 格式化时间
 * @param d
 * @param fmt
 * @returns {*}
 */
wtx.dateFtt = function (d, fmt) {
    if (d == null || d == "") {
        return "";
    }
    var date = new Date(d);
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 获取当前时间
 * @returns {string}
 */
wtx.getNowFormatDate = function(){
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    //分钟数不足十分钟补零，对应weui日期控件
    var minutes = (Number(date.getMinutes()) > 9 ? date.getMinutes() : "0" + date.getMinutes());
    var hours = (Number(date.getHours()) > 9 ? date.getHours() : "0" + date.getHours());
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + hours + seperator2 + minutes;
    return currentdate;
}

/**
 * 发送ajax get请求
 * @param url
 * @param params
 * @param success
 * @param error
 * @param async  是否异步 默认为true
 */
wtx.get = function (url, params, success, error, async) {
    wtx.ajax({
        url: url,
        type: 'get',
        data: params,
        async : async,
        success: success,
        error: error
    });
}

/**
 * 发送ajax post请求
 * @param url
 * @param params
 * @param success
 * @param error
 * @param async  是否异步 默认为true
 */
wtx.post = function (url, params, success, error, async) {
    wtx.ajax({
        url: url,
        type: 'post',
        data: params,
        async : async,
        success: success,
        error: error
    });
}

/**
 * 发送ajax请求
 * @param options
 */
wtx.ajax = function (options) {
    var _async = options.async;
    var isJson = options.isJson;
    var _contentType;
    if (_async == undefined){
        _async = true;
    }
    if (isJson == undefined){
        _contentType = 'application/x-www-form-urlencoded';
    }else{
        if(isJson){
            _contentType = 'application/json;charset=utf-8';
        }else{
            _contentType = 'application/x-www-form-urlencoded';
        }
    }
    if (typeof options === 'object') {
        var index = layer.load(0);
        $.ajax({
            contentType: _contentType,
            type: options.type,
            url: options.url,
            dataType: "json",
            async : _async,
            timeout: options.timeout ? options.timeout : 6000000,
            data: options.data ? options.data : {},
            success: function (d) {
                layer.close(index);
                if (d.success) {
                    if (options.success) {
                        options.success(d.list, d.totalCount);
                    }
                } else {
                    var message = "";
                    if (d.message) {
                        message = d.message;
                    } else {
                        message = "服务器返回未知错误！";
                    }
                    if (options.error) {
                        wtx.errorAlert(message);
                    } else {
                        wtx.errorAlert(d.message);
                    }
                }
            },
            error : function (e, t) {
                var responseText = e.responseText;
                if (responseText && responseText.indexOf("window.open") > 0){
                    wtx.error("登录超时，需重新登录!", function () {
                        var href = responseText.split("('")[1];
                        href = href.split("',")[0];
                        window.open(href, '_top');
                    });
                } else {
                    if ("timeout" == t){
                        wtx.errorAlert("服务器超时");
                    } else {
                        wtx.errorAlert(t);
                    }
                }
            }
        })
    }
}

/**
 * 替换null undefined值为空字符串
 * @param value
 * @returns {*}
 */
wtx.replaceNULL = function(value){
    if (value == null || value == undefined){
        return "";
    }
    return value;
}

var layer;
var layuiTable = "layuiTable";
var searchForm = "searchForm";
layui.use(['layer', 'laydate'], function(){
    layer = layui.layer;
});

/**
 * 初始化表格
 * @param table
 * @param options
 */
wtx.tableRender = function(table, options){
    var index = layer.load(0);
    var height = $(window).height() - $(".layui-elem-quote").height() - $(".layui-tab-title").height() - 48;
    if (options.page == false){
        height = '';
    }
    var layuiOptions = {
        height: height,
         page: true //开启分页
        ,limit : 30
        ,limits : [30, 50, 100]
        ,text : {
            none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
        }
        ,done : function(res){
            if (res.list.length == 0){
                $(".layui-table-total").remove();
            }
            layer.close(index);
        }
        ,request:{
            pageName : 'pageIndex'
            ,limitName : 'pageSize'
        }
        ,response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            ,statusCode: 0 //规定成功的状态码，默认：0
            ,msgName: 'message' //规定状态信息的字段名称，默认：msg
            ,countName: 'totalCount' //规定数据总数的字段名称，默认：count
            ,dataName: 'list' //规定数据列表的字段名称，默认：data
        }
        ,even : false	//开启隔行显示
    };
    jQuery.extend(layuiOptions, options);
    table.render(layuiOptions);
}

/**
 * 刷新表格
 * @param tableID
 * @param formID
 */
wtx.reload = function(tableID, formID){
    var _tableID = tableID ? tableID : layuiTable;
    var _formID = formID ? formID : searchForm;
    layui.use('table', function(){
        var table = layui.table;
        var index = layer.load(0);
        //执行重载
        table.reload(_tableID, {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            ,where:  wtx.getFormValue(_formID)
            ,done : function(){
                layer.close(index);
            }
        });
    })
}

/**
 * 重新渲染表单
 */
wtx.renderForm = function(){
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
};

var dateStart = {
    elem: null,
    max: 0,
    index : 1,
    type : 'date',
    value : new Date(),
    done: function(value, date, endDate){
        var max = dateStart.max;
        if (max == 0){
            max = new Date();
        }
        if (dateStart.type == 'year'){
            max = wtx.dateFtt(max, 'yyyy');
        } else if (dateStart.type == 'month'){
            max = wtx.dateFtt(max, 'yyyy-MM');
        } else if (dateStart.type == 'date' || options.type == null){
            max = wtx.dateFtt(max, 'yyyy-MM-dd');
        } else {
            max = wtx.dateFtt(max, 'yyyy-MM-dd hh:mm:ss');
        }
        if (value > max){
            value = max;
            $(dateStart.elem).val(max);
        } else if (value < dateStart.min){
            value = dateStart.min;
            $(dateStart.elem).val(dateStart.min);
        } else {
            $(dateStart.elem).val(value);
        }
        dateStart.value = value;
        var options = dateEnd;
        if(options.elem != null){
            if(value != null && value != ''){
                var cal = $(options.elem);
                var date = cal.parent().html();
                cal.after(date);
                cal.next().attr('lay-key','');
                cal.next().val(cal.val());
                var $dom = cal.next();
                cal.remove();
                var dom = $dom[0];
                options.elem = dom;
                options.min = value;
                layui.laydate.render(options);
            }
        }

    }
    ,change: function(value, date, endDate){
        $('#layui-laydate' + dateStart.index).remove();//删除
        this.done(value, date);
    }
}
var dateEnd = {
    elem: null,
    max: 0,
    index : 2,
    type : 'date',
    value : new Date(),
    done: function(value, date, endDate){
        var max = dateEnd.max;
        if (max == 0){
            max = new Date();
        }
        if (dateEnd.type == 'year'){
            max = wtx.dateFtt(max, 'yyyy');
        } else if (dateEnd.type == 'month'){
            max = wtx.dateFtt(max, 'yyyy-MM');
        } else if (dateEnd.type == 'date' || options.type == null){
            max = wtx.dateFtt(max, 'yyyy-MM-dd');
        } else {
            max = wtx.dateFtt(max, 'yyyy-MM-dd hh:mm:ss');
        }
        if (value > max){
            value = max;
            $(dateEnd.elem).val(max);
        } else if (value < dateEnd.min){
            value = dateEnd.min;
            $(dateEnd.elem).val(dateEnd.min);
        } else {
            $(dateEnd.elem).val(value);
        }
        dateEnd.value = value;
        var options = dateStart;
        if(options.elem != null){
            if(value != null && value != ''){
            var cal = $(options.elem);
            var date = cal.parent().html();
            cal.after(date);
            cal.next().attr('lay-key','');
            cal.next().val(cal.val());
            var $dom = cal.next();
            cal.remove();
            var dom = $dom[0];
            options.elem = dom;
            options.max = value;
            layui.laydate.render(options);}}
    }
    ,change: function(value, date, endDate){
        $('#layui-laydate' + dateEnd.index).remove();//删除
        this.done(value, date);
    }
};


var dateStart2 = {
    elem: null,
    max: 0,
    index : 3,
    type : 'date',
    value : new Date(),
    done: function(value, date, endDate){
        var max = dateStart2.max;
        if (max == 0){
            max = new Date();
        }
        if (dateStart2.type == 'year'){
            max = wtx.dateFtt(max, 'yyyy');
        } else if (dateStart2.type == 'month'){
            max = wtx.dateFtt(max, 'yyyy-MM');
        } else if (dateStart2.type == 'date' || options.type == null){
            max = wtx.dateFtt(max, 'yyyy-MM-dd');
        } else {
            max = wtx.dateFtt(max, 'yyyy-MM-dd hh:mm:ss');
        }
        if (value > max){
            value = max;
            $(dateStart2.elem).val(max);
        } else if (value < dateStart2.min){
            value = dateStart2.min;
            $(dateStart2.elem).val(dateStart2.min);
        } else {
            $(dateStart2.elem).val(value);
        }
        dateStart2.value = value;
        var options = dateEnd2;
        if(options.elem != null){
            if(value != null && value != ''){
                var cal = $(options.elem);
                var date = cal.parent().html();
                cal.after(date);
                cal.next().attr('lay-key','');
                cal.next().val(cal.val());
                var $dom = cal.next();
                cal.remove();
                var dom = $dom[0];
                options.elem = dom;
                options.min = value;
                layui.laydate.render(options);
            }
        }

    }
    ,change: function(value, date, endDate){
        $('#layui-laydate' + dateStart2.index).remove();//删除
        this.done(value, date);
    }
}
var dateEnd2 = {
    elem: null,
    max: 0,
    index : 4,
    type : 'date',
    value : new Date(),
    done: function(value, date, endDate){
        var max = dateEnd2.max;
        if (max == 0){
            max = new Date();
        }
        if (dateEnd2.type == 'year'){
            max = wtx.dateFtt(max, 'yyyy');
        } else if (dateEnd2.type == 'month'){
            max = wtx.dateFtt(max, 'yyyy-MM');
        } else if (dateEnd2.type == 'date' || options.type == null){
            max = wtx.dateFtt(max, 'yyyy-MM-dd');
        } else {
            max = wtx.dateFtt(max, 'yyyy-MM-dd hh:mm:ss');
        }
        if (value > max){
            value = max;
            $(dateEnd2.elem).val(max);
        } else if (value < dateEnd2.min){
            value = dateEnd2.min;
            $(dateEnd2.elem).val(dateEnd2.min);
        } else {
            $(dateEnd2.elem).val(value);
        }
        dateEnd2.value = value;
        var options = dateStart2;
        if(options.elem != null){
            if(value != null && value != ''){
                var cal = $(options.elem);
                var date = cal.parent().html();
                cal.after(date);
                cal.next().attr('lay-key','');
                cal.next().val(cal.val());
                var $dom = cal.next();
                cal.remove();
                var dom = $dom[0];
                options.elem = dom;
                options.max = value;
                layui.laydate.render(options);}}
    }
    ,change: function(value, date, endDate){
        $('#layui-laydate' + dateEnd2.index).remove();//删除
        this.done(value, date);
    }
};

/**
 * 弹框
 * @param options
 */

wtx.openWin = function(title, url,area,type, tableID, formID, isInner){
    var _tableID = tableID ? tableID : layuiTable;
    var _formID = formID ? formID : searchForm;
    var _area = area ? area : ['800px', '500px'];
    var _type = type ? type : 2;
    var flag = 0;
    var winOptions = {
        shadeClose: false, //点击弹框区域外的地方是否可关闭弹框
        shade: 0.8,
        type: _type,
        title : title,
        area: _area, //宽高
        content: url,
        closeBtn : 1,
        maxmin :true,//最大化
        cancel:function(){
            flag = 1;
            wtx.closeWin();
        },
        /* //skin: 'layui-layer-rim', //加上边框*/
        end: function () {
            if(flag == 0){
                if (title == '新增'){
                    location.reload();
                } else {
                    wtx.reload(_tableID, _formID);
                }
            }
        }
    }
    //if (isInner){
        layer.open(winOptions);
    //} else {
        //parent.parent.layer.open(winOptions);
    //}
}

wtx.closeWin = function(){
    /*var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);*/
    var index = layer.getFrameIndex(window.name);
    layer.close(index);
}
wtx.cancel = function () {
    parent.$(".layui-layer-close").click();
}

/*wtx.cancel = function () {
    parent.$(".layui-layer-close").click();
}*/

wtx.msg = function(msg, callBack) {
    layer.msg(msg, {}, callBack);
}

wtx.msg = function(msg){
    layer.msg(msg);
}

wtx.alert = function(msg, callBack){
    var option = {
        icon : 1,
        time : 1000
    }
    layer.alert(msg, option, callBack);
}

wtx.warnning = function(msg){
    var option = {
        icon : 0,
        title : '警告'
    }
    layer.alert(msg, option);
}

wtx.success = function(msg, callBack){
    var option = {icon : 1, time : 2000};
    layer.msg(msg, option, callBack);
}

wtx.errorAlert = function(msg){
    var option = {
        icon : 2,
        title : '错误'
    }
    layer.alert(msg, option);
}

wtx.error = function(msg, callBack){
    var option = {icon : 2}
    layer.msg(msg, option, callBack);
}

wtx.errorMsg = function(msg){
    var option = {icon : 2, time : 1000};
    layer.msg(msg, option);
}

wtx.confirm = function(msg, callBack){
    var option = {
        icon : 3,
        title : '提示'
    }
    layer.confirm(msg, option, function(index) {
        callBack();
        layer.close(index);
    });
}

wtx.initSelect = function(domID, url, params, value, title){
    var index = layer.load(0);
    var _title = title ? title : '请选择';
    wtx.get(url, params, function(d){
        var html = '';
        $.each(d, function(i, o){
            html += '<option value="' + o.value + '">' + o.title + '</option>'
        })
        if (html != ''){
            $("#" + domID).html('<option value="">'+ _title +'</option>' + html);
        }
        $("#" + domID).val(value);
        //重新渲染表单
        wtx.renderForm();
        layer.close(index);

    })
}


/**
 * 修改window.open的开窗口后显示参数的问题
 * @param url
 * @returns
 */
wtx.windowOpen = function(url){
    var action, params;
    var newWin = window.open();
    if (url.indexOf("?") > -1){
        action = url.split("?")[0];
        params = url.split("?")[1];
    } else {
        action = url;
    }

    //创建form
    var tempForm = document.createElement("form");
    //设置为post提交
    tempForm.method = "post";
    tempForm.action = action;

    if (params){
        //设置参数
        var paramsArr = params.split("&");
        for (var i = 0; i < paramsArr.length; i++){
            var inputName = paramsArr[i].split("=")[0];
            var inputValue = paramsArr[i].split("=")[1];
            //创建input标签，用来设置参数
            var hideInput = document.createElement("input");
            hideInput.type="hidden";
            hideInput.name= inputName;
            hideInput.value= inputValue;
            //将input添加到form中
            tempForm.appendChild(hideInput);
        }
    }

    //将此form表单添加到页面主体body中
    newWin.document.body.appendChild(tempForm);
    //手动触发，提交表单
    tempForm.submit();
    //从body中移除form表单
    newWin.document.body.removeChild(tempForm);
}

/**
 * 格式化状态
 * @param text 要现在的文字
 * @param color 十六进制背景颜色(如'#ffffff')
 * @returns {string}
 */
wtx.fmtStatus = function(text, cls){
    return '<span class="layui-btn layui-btn-xs '+ cls +'">'+ text +'</span>';
}

/**
 * 将Integer格式的时间 格式成固定形式
 * @param text
 */
wtx.fmtDateByInteger = function(date){
    date = date + "";
    if (date.length >= 8){
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8)
    } else if (date.length >= 6 && date.length < 8) {
        return date.substring(0, 4) + "-" + date.substring(4, 6)
    } else if (date.length >= 4 && date.length < 6){
        return date.substring(0, 4)
    } else {
        return date;
    }
}

/**
 * cookie的操作辅助类
 * @type {{get: Window.wtx.cookie.get}}
 */
wtx.cookie = {
    get:function(key){//获取cookie方法
        /*获取cookie参数*/
        var getCookie = document.cookie.replace(/[ ]/g,"");  //获取cookie，并且将获得的cookie格式化，去掉空格字符
        var arrCookie = getCookie.split(";")  //将获得的cookie以"分号"为标识 将cookie保存到arrCookie的数组中
        var tips;  //声明变量tips
        for(var i=0;i<arrCookie.length;i++){   //使用for循环查找cookie中的tips变量
            var arr=arrCookie[i].split("=");   //将单条cookie用"等号"为标识，将单条cookie保存为arr数组
            if(key==arr[0]){  //匹配变量名称，其中arr[0]是指的cookie名称，如果该条变量为tips则执行判断语句中的赋值操作
                tips=arr[1];   //将cookie的值赋给变量tips
                break;   //终止for循环遍历
            }
        }
        return tips;
    }
};
/** @Desc   FormSelects初始化插件
 * @author liub
 * @Date 2019/2/28 8:51
 * @param  xm_select对应select控件中xm-select值
 */
wtx.formSelects = function (xm_select,url,parmas,searchName,options) {
    var FormSelectsOptions= {
        searchUrl: url,
        searchName: searchName?searchName:xm_select,  //自定义搜索内容的key值
        keyChildren: 'children',    //联动多选自定义children
        success: function(id, url, searchVal, result){      //使用远程方式的success回调
            //当有搜索值时不写入默认值
            if(parmas && !searchVal){
                layui.formSelects.value(xm_select, parmas); //parmas为数组
            }
            //解决搜索数据加载后 搜索值被清空的问题
            if (searchVal){
                $("div[fs_id='"+ xm_select +"']").find(".xm-select-input").val(searchVal);
                $("div[fs_id='"+ xm_select +"']").find(".xm-select-input").focus();
            }
        },
        delay: 5000, //搜索延迟时间, 默认停止输入1000ms后开始搜索
        //以下代码为默认加载远程数据
        beforeSuccess: function (id, url, searchVal, result) {
            result = result.list;
            //然后返回数据
            return result;
        },
        beforeSearch: function(id, url, searchVal){         //搜索前调用此方法, return true将触发搜索, 否则不触发
            if(!searchVal){//如果搜索内容为空,就不触发搜索
                return false;
            }
            return true;
        },
        clearInput: false,          //当有搜索内容时, 点击选项是否清空搜索内容, 默认不清空
    };
    jQuery.extend(FormSelectsOptions, options);
    layui.formSelects.config(xm_select,FormSelectsOptions).data(xm_select, 'server', {
        url : url
    });
};
/**
 *  下拉框单选 (带回显)
 * @param xm_select
 * @param url  接口请求地址
 * @param parmas //回显的item的value ,使用[]形式
 * @param options
 */
wtx.formSingleSelect = function (xm_select,url,parmas,options) {
    var FormSelectsOptions= {
        //searchUrl: url,
        success: function(id, url, searchVal, result){      //使用远程方式的success回调
            if(parmas){
                layui.formSelects.value(xm_select, parmas); //parmas为数组
            }
        },

        //以下代码为默认加载远程数据
        beforeSuccess: function (id, url, searchVal, result) {
            result = result.list;
            //然后返回数据
            return result;
        },
        clearInput: true
    };
    jQuery.extend(FormSelectsOptions, options);
    layui.formSelects.config(xm_select,FormSelectsOptions).data(xm_select, 'server', {
        url : url
    });
}

/** @Desc   新增tab页面
 * @author liub
 * @Date 2019/3/4 17:57
 */
wtx.addTabs = function (id,title,url) {
    parent.addTabs({id:id,title:title,url: url,close:true});
}

/** @Desc  获取Url上指定参数的值
 * @author liub
 * @Date 2019/3/4 18:38
 */
wtx.getParamValueFromURL = function (param) {
    var url = window.location.href;
    if (url.indexOf("?") > 0) {
        var result = url.split("?")[1];
        var keyValue = result.split("&");
        var result = "";
        for (var i = 0; i < keyValue.length; i++) {
            var item = keyValue[i].split("=");
            if (item[0] == param) {
                result = item[1];
                break;
            }
        }
        return result;
    }
    return "";
}

/** @Desc  获取tab菜单子页面的所有参数(iframe)
 * @author znq
 * @param id tab的id (列如 #tab_syzkfxgl-ys iframe)
 * @Date
 */
wtx.getIframeUrlParams = function (id) {
    var orginParams = {};
    var win = parent.document.querySelector(id);
    if (!win){
        return orginParams;
    }
    var url = win.contentWindow.location.href;

    if (url.indexOf("?") > 0) {
        var result = url.split("?")[1];
        var keyValue = result.split("&");
        for (var i = 0; i < keyValue.length; i++) {
            var item = keyValue[i].split("=");
            orginParams[item[0]] =item[1];
        }
        return orginParams;
    }
    return orginParams;
};

/** @Desc  整数转成百分比
 * @author csx
 * @Date
 */
wtx.toPercent = function  (num) {
    return (Math.round(num * 10000)/100).toFixed(2) + '%';
}

/** @Desc  替换特殊字符
 * @author yanghaitao
 * @Date 2019/3/4 18:38
 */
wtx.replaceSpecialChar = function replaceSpecialChar(value){
    value= value.replace(/[\-\_\,\!\|\~\`\(\)\#\$\%\^\&\*\{\}\:\;\"\L\<\>\?]/g, '');// 去掉特殊字符
    return value;
}

Number.prototype.round = function(length){
    return Math.round(this * Math.pow(10, length)) / Math.pow(10, length);
}

String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, '');
}
//是否是数值型
String.prototype.isNumber = function() {
    return /^(-?\d+)(\.\d+)?$/.test(this);
}
//是否是非负数值型
String.prototype.isPosnumber = function() {
    return /^\d+(\.\d+)?$/.test(this);
}
//是否是整数
String.prototype.isInteger = function() {
    return /^-?[0-9]\d*$/.test(this);
}
//是否是非负整数
String.prototype.isPosinteger = function() {
    return /^[1-9]\d*$/.test(this);
}

wtx.enhanceFromVerify = function(form){
    form.verify({
        mustradio: function (value, item) { //单选按钮必选
            var va = $(item).find("input[type='radio']:checked").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        }
        , mustcheck: function (value, item) { //复选框必选
            var xname = $(item).attr("id")
            var va = $(item).find("input[type='checkbox']:checked").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        }
        , muststar: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (!new RegExp("^\\d+$").test(value)) {
                return $(item).attr("lay-verify-msg");
            }
        }
        , required: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (!(/[\S]+/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-required");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '必填项不能为空';
            }
        }
        , phone: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/^1\d{10}$/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-phone");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '请输入正确的手机号';
            }
        }
        , email: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-email");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '请输入正确的邮箱地址';
            }
        }
        , url: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-url");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '请输入正确的网络地址';
            }
        }
        , number: function (value, item) {
            if ((/[\S]+/.test(value)) &&(!value || isNaN(value))) {
                var attr = $(item).attr("lay-verify-msg-number");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '只能填写数字';
            }
        }
        , intNumber : function (value, item) {
            if (/[\S]+/.test(value) &&  !/^\d+$/.test(value)) {
                var attr = $(item).attr("lay-verify-msg-number");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '只能填写数字';
            }
        }
        , date: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-date");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '日期格式不正确';
            }
        }
        , identity: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/(^\d{15}$)|(^\d{17}(x|X|\d)$)/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-identity");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '请输入正确的身份证号';
            }
        }
        , money: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ((/[\S]+/.test(value)) && !(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(value))) {

                var attr = $(item).attr("lay-verify-msg-identity");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }

                attr = $(item).attr("lay-verify-msg");
                if (typeof attr !== typeof undefined && attr !== false && attr !== '') {
                    return attr;
                }
                return '请输入正确的金额';
            }
        }
    });
}

wtx.getMonthBeginDayAndEndDay = function (num, date) {
    if (!num){
        num = 0;
    }
    var result = [];
    var firstDate;
    if (!date){
        firstDate = new Date();
    } else {
        firstDate = new Date(date);
    }
    var year = firstDate.getFullYear();
    var month = firstDate.getMonth() + 1;
    var newMonth;
    if (num > 0){
        if (month == 1){
            year = year - 1;
            if (num > 0){
                newMonth = 13 - num;
            } else {
                newMonth = 12;
            }
        }
    }
    newMonth = month - num;
    //判断如果提前一年时间的时间设置 最多支持提前4年
    if (newMonth < 0){
        year --;
        newMonth += 12;
        if (newMonth < 0){
            year --;
            newMonth += 12;
            if (newMonth < 0){
                year --;
                newMonth += 12;
                if (newMonth < 0){
                    year --;
                    newMonth += 12;
                }
            }
        }
    }
    if (newMonth < 10){
        newMonth = "0" + newMonth;
    }
    var startDate = year +"-"+newMonth+"-"+"01";
    var date=new Date(startDate);
    var currentMonth=date.getMonth();
    var nextMonth=++currentMonth;
    var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
    var oneDay=1000*60*60*24;
    var lastDate =  new Date(nextMonthFirstDay-oneDay);
    var  endDate = lastDate.getFullYear()+"-"+((lastDate.getMonth()+1)<10?"0":"")+(lastDate.getMonth()+1)+"-"+(lastDate.getDate()<10?"0":"")+lastDate.getDate();
    result.push(startDate);
    result.push(endDate);
    return result;
}

/**
 * 判断是否为正数字
 * @param value
 * @returns {boolean}
 */
wtx.isNumber = function (value) {
    var reg = /^[0-9]+(.[0-9]{1,2})?$/;
    return reg.test(value);
}

/**
 * 获取当前季度开始日结束日
 */
wtx.getQuarterBeginDayAndEndDay = function () {
    var now = new Date();
    var month = now.getMonth() + 1;
    var year = now.getFullYear();
    var result = [];
    if (month == '1' || month == '2' || month == '3'){
        result.push(year + "-01-01");
        result.push(year + "-03-31");
    } else if (month == '4' || month == '5' || month == '6'){
        result.push(year + "-04-01");
        result.push(year + "-06-30");
    } else if (month == '7' || month == '8' || month == '9'){
        result.push(year + "-07-01");
        result.push(year + "-09-30");
    } else {
        result.push(year + "-10-01");
        result.push(year + "-12-31");
    }
    return result;
}

/**
 * 根据季度 获取开始日结束日
 * @param quarter
 * @param year
 * @returns {Array}
 */
wtx.getBeginDayAndEndDayByQuarter = function (quarter, year) {
    var _year;
    var result = [];
    if (!year){
        now = new Date();
        year = now.getFullYear();
    } else {
        _year = year;
    }
    if (quarter == '1'){
        result.push(_year + "-01-01");
        result.push(_year + "-03-31");
    } else if (quarter == '2'){
        result.push(_year + "-04-01");
        result.push(_year + "-06-30");
    } else if (quarter == '3'){
        result.push(_year + "-07-01");
        result.push(_year + "-09-30");
    } else {
        result.push(_year + "-10-01");
        result.push(_year + "-12-31");
    }
    return result;
}

/**
 * 获取当前属于哪一季度
 */
wtx.getNowQuarter = function () {
    var month = wtx.dateFtt(new Date(), "M");
    var quarter = 1;
    if (month < 4){
        quarter = 1;
    } if (month >=4 && month < 7){
        quarter = 2;
    } if (month >= 7 && month < 10){
        quarter = 3;
    } if (month >= 10 && month <= 12){
        quarter = 4
    }
    return quarter;
}
/**
 * 表格页 搜索条件过多 收起/展开
 */
$(".poa-right-bottom .arrow").on("click",function () {
    if($(this).find(".layui-icon").hasClass("layui-icon-up")){
        $(this).find(".layui-icon").removeClass("layui-icon-up").addClass("layui-icon-down").siblings("span").html("展开");
        $(this).parents(".pos-r-all").siblings(".layui-inline:gt(2)").hide();
    }else{
        $(this).find(".layui-icon").removeClass("layui-icon-down").addClass("layui-icon-up").siblings("span").html("收起");
        $(this).parents(".pos-r-all").siblings(".layui-inline:gt(2)").show();
    }
});
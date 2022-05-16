package com.kinghis.yyoauth.common.tag;

import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysFunc;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;

import java.util.List;
import java.util.Map;

/**
 * @Desc 权限按钮标签
 * @Author liuc
 * @Date 2019/1/29 9:38
 */
public class BtnTag extends AbstractMarkupSubstitutionElementProcessor {

    protected BtnTag(String elementName) {
        super(elementName);
    }

    @Override
    protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {
        QxUser qxUser = WtxUserInfo.getQxUser();
        Map<String,Map<String,SysFunc>> menuMap = qxUser.getFuncs();
        String menuCode = element.getAttributeValue("menuCode");//菜单编码
        String btnCode = element.getAttributeValue("btnCode");//按钮编码
        Map<String,SysFunc> funcMap = menuMap.get(menuCode);
        List<Node> nodes = null;
        if(null!=funcMap && funcMap.containsKey(btnCode)){
            //有按钮权限
            nodes = element.getChildren();
        }else {
        //没有按钮权限
            List<Node> children = element.getChildren();
            for (Node n:children) {
                element.removeChild(n);
            }
            nodes = element.getChildren();
        }
        return nodes;
    }

    @Override
    public int getPrecedence() {
        return 1000;
    }
}

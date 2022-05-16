package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.SysFuncMapper;
import com.kinghis.yyoauth.pojo.SysFunc;
import com.kinghis.yyoauth.service.SysFuncService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * @Desc 资源信息
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysFuncService")
public class SysFuncServiceImpl  implements SysFuncService {

    @Autowired
    private SysFuncMapper mapper;

    @Override
    public SysFunc getSysFunc(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysFunc> queryList(SysFunc sysFunc) {
        return this.mapper.select(sysFunc);
    }

    @Override
    public DataSet<SysFunc> queryPage(SysFunc sysFunc, Page page) {
        Example example = new Example(SysFunc.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysFunc.getName())){
        //    criteria.andLike("name","%"+sysFunc.getName()+"%");
        //}
        List<SysFunc> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysFunc sysFunc) {
        return this.mapper.insertSelective(sysFunc);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysFunc sysFunc) {
        return this.mapper.updateByPrimaryKeySelective(sysFunc);
    }

    @Override
    public int updateAll(SysFunc sysFunc) {
        return this.mapper.updateByPrimaryKey(sysFunc);
    }

    @Override
    public List<SysFunc> queryListInCodes(SysFunc sysFunc, String funcCodes) {
        Example example = new Example(SysFunc.class);
        Example.Criteria criteria = example.createCriteria();

        if(CommonUtil.isNotEmpty(sysFunc.getProjectCode())){
            criteria.andEqualTo("projectCode", sysFunc.getProjectCode());
        }
        if (CommonUtil.isNotEmpty(funcCodes)){
            String [] arr = funcCodes.split(",");
            criteria.andIn("funcCode", Arrays.asList(arr));
        }

        return this.mapper.selectByExample(example);
    }
}

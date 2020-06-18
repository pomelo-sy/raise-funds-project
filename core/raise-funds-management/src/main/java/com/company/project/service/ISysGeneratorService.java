package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.entity.SysGenerator;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aitangbao
 * @since 2020-03-20
 */
public interface ISysGeneratorService extends IService<SysGenerator> {


    IPage<SysGenerator> selectAllTables(Page page, SysGenerator vo);

    byte[] generatorCode(String[] split);
}

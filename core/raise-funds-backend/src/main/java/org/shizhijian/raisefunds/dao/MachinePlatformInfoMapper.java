package org.shizhijian.raisefunds.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.shizhijian.raisefunds.pojo.MachinePlatformInfo;

public interface MachinePlatformInfoMapper extends BaseMapper<MachinePlatformInfo>{

//    @Select("SELECT mp.id AS id  ,mp.plat_id AS platId, mp.plat_disp AS platDisp ,mp.plat_no AS platNo, mp.type_id AS typeId ,mt.type_id AS typeName  FROM mt_mach_plat_info mp LEFT JOIN mt_mach_type_info mt ON mp.type_id=mt.id WHERE mt.id=3")
//    List<Map<String, Object>> dyGetUserList(Page<Map<String,Object>> page,Integer id);
//	
}
package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.SysPermission;
import com.company.project.vo.resp.PermissionRespNode;

import java.util.List;
import java.util.Set;

public interface PermissionService extends IService<SysPermission> {

    List<SysPermission> getPermission(String userId);

    SysPermission addPermission(SysPermission vo);

    SysPermission detailInfo(String permissionId);

    void updatePermission(SysPermission vo);

    void deleted(String permissionId);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(String userId);

    List<PermissionRespNode> permissionTreeList(String userId);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode> selectAllMenuByTree(String permissionId);

}

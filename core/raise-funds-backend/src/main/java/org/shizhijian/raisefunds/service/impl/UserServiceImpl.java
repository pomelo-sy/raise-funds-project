package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.UserMapper;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {

    @Override
    public User getUserByOpenId(String openId) {
        QueryWrapper<User> opEqual = new QueryWrapper<>();
        opEqual.eq("open_id", openId);
        List<User> users = list(opEqual);
        if(users != null && users.size() > 0)
        return users.get(0);
        else return null;
    }
}

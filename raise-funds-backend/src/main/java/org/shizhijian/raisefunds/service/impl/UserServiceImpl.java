package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.UserMapper;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
}

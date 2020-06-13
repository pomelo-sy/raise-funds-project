package org.shizhijian.raisefunds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shizhijian.raisefunds.pojo.User;

public interface UserService extends IService<User> {

    User getUserByOpenId(String openId);
}

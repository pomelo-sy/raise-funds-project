package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.TokenMapper;
import org.shizhijian.raisefunds.pojo.Token;
import org.shizhijian.raisefunds.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl  extends ServiceImpl<TokenMapper, Token> implements TokenService {
}

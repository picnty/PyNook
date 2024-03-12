package com.project.pi.service;

import com.project.module.common.model.TokenInfo;

import java.util.Map;

public interface AuthPiService {

    TokenInfo login(Map<String, Object> parameters);

    TokenInfo register(Map<String, Object> parameters);

    Boolean logout(Map<String, Object> parameters);

}

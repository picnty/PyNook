package com.project.pi.controller.api.auth;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.project.pi.constant.PiRouteConstant;
import com.project.pi.service.AuthPiService;
import com.project.plugin.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(PiRouteConstant.API_BASE_PATH + "/auth")
public class AuthPiApiController {

    private static final Logger log = LoggerFactory.getLogger(AuthPiApiController.class);

    @Resource
    private AuthPiService service;

    @RequestMapping("")
    public ResponseEntity<?> index() {
        Map<String, Object> map = new LinkedHashMap<>();
        return ResponseEntity.ok(Result.success().data(map));
    }

    @RequestMapping(value = "login", method = {RequestMethod.POST})
    public Result login(HttpServletRequest request, @RequestBody Map<String, Object> parameters) {
        return Result.success().data(service.login(parameters));
    }

    @RequestMapping(value = "register", method = {RequestMethod.POST})
    public Result register(HttpServletRequest request, @RequestBody Map<String, Object> parameters) {
        return Result.success().data(service.register(parameters));
    }

    @SaCheckLogin
    @RequestMapping(value = "logout", method = {RequestMethod.POST})
    public Result logout(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> parameters) {
        return Result.success().data(service.logout(parameters));
    }

}

package com.project.pi.constant;

import com.project.module.common.constant.RouteConstant;

public interface PiRouteConstant {

    String BASE_PATH = "/pi";
    String API_BASE_PATH = RouteConstant.API_PREFIX + BASE_PATH;
    String BACKEND_BASE_PATH = RouteConstant.BACKEND_PATH + BASE_PATH;
    String BACKEND_API_BASE_PATH = RouteConstant.BACKEND_API_PREFIX + BASE_PATH;

}

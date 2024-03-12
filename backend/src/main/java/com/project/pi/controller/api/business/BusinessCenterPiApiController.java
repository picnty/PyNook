package com.project.pi.controller.api.business;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.module.common.entity.TbProduct;
import com.project.module.common.service.AuthService;
import com.project.pi.constant.PiRouteConstant;
import com.project.pi.dto.BusinessPiDTO;
import com.project.pi.service.BusinessPiService;
import com.project.pi.service.PiBusinessService;
import com.project.pi.vo.PiBusinessVO;
import com.project.plugin.result.Result;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@SaCheckLogin
@RestController
@RequestMapping(PiRouteConstant.API_BASE_PATH + "/business")
public class BusinessCenterPiApiController {

    private static final Logger log = LoggerFactory.getLogger(BusinessCenterPiApiController.class);

    @Resource
    private AuthService authService;

    @Resource
    private PiBusinessService service;

    @Resource
    private BusinessPiService businessPiService;

    @RequestMapping("")
    public ResponseEntity<?> index() {
        Map<String, Object> map = new LinkedHashMap<>();
        return ResponseEntity.ok(Result.success().data(map));
    }

    @GetMapping("getBusinessData")
    public ResponseEntity<?> getBusinessData() {
        Long userId = authService.getLoginUserId();
        PiBusinessVO result = service.getBusinessData(userId);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @PostMapping("setBusinessData")
    public ResponseEntity<?> setBusinessData(@RequestBody PiBusinessVO parameters) {
        Long userId = authService.getLoginUserId();
        PiBusinessVO result = service.setBusinessData(userId, parameters);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @GetMapping("getBusinessConfig")
    public ResponseEntity<?> getBusinessConfig() {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.getBusinessConfig(userId);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @PostMapping("setBusinessConfig")
    public ResponseEntity<?> setBusinessConfig(@RequestBody BusinessPiDTO parameters) {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.setBusinessConfig(userId, parameters);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @GetMapping("getBusinessPhotos")
    public ResponseEntity<?> getBusinessPhotos() {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.getBusinessPhotos(userId);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @PostMapping("setBusinessPhotos")
    public ResponseEntity<?> setBusinessPhotos(@RequestBody BusinessPiDTO parameters) {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.setBusinessPhotos(userId, parameters);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @GetMapping("getBusinessStamps")
    public ResponseEntity<?> getBusinessStamps() {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.getBusinessStamps(userId);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @PostMapping("setBusinessStamps")
    public ResponseEntity<?> setBusinessStamps(@RequestBody BusinessPiDTO parameters) {
        Long userId = authService.getLoginUserId();
        BusinessPiDTO result = businessPiService.setBusinessStamps(userId, parameters);
        return ResponseEntity.ok(Result.success().data(result));
    }

    @GetMapping("/getMerchantProductList")
    public ResponseEntity<?> getMerchantProductList(Page<TbProduct> page, TbProduct model, @RequestParam Map<String, Object> parameters) {
        return ResponseEntity.ok(Result.success().data(this.service.getMerchantProductList(page, model, parameters)));
    }

    @GetMapping("/getMerchantProductDetail")
    public ResponseEntity<?> getMerchantProductDetail(@RequestParam Long id) {
        return ResponseEntity.ok(Result.success().data(this.service.getMerchantProductDetail(id)));
    }

    @PostMapping("/addMerchantProduct")
    public ResponseEntity<?> addMerchantProduct(@RequestBody TbProduct model) {
        return ResponseEntity.ok(Result.success().data(this.service.addMerchantProduct(model)));
    }

    @PostMapping("/updateMerchantProduct")
    public ResponseEntity<?> updateMerchantProduct(@RequestBody TbProduct model) {
        return ResponseEntity.ok(Result.success().data(this.service.updateMerchantProduct(model)));
    }

    @PostMapping("/deleteMerchantProduct")
    public ResponseEntity<?> deleteMerchantProduct(@RequestBody TbProduct model) {
        return ResponseEntity.ok(Result.success().data(this.service.deleteMerchantProduct(model)));
    }

}

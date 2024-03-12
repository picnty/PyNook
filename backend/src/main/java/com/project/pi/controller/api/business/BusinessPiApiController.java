package com.project.pi.controller.api.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.pi.entity.PiBusiness;
import com.project.pi.service.PiBusinessService;
import com.project.pi.vo.PiBusinessVO;
import com.project.plugin.result.Result;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(com.project.module.common.constant.RouteConstant.API_PREFIX + "/business")
public class BusinessPiApiController {

    @Resource
    private PiBusinessService service;

    @GetMapping("/list")
    public ResponseEntity<?> list(Page<PiBusiness> page, PiBusiness model, @RequestParam Map<String, Object> parameters) {
        // page.addOrder(OrderItem.asc(MybatisPlusUtils.columnToString(PiBusiness::getId)));
        return ResponseEntity.ok(Result.success().data(this.service.page(page, model, parameters)));
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam Long id) {
        PiBusiness model = this.service.detail(id);
        PiBusinessVO modelVO = this.service.convert(model);
        return ResponseEntity.ok(Result.success().data(modelVO));
    }

    @GetMapping("getBusinessDetail")
    public ResponseEntity<?> getBusinessDetail(PiBusiness model) {
        PiBusinessVO modelVO = service.getBusinessDetail(model);
        return ResponseEntity.ok(Result.success().data(modelVO));
    }

}

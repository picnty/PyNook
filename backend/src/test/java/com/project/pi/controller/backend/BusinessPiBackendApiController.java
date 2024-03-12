package com.project.pi.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.module.common.controller.api.BaseApiController;
import com.project.pi.constant.PiRouteConstant;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.pi.entity.PiBusiness;
import com.project.pi.service.PiBusinessService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Pi业务表(PiBusiness)表控制层
 *
 * @author system
 * @version v1.0.0
 */
@RestController
@RequestMapping(PiRouteConstant.BACKEND_API_BASE_PATH + "/piBusiness")
public class BusinessPiBackendApiController extends BaseApiController {

    /**
     * 服务对象
     */
    @Resource
    private PiBusinessService service;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param model 查询实体
     * @return 所有数据
     */
    @GetMapping
    public ResponseEntity<?> index(Page<PiBusiness> page, PiBusiness model, @RequestParam Map<String, Object> parameters) {
        // return Result.success().data(this.service.page(page, new QueryWrapper<>(model)));
        // return ResponseEntity.ok(Result.success().data(this.service.page(page, model, parameters)));
        return success(this.service.page(page, model, parameters));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        // return Result.success().data(this.service.getById(id));
        // return ResponseEntity.ok(Result.success().data(this.service.detail(id)));
        return success(this.service.detail(id));
    }

    /**
     * 新增数据
     *
     * @param model 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody PiBusiness model) {
        // this.service.save(model);
        // return Result.success().data(model);
        // return ResponseEntity.ok(Result.success().data(this.service.insert(model)));
        return success(this.service.insert(model));
    }

    /**
     * 修改数据
     *
     * @param model 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResponseEntity<?> update(@RequestBody PiBusiness model) {
        // this.service.updateById(model);
        // return Result.success().data(model);
        // return ResponseEntity.ok(Result.success().data(this.service.update(model)));
        return success(this.service.update(model));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        // List<Long> ids = Arrays.stream(Optional.ofNullable(id).orElse("").split(",")).map(Long::valueOf).collect(Collectors.toList());
        // return Result.success().data(this.service.removeByIds(ids));
        // return ResponseEntity.ok(Result.success().data(this.service.delete(id)));
        return success(this.service.delete(id));
    }

}


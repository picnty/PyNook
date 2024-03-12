package com.project.pi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.module.common.entity.TbMerchant;
import com.project.module.common.entity.TbProduct;
import com.project.module.common.vo.TbMerchantVO;
import com.project.pi.entity.PiBusiness;
import com.project.pi.vo.PiBusinessVO;

import java.util.List;
import java.util.Map;

/**
 * Pi业务表(PiBusiness)表服务接口
 *
 * @author system
 * @version v1.0.0
 */
public interface PiBusinessService extends IService<PiBusiness> {

    QueryWrapper<PiBusiness> search(PiBusiness model, Map<String, Object> parameters);

    Page<PiBusiness> page(Page<PiBusiness> page, PiBusiness model, Map<String, Object> parameters);

    PiBusiness detail(Long id);

    PiBusiness insert(PiBusiness model);

    PiBusiness update(PiBusiness model);

    Boolean delete(String id);

    PiBusinessVO convert(PiBusiness model);

    List<PiBusinessVO> convertList(List<PiBusiness> models);

    IPage<PiBusinessVO> pageVO(Page<PiBusiness> page, PiBusiness model, Map<String, Object> parameters);

    PiBusiness buildInitModel();

    PiBusiness findBusinessByUserId(Long userId);

    PiBusiness findBusinessByMerchantId(Long merchantId);

    TbMerchant findMerchantByUserId(Long userId);

    PiBusinessVO getBusinessData(Long userId);

    PiBusinessVO setBusinessData(Long userId, PiBusinessVO parameters);

    TbMerchant syncMerchant(Long userId, Long merchantId, TbMerchantVO merchantVO);

    Page<TbProduct> getMerchantProductList(Page<TbProduct> page, TbProduct model, Map<String, Object> parameters);

    TbProduct getMerchantProductDetail(Long id);

    TbProduct addMerchantProduct(TbProduct model);

    TbProduct updateMerchantProduct(TbProduct model);

    Boolean deleteMerchantProduct(TbProduct model);

    PiBusinessVO getBusinessDetail(PiBusiness model);

}


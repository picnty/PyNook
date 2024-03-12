package com.project.pi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.project.module.common.entity.TbMerchant;
import com.project.module.common.enums.CommonStatusEnum;
import com.project.module.common.enums.GlobalStatusCodeEnum;
import com.project.module.common.exception.ServiceException;
import com.project.module.common.service.TbMerchantService;
import com.project.pi.dto.BusinessPiDTO;
import com.project.pi.service.BusinessPiService;
import jakarta.annotation.Resource;
import com.project.module.common.util.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Objects;

@Deprecated
@Service
public class BusinessPiServiceImpl implements BusinessPiService {

    @Resource
    private TbMerchantService merchantService;

    @Override
    public BusinessPiDTO getBusinessData(Long userId) {
        BusinessPiDTO businessPiDTO = new BusinessPiDTO();

        TbMerchant merchant = merchantService.getOne(Wrappers.lambdaQuery(TbMerchant.class)
                        .eq(TbMerchant::getUserId, userId)
                , false);
        if (Objects.nonNull(merchant)) {
            BusinessPiDTO.Profile profile = new BusinessPiDTO.Profile();
            BeanUtils.copyProperties(merchant, profile); // 参数1 赋值给 参数2
            businessPiDTO.setProfile(profile);
        }

        return businessPiDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BusinessPiDTO setBusinessData(Long userId, BusinessPiDTO parameters) {
        Assert.isTrue(Objects.nonNull(parameters), "参数不能为空");

        BusinessPiDTO.Profile profile = parameters.getProfile();

        Long id = profile.getId();
        profile.setId(null); // 删除前端传过来的商户id参数

        String name = profile.getName();
        Long typeId = profile.getTypeId();
        Long categoryId = profile.getCategoryId();
        String address = profile.getAddress();
        BigDecimal longitude = profile.getLongitude();
        BigDecimal latitude = profile.getLatitude();
        String introduction = profile.getIntroduction();

        // todo：处理业务数据...

        TbMerchant merchant = merchantService.getOne(Wrappers.lambdaQuery(TbMerchant.class)
                        .eq(TbMerchant::getUserId, userId)
                , false);
        if (Objects.isNull(merchant)) {
            merchant = new TbMerchant();
            merchant.setUserId(userId);
            merchant.setStatus(CommonStatusEnum.ACTIVE.getValue());
        } else {
            profile.setId(merchant.getId());
        }
        BeanUtils.copyProperties(profile, merchant); // 参数1 赋值给 参数2

        // todo: 添加商户数据
        boolean saveOrUpdate = merchantService.saveOrUpdate(merchant);
        if (!Boolean.TRUE.equals(saveOrUpdate)) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getCode(), GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getMessage());
        }

        return this.getBusinessData(userId);
    }

    @Override
    public BusinessPiDTO getBusinessConfig(Long userId) {
        return null;
    }

    @Override
    public BusinessPiDTO setBusinessConfig(Long userId, BusinessPiDTO parameters) {
        return null;
    }

    @Override
    public BusinessPiDTO getBusinessPhotos(Long userId) {
        return null;
    }

    @Override
    public BusinessPiDTO setBusinessPhotos(Long userId, BusinessPiDTO parameters) {
        return null;
    }

    @Override
    public BusinessPiDTO getBusinessStamps(Long userId) {
        return null;
    }

    @Override
    public BusinessPiDTO setBusinessStamps(Long userId, BusinessPiDTO parameters) {
        return null;
    }

}

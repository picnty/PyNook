package com.project.pi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.module.common.dao.TbMerchantDao;
import com.project.module.common.dao.TbUserDao;
import com.project.module.common.entity.TbMerchant;
import com.project.module.common.entity.TbProduct;
import com.project.module.common.entity.TbUser;
import com.project.module.common.enums.CommonStatusEnum;
import com.project.module.common.enums.GlobalStatusCodeEnum;
import com.project.module.common.exception.ServiceException;
import com.project.module.common.service.AuthService;
import com.project.module.common.service.TbMerchantService;
import com.project.module.common.service.TbProductService;
import com.project.module.common.util.lombok.LombokUtils;
import com.project.module.common.vo.TbMerchantVO;
import com.project.pi.dao.PiBusinessDao;
import com.project.pi.entity.PiBusiness;
import com.project.pi.service.PiBusinessService;
import com.project.pi.vo.PiBusinessVO;
import com.project.plugin.datasource.mybatisplus.MybatisPlusUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import com.project.module.common.util.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Pi业务表(PiBusiness)表服务实现类
 *
 * @author system
 * @version v1.0.0
 */
@Service("piBusinessService")
public class PiBusinessServiceImpl extends ServiceImpl<PiBusinessDao, PiBusiness> implements PiBusinessService {

    @Resource
    private TbUserDao userDao;

    @Resource
    private TbMerchantDao merchantDao;

    @Resource
    private AuthService authService;

    @Resource
    private TbMerchantService merchantService;

    @Resource
    private TbProductService productService;

    @Override
    public QueryWrapper<PiBusiness> search(PiBusiness model, Map<String, Object> parameters) {
        QueryWrapper<PiBusiness> queryWrapper = new QueryWrapper<>(model);
        if (Objects.nonNull(parameters)) {
            // 关键字
            if (parameters.containsKey("keyword")) {
                String keyword = parameters.getOrDefault("keyword", "").toString();
                if (StringUtils.isNotBlank(keyword)) {
                    queryWrapper.lambda()
                            .like(PiBusiness::getId, keyword)
                    // .or().like(PiBusiness::getName, keyword)
                    ;
                }
            }
        }
        return queryWrapper;
    }

    @Override
    public Page<PiBusiness> page(Page<PiBusiness> page, PiBusiness model, Map<String, Object> parameters) {
        QueryWrapper<PiBusiness> queryWrapper = this.search(model, parameters);
        queryWrapper.lambda().orderByDesc(PiBusiness::getId);
        return this.page(page, queryWrapper);
    }

    @Override
    public PiBusiness detail(Long id) {
        PiBusiness model = this.getById(id);
        if (Objects.isNull(model)) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_NOT_FOUND.getCode(), GlobalStatusCodeEnum.DATA_ERROR_NOT_FOUND.getMessage());
        }
        // Long modelId = model.getId();
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PiBusiness insert(PiBusiness model) {
        boolean save = this.save(model);
        if (!Boolean.TRUE.equals(save)) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_SAVE.getCode(), GlobalStatusCodeEnum.DATA_ERROR_SAVE.getMessage());
        }
        // Long modelId = model.getId();
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PiBusiness update(PiBusiness model) {
        boolean update = this.updateById(model);
        if (!Boolean.TRUE.equals(update)) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_UPDATE.getCode(), GlobalStatusCodeEnum.DATA_ERROR_UPDATE.getMessage());
        }
        // Long modelId = model.getId();
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean delete(String id) {
        List<Long> ids = Arrays.stream(Optional.ofNullable(id).orElse("").split(",")).map(Long::valueOf).collect(Collectors.toList());
        return this.removeByIds(ids);
    }

    @Override
    public PiBusinessVO convert(PiBusiness model) {
        PiBusinessVO modelVO = new PiBusinessVO();
        BeanUtils.copyProperties(model, modelVO);
        return modelVO;
    }

    @Override
    public List<PiBusinessVO> convertList(List<PiBusiness> models) {
        return models.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public IPage<PiBusinessVO> pageVO(Page<PiBusiness> page, PiBusiness model, Map<String, Object> parameters) {
        Page<PiBusiness> modelPage = this.page(page, model, parameters);
        IPage<PiBusinessVO> modelVOPage = modelPage.convert(this::convert);
        if (Objects.nonNull(modelVOPage)) {
            // 记录列表
            List<PiBusinessVO> records = modelVOPage.getRecords();
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(records)) {
                // 项id列表
                Set<Long> itemIds = records.stream().map(PiBusinessVO::getUserId).collect(Collectors.toSet());
                // 项列表映射
                Map<Long, TbUser> itemListMap = new HashMap<>();
                // 项id列不为空
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(itemIds) && itemIds.size() > 0) {
                    LambdaQueryWrapper<TbUser> lambdaQueryWrapper = Wrappers.lambdaQuery(TbUser.class)
                            .select(TbUser::getId, TbUser::getUsername, TbUser::getNickname)
                            .in(TbUser::getId, itemIds);
                    List<TbUser> itemList = userDao.selectList(lambdaQueryWrapper);
                    itemListMap = itemList.stream().collect(Collectors.toMap(TbUser::getId, item -> item));
                }
                final Map<Long, TbUser> itemListMapFinal = itemListMap;
                // 循环处理
                records.forEach(record -> {
                    Long itemId = record.getUserId();
                    TbUser item = itemListMapFinal.get(itemId);
                    if (Objects.nonNull(item)) {
                        // record.setUserId(item.getId());
                    }
                });
                // 赋值新的记录列表
                modelVOPage.setRecords(records);
            }
        }
        return modelVOPage;
    }

    @Override
    public PiBusiness buildInitModel() {
        PiBusiness model = new PiBusiness();
        model.setStatus(CommonStatusEnum.ACTIVE.getValue()); // 状态
        return model;
    }

    @Override
    public PiBusiness findBusinessByUserId(Long userId) {
        PiBusiness model = this.getOne(Wrappers.lambdaQuery(PiBusiness.class)
                        .eq(PiBusiness::getUserId, userId)
                , false);
        return model;
    }

    @Override
    public PiBusiness findBusinessByMerchantId(Long merchantId) {
        PiBusiness model = this.getOne(Wrappers.lambdaQuery(PiBusiness.class)
                        .eq(PiBusiness::getMerchantId, merchantId)
                , false);
        return model;
    }

    @Override
    public TbMerchant findMerchantByUserId(Long userId) {
        PiBusiness business = this.findBusinessByUserId(userId);
        if (Objects.nonNull(business)) {
            Long merchantId = business.getMerchantId();
            if (Objects.nonNull(merchantId)) {
                TbMerchant merchant = merchantService.detail(merchantId);
                return merchant;
            }
        }
        throw new ServiceException(GlobalStatusCodeEnum.BUSINESS_ERROR.getCode(), "商户未开通");
    }

    @Override
    public PiBusinessVO getBusinessData(Long userId) {
        PiBusiness model = this.findBusinessByUserId(userId);
        if (Objects.isNull(model)) {
            model = this.buildInitModel();
        }
        PiBusinessVO modelVO = this.convert(model);
        Long merchantId = modelVO.getMerchantId();
        if (Objects.isNull(merchantId)) {
            modelVO.setMerchant(new TbMerchantVO());
        }
        if (Objects.nonNull(merchantId)) {
            TbMerchant merchant = merchantDao.selectOne(Wrappers.lambdaQuery(TbMerchant.class)
                            .eq(TbMerchant::getId, merchantId)
                    , false);
            TbMerchantVO merchantVO = merchantService.convert(merchant);
            modelVO.setMerchant(merchantVO);
        }
        return modelVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PiBusinessVO setBusinessData(Long userId, PiBusinessVO parameters) {
        Assert.isTrue(Objects.nonNull(parameters), "参数不能为空");

        // 查找业务数据，不存在则创建
        PiBusiness business = this.findBusinessByUserId(userId);
        if (Objects.isNull(business)) {
            business = this.buildInitModel();
            business.setUserId(userId);
        }

        // 定义要排除的字段
        // String[] ignoreProperties = {"id", "merchantId", "userId", "status", "data"};
        // 将 sourceObject 的属性复制到 targetObject，但排除了 field1 和 field2
        // BeanUtils.copyProperties(sourceObject, targetObject, ignoreProperties);

        // 处理业务数据
        String[] ignoreProperties = {
                LombokUtils.extract(PiBusiness::getId),
                LombokUtils.extract(PiBusiness::getMerchantId),
                LombokUtils.extract(PiBusiness::getUserId),
                LombokUtils.extract(PiBusiness::getStatus),
                LombokUtils.extract(PiBusiness::getData),
        };
        BeanUtils.copyProperties(parameters, business, ignoreProperties); // 参数1 赋值给 参数2

        // 处理商户数据

        // 判断商户数据是否传入
        TbMerchantVO merchantVO = parameters.getMerchant();
        Long merchantId = business.getMerchantId();
        if (Objects.nonNull(merchantVO)) {
            // 同步商户数据
            TbMerchant merchant = this.syncMerchant(userId, merchantId, merchantVO);
            if (Objects.isNull(business.getMerchantId())) {
                merchantId = merchant.getId();
                business.setMerchantId(merchantId);
            }
        }

        // 保存或更新数据
        boolean saveOrUpdate = this.saveOrUpdate(business);
        if (!Boolean.TRUE.equals(saveOrUpdate)) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getCode(), GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getMessage());
        }

        return this.getBusinessData(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TbMerchant syncMerchant(Long userId, Long merchantId, TbMerchantVO merchantVO) {
        TbMerchant merchant = new TbMerchant();
        merchant.setUserId(userId);
        merchant.setStatus(CommonStatusEnum.ACTIVE.getValue());

        if (Objects.nonNull(merchantId)) {
            merchant = merchantDao.selectOne(Wrappers.lambdaQuery(TbMerchant.class)
                            .eq(TbMerchant::getId, merchantId)
                            .eq(TbMerchant::getUserId, userId)
                    , false);
        }

        // 赋值数据
        // merchant.setName(merchantVO.getName());
        // merchant.setImage(merchantVO.getImage());
        // merchant.setImages(merchantVO.getImages());
        // merchant.setIntroduction(merchantVO.getIntroduction());
        // merchant.setAddress(merchantVO.getAddress());
        // merchant.setLongitude(merchantVO.getLongitude());
        // merchant.setLatitude(merchantVO.getLatitude());
        // merchant.setTypeId(merchantVO.getTypeId());
        // merchant.setCategoryId(merchantVO.getCategoryId());
        // 排除的字段
        String[] ignoreProperties = {
                LombokUtils.extract(TbMerchant::getId),
                LombokUtils.extract(TbMerchant::getUserId),
        };
        BeanUtils.copyProperties(merchantVO, merchant, ignoreProperties); // 参数1 赋值给 参数2

        // 判断商户名称是否已经存在, 如果存在则判断存在的数据是否为当前数据
        TbMerchant tbMerchant = merchantDao.selectOne(Wrappers.lambdaQuery(TbMerchant.class)
                        .eq(TbMerchant::getName, merchant.getName())
                , false);
        if (Objects.nonNull(tbMerchant)) {
            if (!Objects.equals(tbMerchant.getId(), merchant.getId())) {
                throw new ServiceException(GlobalStatusCodeEnum.BUSINESS_ERROR.getCode(), "商户名称已存在，请重新输入");
            }
        }

        // 保存或更新数据
        int saveOrUpdateCount = Objects.nonNull(merchant.getId()) ? merchantDao.updateById(merchant) : merchantDao.insert(merchant);
        if (Objects.isNull(saveOrUpdateCount) || saveOrUpdateCount <= 0) {
            throw new ServiceException(GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getCode(), GlobalStatusCodeEnum.DATA_ERROR_SAVE_OR_UPDATE.getMessage());
        }
        return merchant;
    }

    @Override
    public Page<TbProduct> getMerchantProductList(Page<TbProduct> page, TbProduct model, Map<String, Object> parameters) {
        Long userId = authService.getLoginUserId();
        TbMerchant merchant = this.findMerchantByUserId(userId);
        if (Objects.isNull(merchant)) {
            // return new Page<>(page.getCurrent(), page.getSize());
            // return page; // 返回空结果
        }
        Long merchantId = merchant.getId();
        model.setMerchantId(merchantId);
        // 根据id倒序从大到小排列
        page.addOrder(OrderItem.desc(MybatisPlusUtils.columnToString(TbProduct::getId)));
        Page<TbProduct> models = productService.page(page, model, parameters);
        return models;
    }

    @Override
    public TbProduct getMerchantProductDetail(Long id) {
        Long userId = authService.getLoginUserId();
        TbMerchant merchant = this.findMerchantByUserId(userId);
        Long merchantId = merchant.getId();
        TbProduct model = productService.detail(id);
        if (Objects.isNull(model)) {
            throw new ServiceException(GlobalStatusCodeEnum.PERMISSION_DENIED.getCode(), "商品不存在");
        }
        if (Objects.nonNull(model) && !Objects.equals(model.getMerchantId(), merchantId)) {
            // return new TbProduct(); // 返回空结果
            throw new ServiceException(GlobalStatusCodeEnum.PERMISSION_DENIED.getCode(), "对不起，您没有操作权限！");
        }
        return model;
    }

    @Override
    public TbProduct addMerchantProduct(TbProduct model) {
        Assert.isTrue(Objects.nonNull(model), "参数不能为空");
        Long userId = authService.getLoginUserId();
        TbMerchant merchant = this.findMerchantByUserId(userId);
        Long merchantId = merchant.getId();
        // 构造商品初始化模型
        TbProduct product = productService.buildInitModel();
        product.setMerchantId(merchantId);
        // 排除的字段
        String[] ignoreProperties = {
                LombokUtils.extract(TbProduct::getId),
                LombokUtils.extract(TbProduct::getMerchantId),
        };
        BeanUtils.copyProperties(model, product, ignoreProperties); // 参数1 赋值给 参数2
        return productService.insert(product);
    }

    @Override
    public TbProduct updateMerchantProduct(TbProduct model) {
        Assert.isTrue(Objects.nonNull(model), "参数不能为空");
        Long productId = model.getId();
        // 获取商品详情
        TbProduct product = this.getMerchantProductDetail(productId);
        // 排除的字段
        String[] ignoreProperties = {
                LombokUtils.extract(TbProduct::getId),
                LombokUtils.extract(TbProduct::getMerchantId),
        };
        BeanUtils.copyProperties(model, product, ignoreProperties); // 参数1 赋值给 参数2
        return productService.update(product);
    }

    @Override
    public Boolean deleteMerchantProduct(TbProduct model) {
        Assert.isTrue(Objects.nonNull(model), "参数不能为空");
        Long productId = model.getId();
        TbProduct product = this.getMerchantProductDetail(productId);
        return productService.delete(String.valueOf(product.getId()));
    }

    @Override
    public PiBusinessVO getBusinessDetail(PiBusiness model) {
        QueryWrapper<PiBusiness> queryWrapper = new QueryWrapper<>(model);
        PiBusiness business = this.getOne(queryWrapper, false);
        return this.convert(business);
    }

}


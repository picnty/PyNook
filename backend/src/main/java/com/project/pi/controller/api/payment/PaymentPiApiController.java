package com.project.pi.controller.api.payment;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.project.module.common.entity.TbOrder;
import com.project.module.common.enums.CommonStatusEnum;
import com.project.module.common.enums.order.OrderStatusCodeEnum;
import com.project.module.common.service.TbOrderService;
import com.project.pi.constant.PiRouteConstant;
import com.project.pi.sdk.PiSDKService;
import com.project.pi.sdk.model.PiPaymentDTO;
import com.project.plugin.result.Result;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(PiRouteConstant.API_BASE_PATH + "/payment")
public class PaymentPiApiController {

    private static final Logger log = LoggerFactory.getLogger(PaymentPiApiController.class);

    @Resource
    private PiSDKService piSDKService;

    @Resource
    private TbOrderService orderService;

    @RequestMapping("")
    public ResponseEntity<?> index() {
        Map<String, Object> map = new LinkedHashMap<>();
        return ResponseEntity.ok(Result.success().data(map));
    }

    /**
     * Pi 批准付款
     */
    @PostMapping("/approve")
    public ResponseEntity<?> approve(@RequestBody Map<String, Object> parameters) {
        String paymentId = parameters.getOrDefault("paymentId", "").toString();
        PiPaymentDTO piPaymentDTO = piSDKService.approve(paymentId);
        return ResponseEntity.ok(Result.success().data(piPaymentDTO));
    }

    /**
     * Pi 完成付款
     */
    @PostMapping("/complete")
    public ResponseEntity<?> complete(@RequestBody Map<String, Object> parameters) {
        String paymentId = parameters.getOrDefault("paymentId", "").toString();
        String txid = parameters.getOrDefault("txid", "").toString();
        PiPaymentDTO piPaymentDTO = piSDKService.complete(paymentId, txid);
        if (piSDKService.isPaymentSuccess(piPaymentDTO)) {
            // 支付成功
            // Pi 异步通知 notify
            this.notify(piPaymentDTO);
        }
        return ResponseEntity.ok(Result.success().data(piPaymentDTO));
    }

    public void notify(PiPaymentDTO piPaymentDTO) {
        // String jsonString = JSONObject.toJSONString(piPaymentDTO);
        log.info("[PiPay][notify][piPaymentDTO => {}] [{}.{}({}:{})]", JSON.toJSONString(piPaymentDTO), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getFileName(), Thread.currentThread().getStackTrace()[1].getLineNumber());

        String identifier = piPaymentDTO.getIdentifier();
        String transactionId = Objects.requireNonNull(piPaymentDTO.getTransaction()).getTxid();
        BigDecimal amount = piPaymentDTO.getAmount();
        Map<String, Object> metadata = (Map<String, Object>) piPaymentDTO.getMetadata();
        String orderNo = metadata.getOrDefault("orderNo", "").toString();
        BigDecimal totalAmount = new BigDecimal(metadata.getOrDefault("totalAmount", "0").toString());

        // TODO: 处理支付成功后的业务逻辑
        // log.debug("处理支付成功后的业务逻辑");
        // service.handleLog(orderNo, amount, transactionId);
        TbOrder order = orderService.getOne(Wrappers.lambdaQuery(TbOrder.class).eq(TbOrder::getOrderNo, orderNo), false);
        BigDecimal orderTotalAmount = order.getTotalAmount();
        // 截取小数点后7位，因为PiPay返回的金额只保留小数点后7位
        orderTotalAmount = orderTotalAmount.setScale(7, RoundingMode.DOWN); // BigDecimal.ROUND_DOWN

        // 实际支付金额 小于 应支付金额
        if (amount.compareTo(orderTotalAmount) == -1) {
            // 表示支付金额不对
            log.info("[PiPay][notify][order => {}] [{}.{}({}:{})]", JSON.toJSONString(order), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getFileName(), Thread.currentThread().getStackTrace()[1].getLineNumber());
            return;
        }

        order.setStatus(OrderStatusCodeEnum.ORDER_STATUS_PAID.getCode());
        order.setPaymentStatus(CommonStatusEnum.ACTIVE.getValue());
        order.setPaymentTime(LocalDateTime.now());
        order.setPaymentMethod("PiPay");
        order.setPaymentNo(transactionId);
        orderService.updateById(order);

    }

}

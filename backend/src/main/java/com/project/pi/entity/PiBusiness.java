package com.project.pi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.project.module.common.entity.BaseEntity;
import com.project.plugin.datasource.jpa.MapJpaJsonConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PiBusiness.TABLE_NAME,
        indexes = {
                @Index(columnList = "menu_status", name = "idx_menu_status"),
                @Index(columnList = "accept_order_status", name = "idx_accept_order_status"),
                @Index(columnList = "accept_payment_status", name = "idx_accept_payment_status"),
                @Index(columnList = "loyalty_card_status", name = "idx_loyalty_card_status"),
                @Index(columnList = "merchant_id", name = "idx_merchant_id"),
                @Index(columnList = "user_id", name = "idx_user_id"),
                @Index(columnList = "status", name = "idx_status"),
        },
        uniqueConstraints = {
        }
)
@org.hibernate.annotations.Comment(PiBusiness.TABLE_COMMENT)
@com.baomidou.mybatisplus.annotation.TableName(value = PiBusiness.TABLE_NAME, autoResultMap = true)
public class PiBusiness extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "pi_business"; // 表名
    public static final String TABLE_COMMENT = "Pi业务表"; // 表备注

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(name = "id", columnDefinition = "bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID'")
    private Long id;

    @Column(name = "menu_status", columnDefinition = "tinyint(4) DEFAULT '0' COMMENT '菜单状态:0-关闭;1-开启'")
    private Integer menuStatus;

    @Column(name = "accept_order_status", columnDefinition = "tinyint(4) DEFAULT '0' COMMENT '接受订单状态:0-关闭;1-开启'")
    private Integer acceptOrderStatus;

    @Column(name = "accept_payment_status", columnDefinition = "tinyint(4) DEFAULT '0' COMMENT '接受付款状态:0-关闭;1-开启'")
    private Integer acceptPaymentStatus;

    @Column(name = "loyalty_card_status", columnDefinition = "tinyint(4) DEFAULT '0' COMMENT '会员卡状态:0-关闭;1-开启'")
    private Integer loyaltyCardStatus;

    @Column(name = "free_item_name", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '免费物品名称'")
    private String freeItemName;

    @Column(name = "free_item_number", columnDefinition = "int(11) DEFAULT NULL COMMENT '免费物品数量'")
    private Integer freeItemNumber;

    @Column(name = "free_item_needed_stamp_number", columnDefinition = "int(11) DEFAULT NULL COMMENT '免费物品需要的邮票数量'")
    private Integer freeItemNeededStampNumber; // 邮票数量/兑换券数量

    @Convert(converter = MapJpaJsonConverter.class)
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = JacksonTypeHandler.class)
    @Column(name = "data", columnDefinition = "text COMMENT '数据(JSON格式)'")
    private Map<String, Object> data;

    @Column(name = "merchant_id", columnDefinition = "bigint(20) unsigned DEFAULT NULL COMMENT '商户ID'")
    private Long merchantId;

    @Column(name = "user_id", columnDefinition = "bigint(20) unsigned DEFAULT NULL COMMENT '用户ID'")
    private Long userId;

    @Column(name = "status", columnDefinition = "tinyint(4) DEFAULT '0' COMMENT '状态:0-禁用;1-正常'")
    private Integer status;

}

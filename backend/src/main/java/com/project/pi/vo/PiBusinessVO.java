package com.project.pi.vo;

import com.project.module.common.vo.TbMerchantVO;
import com.project.pi.entity.PiBusiness;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class PiBusinessVO extends PiBusiness implements Serializable {

    private TbMerchantVO merchant;

}

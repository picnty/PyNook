package com.project.pi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessPiDTO implements Serializable {

    private Profile profile = new Profile();

    @Data
    public static class Profile implements Serializable {

        private Long id;
        private String name = "";
        private Long typeId;
        private Long categoryId;
        private String address = "";
        private String introduction = "";
        private BigDecimal longitude;
        private BigDecimal latitude;

    }

    private Config config = new Config();

    @Data
    public static class Config implements Serializable {

        public Integer menuStatus = 0;
        public Integer acceptOrderStatus = 0;
        public Integer acceptPaymentStatus = 0;

    }

    private String qrcode = "";
    private List<String> photos = new ArrayList<>();

}

package com.project.pi.service;

import com.project.pi.dto.BusinessPiDTO;

@Deprecated
public interface BusinessPiService {

    BusinessPiDTO getBusinessData(Long userId);

    BusinessPiDTO setBusinessData(Long userId, BusinessPiDTO parameters);

    BusinessPiDTO getBusinessConfig(Long userId);

    BusinessPiDTO setBusinessConfig(Long userId, BusinessPiDTO parameters);

    BusinessPiDTO getBusinessPhotos(Long userId);

    BusinessPiDTO setBusinessPhotos(Long userId, BusinessPiDTO parameters);

    BusinessPiDTO getBusinessStamps(Long userId);

    BusinessPiDTO setBusinessStamps(Long userId, BusinessPiDTO parameters);

}

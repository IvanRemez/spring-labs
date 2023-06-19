package com.cydeo.service;

import com.cydeo.dto.DiscountDTO;

import java.util.List;

public interface DiscountService {

    List<DiscountDTO> getDiscountList();

    DiscountDTO createDiscount(DiscountDTO discountDTO);

    DiscountDTO updateDiscount(DiscountDTO discountDTO);

    DiscountDTO getDiscountByName(String name);
}

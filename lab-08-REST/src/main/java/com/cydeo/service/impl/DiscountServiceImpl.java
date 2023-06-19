package com.cydeo.service.impl;

import com.cydeo.dto.CustomerDTO;
import com.cydeo.dto.DiscountDTO;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Discount;
import com.cydeo.exception.NotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.DiscountRepository;
import com.cydeo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final MapperUtil mapperUtil;

    public DiscountServiceImpl(DiscountRepository discountRepository, MapperUtil mapperUtil) {
        this.discountRepository = discountRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<DiscountDTO> getDiscountList() {

        return discountRepository.findAll().stream().map(
                discount -> mapperUtil.convert(discount, new DiscountDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {

        Discount discount = discountRepository.save(mapperUtil.convert(discountDTO, new Discount()));

        return mapperUtil.convert(discount, new DiscountDTO());
    }

    @Override
    public DiscountDTO updateDiscount(DiscountDTO discountDTO) {

        Discount discountToUpdate = discountRepository.findById(discountDTO.getId())
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        Discount updatedDiscount = discountRepository.save(
                mapperUtil.convert(discountDTO, new Discount()));

        return mapperUtil.convert(updatedDiscount, new DiscountDTO());
    }

    @Override
    public DiscountDTO getDiscountByName(String name) {

        Discount discount = discountRepository.findFirstByName(name);

        return mapperUtil.convert(discount, new DiscountDTO());
    }
}

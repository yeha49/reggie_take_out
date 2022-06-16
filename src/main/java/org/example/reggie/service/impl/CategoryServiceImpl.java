package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.reggie.common.CustomException;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.Setmeal;
import org.example.reggie.mapper.CategoryMapper;
import org.example.reggie.service.CategoryService;
import org.example.reggie.service.DishService;
import org.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.eq(Dish::getCategoryId, id);

        int count = dishService.count(queryWrapper);

        if (count > 0){
            throw new CustomException("當前分類關聯了菜品，不能刪除");
        }

        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<Setmeal>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);

        int count1 = setmealService.count(queryWrapper1);

        if (count1 > 0){
            throw new CustomException("當前分類關聯了套餐，不能刪除");

        }

        super.removeById(id);

    }
}

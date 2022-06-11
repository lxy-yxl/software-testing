package com.example.demo.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-12-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IService<Order> {
    @Resource
    ObjectServiceImpl objectService;
    @Resource
    OrderMapper orderMapper;

    public int generateOrder(int objectId, int borrowerId, LocalDateTime lentoutTime, LocalDateTime returnTime, String campus) {
        int lentDays = (int) Math.ceil((float) Duration.between(lentoutTime, returnTime).getSeconds() / 3600 / 24);
        if (lentDays < 0) {
            return -1;
        } else {
            Order order = new Order();
            order.setBorrowerId(borrowerId);
            order.setLentoutTime(lentoutTime);
            order.setReturnTime(returnTime);
            order.setObjectId(objectId);
            order.setStatus("待支付");
            order.setFinishedTime(null);
            order.setCreatedTime(LocalDateTime.now());
            order.setCampus(campus);
            Integer deposit = objectService.getBaseMapper().selectById(objectId).getDeposit();
            Integer rentDaily = objectService.getBaseMapper().selectById(objectId).getRentDaliy();
            order.setRentTotal(rentDaily * lentDays + deposit);
            int count = orderMapper.insert(order);
            if (count <= 0) {
                return -2;
            } else {
                return order.getOrderId();
            }
        }
    }

    public int confirm(int orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("已完成");
        order.setFinishedTime(LocalDateTime.now());
        int count = orderMapper.updateById(order);
        if (count <= 0) {
            return -1;
        } else
            return 1;
    }

    public int cancelOrder(int orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return -1;
        } else if (order.getStatus().equals("已支付")) {
            return -2;
        } else {
            order.setStatus("已取消");
            int count = orderMapper.updateById(order);
            if (count <= 0) {
                return -3;
            } else {
                return 1;
            }
        }
    }

    public List<JSONObject> getOrderList(Integer userId) {

        return orderMapper.getOrderList(userId);
    }

    public int pay(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return -1;
        } else {
            order.setStatus("待评价");
            int count = orderMapper.updateById(order);
            if (count <= 0) {
                return -2;
            } else {
                return count;
            }
        }
    }


    public String getObjectName(Integer orderId) {
        String objectName = orderMapper.getObjectName(orderId);
        return objectName;
    }


}

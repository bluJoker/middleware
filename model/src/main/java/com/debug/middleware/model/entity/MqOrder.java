package com.debug.middleware.model.entity;

import java.util.Date;

public class MqOrder {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_order.id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_order.order_id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    private Integer orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_order.business_time
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    private Date businessTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_order.memo
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    private String memo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_order.id
     *
     * @return the value of mq_order.id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_order.id
     *
     * @param id the value for mq_order.id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_order.order_id
     *
     * @return the value of mq_order.order_id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_order.order_id
     *
     * @param orderId the value for mq_order.order_id
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_order.business_time
     *
     * @return the value of mq_order.business_time
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public Date getBusinessTime() {
        return businessTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_order.business_time
     *
     * @param businessTime the value for mq_order.business_time
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public void setBusinessTime(Date businessTime) {
        this.businessTime = businessTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_order.memo
     *
     * @return the value of mq_order.memo
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_order.memo
     *
     * @param memo the value for mq_order.memo
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
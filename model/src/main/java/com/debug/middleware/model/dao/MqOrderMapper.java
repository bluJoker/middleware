package com.debug.middleware.model.dao;

import com.debug.middleware.model.entity.MqOrder;

public interface MqOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    int insert(MqOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    int insertSelective(MqOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    MqOrder selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    int updateByPrimaryKeySelective(MqOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_order
     *
     * @mbggenerated Thu May 21 14:31:55 CST 2020
     */
    int updateByPrimaryKey(MqOrder record);
}
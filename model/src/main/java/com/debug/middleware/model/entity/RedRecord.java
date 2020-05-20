package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

//@Data
//@ToString
public class RedRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.user_id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.red_packet
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private String redPacket;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.total
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private Integer total;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.amount
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private BigDecimal amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.is_active
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private Byte isActive;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_record.create_time
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.id
     *
     * @return the value of red_record.id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.id
     *
     * @param id the value for red_record.id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.user_id
     *
     * @return the value of red_record.user_id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.user_id
     *
     * @param userId the value for red_record.user_id
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.red_packet
     *
     * @return the value of red_record.red_packet
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public String getRedPacket() {
        return redPacket;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.red_packet
     *
     * @param redPacket the value for red_record.red_packet
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket == null ? null : redPacket.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.total
     *
     * @return the value of red_record.total
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.total
     *
     * @param total the value for red_record.total
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.amount
     *
     * @return the value of red_record.amount
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.amount
     *
     * @param amount the value for red_record.amount
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.is_active
     *
     * @return the value of red_record.is_active
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public Byte getIsActive() {
        return isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.is_active
     *
     * @param isActive the value for red_record.is_active
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_record.create_time
     *
     * @return the value of red_record.create_time
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_record.create_time
     *
     * @param createTime the value for red_record.create_time
     *
     * @mbggenerated Sun May 17 00:20:15 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
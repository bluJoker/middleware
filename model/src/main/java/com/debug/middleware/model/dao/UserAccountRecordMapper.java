package com.debug.middleware.model.dao;

import com.debug.middleware.model.entity.UserAccountRecord;

public interface UserAccountRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    int insert(UserAccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    int insertSelective(UserAccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    UserAccountRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    int updateByPrimaryKeySelective(UserAccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_account_record
     *
     * @mbggenerated Fri May 22 13:53:40 CST 2020
     */
    int updateByPrimaryKey(UserAccountRecord record);
}
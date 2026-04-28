package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.entity.RiskReminder;

import java.util.List;

public interface RiskReminderMapper {
    int insert(RiskReminder reminder);

    List<RiskReminder> selectByUserId(@Param("userId") Long userId);

    List<RiskReminder> selectAll();

    int updateReadStatus(@Param("id") Long id, @Param("readStatus") Integer readStatus);

    int updateReadStatusByUserId(@Param("userId") Long userId, @Param("readStatus") Integer readStatus);

    int deleteById(@Param("id") Long id);
}

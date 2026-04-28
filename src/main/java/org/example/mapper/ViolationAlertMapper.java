package org.example.mapper;

import org.example.entity.ViolationAlert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ViolationAlertMapper {
    int insert(ViolationAlert alert);

    List<ViolationAlert> selectAll();

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List<Long> ids);
}

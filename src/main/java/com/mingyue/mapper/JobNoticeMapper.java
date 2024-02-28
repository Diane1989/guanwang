package com.mingyue.mapper;

import com.mingyue.vo.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JobNoticeMapper {

    int insertMessage(MessageVo messageVo);

    int compareMessage(@Param("userName") String userName, @Param("msg") String msg);


}

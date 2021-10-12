package com.boot.dao;

import com.boot.entity.SmsReplyReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("replyRepositoy")
public interface SmsReplyRepositoy extends JpaRepository<SmsReplyReceive, Long>, JpaSpecificationExecutor<SmsReplyReceive> {
    List<SmsReplyReceive> findByChannelIdAndCreateTimeBetween(Long channelid, Date from, Date to);
}

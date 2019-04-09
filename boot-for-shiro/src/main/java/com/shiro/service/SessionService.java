package com.shiro.service;

import com.shiro.model.UserStatistic;

import java.util.List;

public interface SessionService {

    List<UserStatistic> list();

    boolean kickOut(String sId);
}

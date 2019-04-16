package com.shiro.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.RandomSessionIdGenerator;
import org.hibernate.boot.model.Caching;

import java.io.Serializable;
import java.util.Collection;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 14:21
 */
public class SelfSessionDAO  extends CachingSessionDAO {

    public SelfSessionDAO(){
        setSessionIdGenerator(new RandomSessionIdGenerator());
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        //持久化session 可以缓存可以DB
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //从缓存或者DB获取缓存信息
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()){
            return;//会话已停止，无需更新
        }
        //不然就通过缓存或者DB更新session
    }

    @Override
    protected void doDelete(Session session) {
        //缓存操作或者DB操作删除session信息
    }

}

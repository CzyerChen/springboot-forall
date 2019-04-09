package com.shiro.service.impl;

import com.shiro.domain.TUser;
import com.shiro.model.UserStatistic;
import com.shiro.service.SessionService;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 16:50
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private RedisSessionDAO sessionDAO;

    @Override
    public List<UserStatistic> list() {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        List<UserStatistic> list = new ArrayList<>();

        for(Session session : activeSessions){
            UserStatistic userStatistic = new UserStatistic();
            //TUser user = new TUser();
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                String primaryPrincipal = (String)principalCollection.getPrimaryPrincipal();
                userStatistic.setUsername(primaryPrincipal);
                userStatistic.setUserId(primaryPrincipal);
            }
            userStatistic.setId(String.valueOf(session.getId()));
            userStatistic.setHost(session.getHost());
            userStatistic.setStartTimestamp(session.getStartTimestamp());
            userStatistic.setLastAccessTime(session.getLastAccessTime());
            long timeout = session.getTimeout();
            if(timeout == 0){
                userStatistic.setStatus("OFFLINE");
            }else {
                userStatistic.setStatus("ONLINE");
            }
            userStatistic.setTimeout(timeout);
           list.add(userStatistic);
        }
        return list;
    }

    @Override
    public boolean kickOut(String sId) {
        Session session = sessionDAO.readSession(sId);
        //让它状态为离线
        //session.setTimeout(0);
        sessionDAO.delete(session);
        return true;
    }
}

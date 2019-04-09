package com.shiro.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 16:45
 */
public class ShiroSessionListener implements SessionListener {

    private final AtomicLong sessionCount = new AtomicLong(0L);
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
    }

    @Override
    public void onStop(Session session) {
          sessionCount.decrementAndGet();
    }

    @Override
    public void onExpiration(Session session) {
       sessionCount.decrementAndGet();
    }
}

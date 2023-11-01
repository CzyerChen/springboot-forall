package com.demo.mvc.bean;

import java.io.Serializable;

/**
 * @author
 */
public class StatisticsAliveDto implements Serializable {

    private Integer total;

    private Integer init;

    private Integer cancel;

    private Integer active;

    private Integer charge;

    private Integer send;

    private Integer open;

    private Integer destroy;

    private Integer initerror;

    private String active_rate;

    private String charge_rate;

    private String total_suc;

    private Long asyncsize;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getInit() {
        return init;
    }

    public void setInit(Integer init) {
        this.init = init;
    }

    public Integer getCancel() {
        return cancel;
    }

    public void setCancel(Integer cancel) {
        this.cancel = cancel;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getSend() {
        return send;
    }

    public void setSend(Integer send) {
        this.send = send;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getDestroy() {
        return destroy;
    }

    public void setDestroy(Integer destroy) {
        this.destroy = destroy;
    }

    public Integer getIniterror() {
        return initerror;
    }

    public void setIniterror(Integer initerror) {
        this.initerror = initerror;
    }

    public String getActive_rate() {
        return active_rate;
    }

    public void setActive_rate(String active_rate) {
        this.active_rate = active_rate;
    }

    public String getCharge_rate() {
        return charge_rate;
    }

    public void setCharge_rate(String charge_rate) {
        this.charge_rate = charge_rate;
    }

    public String getTotal_suc() {
        return total_suc;
    }

    public void setTotal_suc(String total_suc) {
        this.total_suc = total_suc;
    }

    public Long getAsyncsize() {
        return asyncsize;
    }

    public void setAsyncsize(Long asyncsize) {
        this.asyncsize = asyncsize;
    }
}

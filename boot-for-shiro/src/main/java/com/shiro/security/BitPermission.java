package com.shiro.security;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;

/**
 * Desciption:权限字符串格式：+ 资源字符串 + 权限位 + 实例 ID；以 + 开头中间通过 + 分割；权限：0 表示所有权限；1 新
 * 增（二进制：0001）、2 修改（二进制：0010）、4 删除（二进制：0100）、8 查看（二进制：1000）；如 +user+10 表示对资源 user 拥有修改 / 查看权限
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 18:12
 */

/**
 * 解析权限的书写格式，进行合理的封装
 */
public class BitPermission implements Permission {
    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public  BitPermission(String input){
        String[] array = input.split("\\+");
        if(array.length > 1) {
            resourceIdentify = array[1];
        }
        if(StringUtils.isEmpty(resourceIdentify)) {
            resourceIdentify = "*";
        }
        if(array.length > 2) {
            permissionBit = Integer.valueOf(array[2]);
        }
        if(array.length > 3) {
            instanceId = array[3];
        }
        if(StringUtils.isEmpty(instanceId)) {
            instanceId = "*";
        }
    }

    /**
     * 判断权限匹配，看是否适合自己处理
     * @param p
     * @return
     */
    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof  BitPermission)){
           return  false;
        }
        BitPermission other = (BitPermission)p;
        if(!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))){//不是书写为* 并且用户标识不相等的，不能验证
            return  false;
        }
        if(!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) !=0)){
            return false;
        }

        if(!("*".equals(this.instanceId)|| this.instanceId.equals(other))){//权限位不相等并且不为*的，不能验证
            return  false;
        }
        return true;
    }

    public String getResourceIdentify() {
        return resourceIdentify;
    }

    public void setResourceIdentify(String resourceIdentify) {
        this.resourceIdentify = resourceIdentify;
    }

    public int getPermissionBit() {
        return permissionBit;
    }

    public void setPermissionBit(int permissionBit) {
        this.permissionBit = permissionBit;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}

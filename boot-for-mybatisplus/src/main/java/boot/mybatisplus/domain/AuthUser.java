package boot.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数据实体类
 * @author claire
 * @since 2019-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "app_auth_user")
public class AuthUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String  email;

    /**
     * 展示名称
     */
    private String  displayName;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证token
     */
    private String token;

    private transient String id;

    private Boolean disabled;

}

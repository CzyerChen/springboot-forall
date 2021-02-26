/**
 * Author:   claire
 * Date:    2020-02-11 - 09:48
 * Description: 年龄维度常量类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-11 - 09:48          V1.3.1           年龄维度常量类
 */
package boot.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能简述
 * 〈年龄维度常量类〉
 *
 * @author claire
 * @date 2020-02-11 - 09:48
 * @since 1.3.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dim_age_type")
public class DmpDimAgeTypeMapping implements Serializable {
    private Integer ageCode;
    private String codeDescription;
}

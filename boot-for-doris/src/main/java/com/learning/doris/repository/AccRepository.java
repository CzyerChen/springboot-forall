/**
 * Author:   claire Date:    2025/5/23 - 16:41 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2025/5/23 - 16:41          V1.0.0
 */

package com.learning.doris.repository;

import com.learning.doris.entity.Acc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccRepository extends JpaRepository<Acc, Long> {
}

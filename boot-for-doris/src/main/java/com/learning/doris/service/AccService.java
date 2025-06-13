/**
 * Author:   claire Date:    2025/5/23 - 16:42 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2025/5/23 - 16:42          V1.0.0
 */

package com.learning.doris.service;

import com.learning.doris.repository.AccRepository;
import com.learning.doris.entity.Acc;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccService {

    @Autowired
    private AccRepository accRepository;

    public List<Acc> getAllAccs() {
        return accRepository.findAll();
    }

    public Optional<Acc> getAccById(Long id) {
        return accRepository.findById(id);
    }

    public Acc createAcc(Acc acc) {
        return accRepository.save(acc);
    }

    public void saveAccs(List<Acc> accs) {
       accRepository.saveAll(accs);
    }

}
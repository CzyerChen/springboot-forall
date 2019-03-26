package com.mappers.controller;

import com.mappers.domain.ProfilePO;
import com.mappers.domain.dto.ProfileDTO;
import com.mappers.mapper.ProfileMapper;
import com.mappers.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 15:20
 */
@RestController
@RequestMapping("/test")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileMapper profileMapper;


    @GetMapping("/{id}")
    public ResponseEntity testMapper(@PathVariable int id){
        ProfilePO po = profileRepository.findById(id);
        ProfileDTO dto = profileMapper.profilePOToProfileDTO(po);
        return ResponseEntity.ok(dto.toString());
    }
}

/**
 * Author:   claire Date:    2025/5/23 - 16:36 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2025/5/23 - 16:36          V1.0.0
 */

package com.learning.doris.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    // Getters and Setters
}
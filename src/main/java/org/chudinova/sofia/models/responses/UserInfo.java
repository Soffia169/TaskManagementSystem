package org.chudinova.sofia.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Long id;
    private String name;
}

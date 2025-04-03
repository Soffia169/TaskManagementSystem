package org.chudinova.sofia.models.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private long role_id;
}

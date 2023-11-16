package com.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data


public class Users {
    private String id;
    private String name;
    private String email;
    private String status;
    private String gender;
}

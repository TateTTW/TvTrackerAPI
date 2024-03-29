package com.t8webs.tvtrackerapi.enterprise.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data

public class UserAccount {
    /**
     * UserAccount's unique identifier
     */
    private String username;
    private String password;
    private String email;
    private Timestamp birthDate;
    /**
     * Random generated character string used for authentication
     */
    private String token;
    /**
     * <p>Holds the time of the user's last successful login.</p>
     * <p>Use this to check whether a token needs to be updated.</p>
     */
    private Timestamp lastLogin;
}

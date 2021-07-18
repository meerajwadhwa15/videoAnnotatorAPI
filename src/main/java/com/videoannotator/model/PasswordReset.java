package com.videoannotator.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "password_reset_token")
public class PasswordReset {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate = calculateExpiryDate();

    public PasswordReset(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public PasswordReset() {}

    private Date calculateExpiryDate() {
        var cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, PasswordReset.EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
}

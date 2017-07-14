package com.market.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Role role;

    public enum Role {


        ADMIN("ADMIN"),
        USER("USER");

        private String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        @Override
        public String toString() {
            return this.role;
        }

    }


}

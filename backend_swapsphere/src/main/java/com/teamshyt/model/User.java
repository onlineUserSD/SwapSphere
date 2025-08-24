package com.teamshyt.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false, length = 255) // more length = better for hashing - since pass will be hashed then stored
                                            // so hash version will need more lengths in comparison to normal plain
                                            // passwords and 255 is max length for a DB column
    private String password;

    // address ---
    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2; // optional - user can fill or leave blank

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 6)
    private String pincode; // used string instead of long so that '026435' can be stored as it is in DB not
                            // like '26435'

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

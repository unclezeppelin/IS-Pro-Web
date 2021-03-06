package ua.in.usv.entity.root;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import static ua.in.usv.entity.root.UserRole.USER;

@Entity
@Getter
@Setter
@Table(name = "\"USER\"")
public class CustomUser {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_RCD", nullable = false, updatable = false)
    private long id;

    @Column(name = "User_Nm")
    private String login;

    @Column(name = "User_FIO")
    private String name;

    @Column(name = "User_Phn")
    private String phone;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private UserPassword userPassword;

    public CustomUser() {}

    public UserRole getRole() {
        return USER;
    }
}

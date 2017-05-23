package ua.in.usv.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import ua.in.usv.helper.ByteArray2String;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import static ua.in.usv.entity.UserRole.USER;

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

//    @Enumerated(EnumType.STRING)
//    private UserRole role = USER;

    @Column(name = "User_Nm")
    private String login;

//    @Column(name = "User_Pwd", nullable = false)
//    private String passwordHash;

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

    public void setUserPassword(UserPassword userPassword){
        this.userPassword = userPassword;
    }

    public String getPasswordHash(){
        String passwordHash ="";
        passwordHash = ByteArray2String.convert(userPassword.getPasswordBlob());
        return passwordHash;
    }
}

package com.example.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users_table")
public class UsersModel {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
private String name;
private String lastname;
private String customerNumber;
private String phone;
private String password;
private String confirmPassword;
private String email;
private Integer admin = 0; // Default to 0 for regular users
private String resetToken; // Token for password reset

public UsersModel() {
    this.admin = 0; // Ensures all new users are set as customers by default
}

public Integer getId() {
    return id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getLastname() {
    return lastname;
}

public void setLastname(String lastname) {
    this.lastname = lastname;
}

public String getPhone() {
    return phone;
}

public void setPhone(String phone) {
    this.phone = phone;
}

public void setId(Integer id) {
    this.id = id;
}

public String getCustomerNumber() {
    return customerNumber;
}

public void setCustomerNumber(String customerNumber) {
    this.customerNumber = customerNumber;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getConfirmPassword() {
    return confirmPassword;
}

public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public Integer getAdmin() {
    return admin;
}

public void setAdmin(Integer admin) {
    this.admin = admin;
}

public String getResetToken() {
    return resetToken;
}

public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
}



}

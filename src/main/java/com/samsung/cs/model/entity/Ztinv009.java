package com.samsung.cs.model.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ztinv009 {

    @Id
    @Column(name="id")
    private String id;
    
    @Column(name="company")
    private String company;
    
    @Column(name="asc_acctno")
    private String asc_acctno;
    
    @Column(name="asc_Code")
    private String asc_Code;
    
    @Column(name="parts_code")
    private String parts_Code;
    
    @Column(name="movty")
    private Integer movty;
    
    @Column(name="qty")
    private Double qty;
    
    @Column(name="erdat")
    private java.sql.Date erdat;
    
    @Column(name="service_type")
    private String service_type;

     
}
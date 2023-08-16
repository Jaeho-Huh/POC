package com.samsung.cs.model.entity;


import java.util.List;

import com.samsung.cs.model.entity.key.Ztinv001Id;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
//@IdClass(Ztinv001Id.class)
@Getter
@Setter
@EqualsAndHashCode
public class Ztinv001 {
	
	@EmbeddedId
	private Ztinv001Id ztinv001Id;
//	
//    //@Id
//    @Column(name="company")
//    private String company;
//
//    //@Id
//    @Column(name="asc_acctno")
//    private String asc_acctno;
//
//    //@Id
//    @Column(name="asc_code")
//    private String asc_code;
//
//    //@Id
//    @Column(name="parts_code")
//    private String parts_code;

    @Column(name="stock_qty")
    private Integer stock_qty;
    

     
    
    

}
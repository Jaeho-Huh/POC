package com.samsung.cs.model.entity.key;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@Data
@EqualsAndHashCode
public class Ztinv001Id implements Serializable {
	private String company;
    private String asc_acctno;
    private String asc_code;
    private String parts_code;

}
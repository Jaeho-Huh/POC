package com.samsung.cs.model.entity;

import java.util.Date;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="ztinv012_ver2__c",schema = "salesforce")
public class Ztinv012Ver2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="mon03_s__c")
    private Double mon03S;
    
    @Column(name="asc_acctno__c")
    private String ascAcctno;
    
    @Column(name="mon02_s__c")
    private Double mon02S;
    
    @Column(name="mon03_r__c")
    private Double mon03R;
    
    @Column(name="mon01_s__c")
    private Double mon01S;
    
    @Column(name="mon02_r__c")
    private Double mon02R;
    
    @Column(name="seq_no__c")
    private Double seqNo;
    
    @Column(name="remark__c")
    private String remark;
    
    @Column(name="prime_mat__c")
    private String primeMat;
    
    @Column(name="mon01_r__c")
    private Double mon01R;
    
    @Column(name="cuky__c")
    private String cuky;
    
    @Column(name="netpr__c")
    private String netpr;
    
    @Column(name="company__c")
    private String company;
    
    @Column(name="asc_code__c")
    private String ascCode;
    
    @Column(name="name")
    private String name;
    
    @Column(name="poqty__c")
    private Double poqty;
    
    @Column(name="ofs_con_poqty__c")
    private Double ofsConPoqty;
    
    @Column(name="so_item__c")
    private String soItem;
    
    @Column(name="qty__c")
    private Double qty;
    
    @Column(name="usage_avg__c")
    private Double usageAvg;
    
    @Column(name="msc_qty__c")
    private Double mscQty;
    
    @Column(name="isdeleted")
    private Boolean isDeleted;
    
    @Column(name="systemmodstamp")
    private Date systemModstamp;
    
    @Column(name="mon03__c")
    private Double mon03;
    
    @Column(name="ofs_con_mon03__c")
    private Double ofsConMon03;
    
    @Column(name="ofs_con_mon02__c")
    private Double ofsConMon02;
    
    @Column(name="ofs_con_transqty__c")
    private Double ofsConTransqty;
    
    @Column(name="transqty__c")
    private Double transqty;
    
    @Column(name="rso__c")
    private String rso;
    
    @Column(name="mon02__c")
    private Double mon02C;
    
    @Column(name="mon01__c")
    private Double mon01C;
    
    @Column(name="stock_qty__c")
    private Double stockQtyC;
    
    @Column(name="ofs_con_mon01__c")
    private Double ofsConMon01C;
    
    @Column(name="ofs_con_stock_qty__c")
    private Double ofsConStockQtyC;
    
    @Column(name="ofs_con_flag__c")
    private String ofsConFlagC;
    
    @Column(name="engr_qty__c")
    private Double engrQtyC;
    
    @Column(name="proposal_qty__c")
    private Double proposalQtyC;
    
    @Column(name="boqty__c")
    private Double boqtyC;
    
    @Column(name="ofs_con_boqty__c")
    private Double ofsConBoqtyC;
    
    @Column(name="ofs_con_proqty__c")
    private Double ofsConProqtyC;
    
    @Column(name="usage_avg_s__c")
    private Double usageAvgS;
    
    @Column(name="usage_avg_r__c")
    private Double usageAvgR;
    
    @Column(name="ofs_con_sold_to__c")
    private String ofsConSoldTo;
    
    @Column(name="comp_yn__c")
    private String compYn;
    
    @Column(name="unit__c")
    private String unit;
    
    @Column(name="so_no__c")
    private String soNo;
    
    @Column(name="sfid")
    private String sfid;
    
    @Column(name="_hc_lastop")
    private String hcLastop;
    
    @Column(name="_hc_err")
    private String hcErr;

}
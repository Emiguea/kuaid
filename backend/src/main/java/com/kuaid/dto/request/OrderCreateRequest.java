package com.kuaid.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderCreateRequest {

    @NotNull(message = "快递ID不能为空")
    private Long expressId;

    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    @NotBlank(message = "配送地址不能为空")
    private String deliveryAddress;

    private String studentRemark;

    @NotNull(message = "服务费不能为空")
    private BigDecimal fee;

    public Long getExpressId() { return expressId; }
    public void setExpressId(Long expressId) { this.expressId = expressId; }
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getStudentRemark() { return studentRemark; }
    public void setStudentRemark(String studentRemark) { this.studentRemark = studentRemark; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
}

package com.kuaid.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ExpressRegisterRequest {

    @NotBlank(message = "快递单号不能为空")
    private String trackingNo;

    private String company;

    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    @NotBlank(message = "收件人手机号不能为空")
    private String recipientPhone;

    private String recipientName;

    private String shelfNo;

    private String remark;

    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getShelfNo() { return shelfNo; }
    public void setShelfNo(String shelfNo) { this.shelfNo = shelfNo; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

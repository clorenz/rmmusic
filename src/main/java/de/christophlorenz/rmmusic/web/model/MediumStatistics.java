package de.christophlorenz.rmmusic.web.model;

/**
 * Created by clorenz on 10.06.15.
 */
public class MediumStatistics {

    String mediumTypeName;
    Long amount;
    String formattedSumValue;
    String formattedAvgValue;
    Long boughtMediaCount;

    public String getMediumTypeName() {
        return mediumTypeName;
    }

    public void setMediumTypeName(String mediumTypeName) {
        this.mediumTypeName = mediumTypeName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getFormattedSumValue() {
        return formattedSumValue;
    }

    public void setFormattedSumValue(String formattedSumValue) {
        this.formattedSumValue = formattedSumValue;
    }

    public String getFormattedAvgValue() {
        return formattedAvgValue;
    }

    public void setFormattedAvgValue(String formattedAvgValue) {
        this.formattedAvgValue = formattedAvgValue;
    }

    public Long getBoughtMediaCount() {
        return boughtMediaCount;
    }

    public void setBoughtMediaCount(Long boughtMediaCount) {
        this.boughtMediaCount = boughtMediaCount;
    }

    @Override
    public String toString() {
        return "MediumStatistics{" +
                "mediumTypeName='" + mediumTypeName + '\'' +
                ", amount=" + amount +
                ", formattedSumValue='" + formattedSumValue + '\'' +
                ", formattedAvgValue='" + formattedAvgValue + '\'' +
                ", boughtMediaCount=" + boughtMediaCount +
                '}';
    }
}

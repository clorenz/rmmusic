package de.christophlorenz.rmmusic.web.model;

/**
 * Created by clorenz on 10.06.15.
 */
public class MediumStatistics {

    String mediumTypeName;
    Integer amount;
    String formattedSumValue;
    String formattedAvgValue;
    Integer boughtMediaCount;

    public String getMediumTypeName() {
        return mediumTypeName;
    }

    public void setMediumTypeName(String mediumTypeName) {
        this.mediumTypeName = mediumTypeName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

    public Integer getBoughtMediaCount() {
        return boughtMediaCount;
    }

    public void setBoughtMediaCount(Integer boughtMediaCount) {
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

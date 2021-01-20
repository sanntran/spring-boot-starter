package net.ionoff.common.validation;

import io.swagger.annotations.ApiModelProperty;

import java.time.OffsetDateTime;
import java.util.List;


public class SampleApiModel {

    private OffsetDateTime fromDate = null;
    private OffsetDateTime toDate = null;
    private SampleApiModel sampleApiModel = null;
    private List<SampleApiModel> sampleApiModels = null;

    @ApiModelProperty(value = "Epoch timestamp. Ex 1587776157.418 (for GMT 25/0/2020 12:55:57 AM) | @NotNull")
    public OffsetDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(OffsetDateTime fromDate) {
        this.fromDate = fromDate;
    }

    @ApiModelProperty(value = "Epoch timestamp. Ex 1587776157.418 (for GMT 25/04/2020 12:55:57 AM) | @NotBlank | @AfterDate($field::fromDate)")
    public OffsetDateTime getToDate() {
        return toDate;
    }

    @ApiModelProperty(value = "")
    public SampleApiModel getSampleApiModel() {
        return sampleApiModel;
    }

    public void setSampleApiModel(SampleApiModel sampleApiModel) {
        this.sampleApiModel = sampleApiModel;
    }

    @ApiModelProperty(value = "@NotEmptyArray")
    public List<SampleApiModel> getSampleApiModels() {
        return sampleApiModels;
    }

    public void setSampleApiModels(List<SampleApiModel> sampleApiModels) {
        this.sampleApiModels = sampleApiModels;
    }

    public void setToDate(OffsetDateTime toDate) {
        this.toDate = toDate;
    }

}

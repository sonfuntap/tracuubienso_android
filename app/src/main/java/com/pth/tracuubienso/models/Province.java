package com.pth.tracuubienso.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Province extends ViewModel {
    private String nameProvince;
    private String codeProvince;
    private String information;
    private District district;


    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }

    public String getCodeProvince() {
        return codeProvince;
    }

    public void setCodeProvince(String codeProvince) {
        this.codeProvince = codeProvince;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public static class District {
        private String nameDistrict;
        private String  codeDistrict;
        private String information;

        public String getNameDistrict() {
            return nameDistrict;
        }

        public void setNameDistrict(String nameDistrict) {
            this.nameDistrict = nameDistrict;
        }

        public String getCodeDistrict() {
            return codeDistrict;
        }

        public void setCodeDistrict(String codeDistrict) {
            this.codeDistrict = codeDistrict;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }
    }
}

package com.pth.tracuubienso.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Province extends ViewModel {
    private String nameProvince;
    private List<String> codeProvinces = new ArrayList<>();
    private String information;
    private List<District> districts = new ArrayList<>();


    public void addDistrict(District district) {
        if (district != null) {
            districts.add(district);
        }
    }

    public void addCode(String code) {
        if (code != null) {
            codeProvinces.add(code);
        }
    }

    public List<String> getCodeProvinces() {
        return codeProvinces;
    }

    public void setCodeProvinces(List<String> codeProvinces) {
        this.codeProvinces = codeProvinces;
    }

    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }


    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public static class District {
        private String nameDistrict;
        private String codeDistrict;
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

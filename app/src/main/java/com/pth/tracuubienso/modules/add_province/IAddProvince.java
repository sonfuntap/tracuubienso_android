package com.pth.tracuubienso.modules.add_province;

import com.pth.tracuubienso.models.Province;

import java.util.List;

public interface IAddProvince {
    void passData(List<Province.District> districts);
}
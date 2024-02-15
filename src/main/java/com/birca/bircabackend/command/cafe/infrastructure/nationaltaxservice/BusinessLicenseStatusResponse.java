package com.birca.bircabackend.command.cafe.infrastructure.nationaltaxservice;

import java.util.List;

public record BusinessLicenseStatusResponse(String status_code,
                                            List<Data> data) {

    public String getTaxType() {
        return data.get(0).tax_type();
    }

    public record Data(String tax_type) {
    }
}


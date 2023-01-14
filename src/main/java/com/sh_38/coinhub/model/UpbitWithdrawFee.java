package com.sh_38.coinhub.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class UpbitWithdrawFee {
    private boolean success;
    private String data;

    private static ObjectMapper mapper = new ObjectMapper();

    public List<UpbitEachWithdrawFee> getData() throws IOException {
        return mapper.readValue(data, new TypeReference<>() {});
    }
}

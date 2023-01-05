package com.sh_38.coinhub.feign.response;

import lombok.Getter;

@Getter
public class BithumbResponse<T> {
    private String status;

    // data는 Key , Value로 이루어져있어 Map으로 해도 되는데 여기서는 제네릭으로 구현
    // T라는 자료형은 이 클래스를 사용할 때 밖에서 정해지게 됨.
    private T data;
}

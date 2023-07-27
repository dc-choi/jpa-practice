package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.") // 무조건 값이 있어야하는 프로퍼티에 붙이는 어노테이션
    private String name;

    private String city;
    private String street;
    private String zipcode;
}

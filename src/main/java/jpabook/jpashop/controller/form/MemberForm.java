package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.") //필수 입력값
    private String name;

    @NotEmpty(message = "도시 입력은 필수입니다.")
    private String city;

    private String street;

    @Size(min=4, max=5)
    private String zipcode;
}

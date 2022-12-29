package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
//Setter X - 초기에 생성 이외에 바꿀 수 없도록
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //jpa가 기본 생성자를 요구, 규칙
    protected Address() {

    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}

package jpabook.jpashop.domain;

import jpabook.jpashop.excection.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();


    // 비즈니스 로직 구현 // -> 서비스에서 하는거 아닌가요? => 응집도를 높이기 위해서
    // --> 도메인 주도 개발
    //재고 증가 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    //재고 감소 로직
    public void removeStock(int quantity) {
        if (this.stockQuantity - quantity < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity -= quantity;
    }
}

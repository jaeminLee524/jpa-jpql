package jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@Table(name = "Orders")
public class Order {

    @Id @GeneratedValue
    private Long id;
    private int orderAmount;
    @Embedded
    private Address address;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

}

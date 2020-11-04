package yes;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String productId;
    private Integer qty;
    private String status;

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        //ordered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.




        yes.external.Pay pay = new yes.external.Pay();
        pay.setOrderId(ordered.getId());
        Long lChargeAmount = Long.valueOf(12000);
        pay.setChargeAmount(lChargeAmount);
        pay.setStatus("Payed");

        // mappings goes here
        OrderApplication.applicationContext.getBean(yes.external.PayService.class)
                .payment(pay);


    }

    @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        BeanUtils.copyProperties(this, orderCancelled);
        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        yes.external.Cancellation cancellation = new yes.external.Cancellation();
        cancellation.setStatus("Delivery Canceled");
        cancellation.setOrderId(orderCancelled.getId());
        OrderApplication.applicationContext.getBean(yes.external.CancellationService.class)
                .deliverycancelled(cancellation);

        yes.external.Pay pay = new yes.external.Pay();
        pay.setStatus("Pay Canceled");
        pay.setOrderId(orderCancelled.getId());

        // mappings goes here
        OrderApplication.applicationContext.getBean(yes.external.PayService.class)
                .paymentcancel(pay);

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}

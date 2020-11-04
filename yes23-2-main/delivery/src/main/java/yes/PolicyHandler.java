package yes;

import yes.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayConfirmed_Ship(@Payload PayConfirmed payConfirmed){

        if(payConfirmed.isMe()){
            System.out.println("##### listener Ship : " + payConfirmed.toJson());

            Delivery delivery = new Delivery();
            delivery.setOrderId(payConfirmed.getOrderId().toString());
            delivery.setStatus("Shipping");
            delivery.setDeliveryInfo("Delivery Info");

            deliveryRepository.save(delivery);

        }
    }

}

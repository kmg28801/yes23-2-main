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
    CancellationRepository cancellationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCancelled_Cancel(@Payload PayCancelled payCancelled){

        if(payCancelled.isMe()){
            System.out.println("##### listener Cancel : " + payCancelled.toJson());

            Cancellation cancellation = new Cancellation();
            cancellation.setOrderId(payCancelled.getOrderId());
            cancellation.setStatus("Delivery Canceled");

            cancellationRepository.save(cancellation);
        }
    }

}

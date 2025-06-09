package br.com.order.ms_order.infrastructure.adapters.out.kafka;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.kafka.mapper.EventMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component("CreateOrderEventOutImpl")
public class CreateOrderEventOutImpl implements CreateOrderPortOut {

    private final KafkaTemplate<String , String> kafkaTemplate;
    private final EventMapper mapper;

    @Value("${order.topic}")
    private String topic ;

    private ProducerRecord<String,String> createProducerRecord(String eventDTO){
        return new ProducerRecord<>(topic,null,eventDTO);
    }
    private void sendEvent(ProducerRecord<String,String> record ){
        kafkaTemplate.send(record);
    }

    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) throws JsonProcessingException {
        try {
            var event = mapper.mapToEvent(orderDTO);
            log.info("send event : {}", event.getUuid());
            sendEvent(createProducerRecord(mapper.mapToStringEvent(event)));
            return mapper.mapFromEvent(event);
        }catch (Exception e){
            log.error("Error no envio do evento : {}",e.getLocalizedMessage());
            throw e;
        }
    }
}

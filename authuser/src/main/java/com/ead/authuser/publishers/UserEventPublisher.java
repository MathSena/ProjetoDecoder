package com.ead.authuser.publishers;

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enums.ActionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${ead.broker.exchange.userEvent}")
    private String exchangeUser;

    public void publishUserEvent(UserEventDto userEventDto, ActionType actionType) {
        log.info("Publishing user event: {}", userEventDto);
        userEventDto.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exchangeUser, "", userEventDto);
    }


}
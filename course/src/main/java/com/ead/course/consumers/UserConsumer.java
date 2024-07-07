package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDto;
import com.ead.course.enums.ActionType;
import com.ead.course.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserConsumer {

    @Autowired
    private UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenUserEvent(@Payload UserEventDto userEventDto) {
        var userModel = userEventDto.convertToUserModel();

        switch (ActionType.valueOf(userEventDto.getActionType())) {
            case CREATE:
                log.info("Saving userID: {}", userModel.getId());
                userService.save(userModel);
                break;
            case UPDATE:
                log.info("Updating userID: {}", userModel.getId());
                userService.save(userModel);
                break;
            case DELETE:
                log.info("Deleting userID: {}", userModel.getId());
                userService.delete(userModel.getId());
                break;
            default:
                log.error("Invalid action type: {}", userEventDto.getActionType());
                break;
        }

        log.info("User event received: {}", userEventDto);
    }
}

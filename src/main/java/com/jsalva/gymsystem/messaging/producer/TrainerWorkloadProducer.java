package com.jsalva.gymsystem.messaging.producer;

import com.jsalva.gymsystem.messaging.dto.TrainerWorkloadCommandMessageDto;
import com.jsalva.gymsystem.messaging.enums.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadProducer {

    private static final Logger logger = LoggerFactory.getLogger(TrainerWorkloadProducer.class);

    private final JmsTemplate jmsTemplate;

    private final String WORKLOAD_COMMAND_QUEUE = "trainer.workload.command.queue";

    public TrainerWorkloadProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    // Message Sending method
//    @Transactional // Jms transactional
    public void sendTrainerWorkloadMessage(TrainerWorkloadCommandMessageDto message, ActionType actionType) {
        // Add headers with auth token
        String token = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getCredentials();

        // get MDC transaction ID
        String transactionId = MDC.get("transactionId");

        jmsTemplate.convertAndSend(
                WORKLOAD_COMMAND_QUEUE,
                message,
                jmsMessage -> { // message post processor using lambda expression.
                    jmsMessage.setStringProperty("X-Transaction-Id", transactionId);
                    jmsMessage.setStringProperty("X-Action-Type", actionType.name());
                    return jmsMessage;
                }
        );

        logger.info(
                "Sent workload message. Trainer={}, Action={}, TxId={}",
                message.username(),
                actionType.name(),
                transactionId
        );

    }

}

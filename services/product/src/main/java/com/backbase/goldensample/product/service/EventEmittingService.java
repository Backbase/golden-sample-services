package com.backbase.goldensample.product.service;


import com.backbase.buildingblocks.backend.communication.context.OriginatorContextUtil;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventEmittingService {

    private final EventBus eventBus;
    private final OriginatorContextUtil originatorContextUtil;

    @Autowired
    public EventEmittingService(EventBus eventBus, OriginatorContextUtil originatorContextUtil) {
        this.eventBus = eventBus;
        this.originatorContextUtil = originatorContextUtil;
    }

//public void createSomething(InternalRequest<CreateRequestBody> request){
//
//    // ... create new resource using the submitted request ...
//
//    // Then emit an event:
//
//    MyResourceCreatedEvent event= new MyResourceCreatedEvent();
//    event.setData(request.getData());
//
//    EnvelopedEvent<T> envelopedEvent=new EnvelopedEvent<>();
//    envelopedEvent.setEvent(event);
//    envelopedEvent.setOriginatorContext(originatorContextUtil.create(internalRequest.getInternalRequestContext()));
//    eventBus.emitEvent(envelopedEvent);
//    }

}


package com.lcwd.electronic.store.exception;

import lombok.Builder;

@Builder
public class ResourcesNotFoundException extends RuntimeException{

    public ResourcesNotFoundException(){
        super("Resources not FOUND");

    }
    public ResourcesNotFoundException(String message){
        super(message);
    }
}

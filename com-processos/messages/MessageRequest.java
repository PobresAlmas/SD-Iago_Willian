package messages;

import java.io.Serializable;

public class MessageRequest implements Serializable {
    
    private String operation;
    private Object obj;

    public MessageRequest(String operation, Object obj) {
        this.operation = operation;
        this.obj = obj;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getObject() {
        return obj;
    }

    public void setPaciente(Object obj) {
        this.obj = obj;
    }
}
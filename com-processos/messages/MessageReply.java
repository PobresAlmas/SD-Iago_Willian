package messages;

import java.io.Serializable;

public class MessageReply implements Serializable {
    
   private int status;
   private String texto;

   public MessageReply(int status, String texto) {
    this.status = status;
    this.texto = texto;
   }

   public int getStatus() {
    return status;
   }

   public void setStatus(int status) {
    this.status = status;
   }

   public String getTexto() {
    return texto;
   }

   public void setTexto(String texto) {
    this.texto = texto;
   }
}

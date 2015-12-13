import model.message.Message;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MessageTest {

    @Test
    public void getMetaInformationFromMessage(){
        String message = "request_info#dasfklgj;dlf@name_of_sender";
        Message m = new Message();
        m.setStringMessage(message);
        assertEquals("REQUEST_INFO", m.getCommand());
        assertEquals("dasfklgj;dlf",m.getText());
        assertEquals("name_of_sender",m.getName());
    }

    @Test
    public void boundaryConditions(){
        String message = "request_info#@name";
        Message m = new Message();
        m.setStringMessage(message);
        assertEquals("REQUEST_INFO", m.getCommand());
        assertEquals("",m.getText());
        assertEquals("name",m.getName());

        message = "@name";
        m = new Message();
        m.setStringMessage(message);
        assertEquals("", m.getCommand());
        assertEquals("",m.getText());
        assertEquals("name",m.getName());
    }
}

import com.learning.KafkaDispacher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispacher<Order> dispacherOrder = new KafkaDispacher<>();
    private final KafkaDispacher<Email> dispacherEmail = new KafkaDispacher<>();

    @Override
    public void destroy() {
        dispacherOrder.close();
        dispacherEmail.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Creating a new order...");
        String queueName = "ECOMMERCE_NEW_ORDER";
        String queueNameEmail = "ECOMMERCE_SEND_EMAIL";

        String keyMessage = UUID.randomUUID().toString();
        String messageEmailKey = UUID.randomUUID().toString();
        Email email = new Email(" BananaPhone Order", "Thanks");

        String orderId = UUID.randomUUID().toString();
        BigDecimal orderValue = new BigDecimal(req.getParameter("value"));
        String orderUserEmail = req.getParameter("email");

        Order order = new Order(orderId, orderValue, orderUserEmail);

        try {

            dispacherOrder.send(queueName, keyMessage, order);
            dispacherEmail.send(queueNameEmail, messageEmailKey, email);

            String returnMessage = "Order has been sent successfully";
            System.out.println(returnMessage);

            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(returnMessage);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Order has been sent.");

    }
}

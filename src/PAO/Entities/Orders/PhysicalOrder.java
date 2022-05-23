package PAO.Entities.Orders;

import PAO.Entities.Customers.Customer;
import PAO.Entities.Products.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PhysicalOrder extends Order{
    public PhysicalOrder(Map< Product, Integer > products, Customer customer, LocalDate date) {
        super(products, customer, date);
        this.orderType = "Physical";
    }

}

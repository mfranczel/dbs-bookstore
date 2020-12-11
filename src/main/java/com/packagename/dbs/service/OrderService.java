package com.packagename.dbs.service;

import com.packagename.dbs.dao.repositories.AccessoryRepository;
import com.packagename.dbs.dao.repositories.BookCopyRepository;
import com.packagename.dbs.dao.repositories.OrderRepository;
import com.packagename.dbs.dao.repositories.StoreRepository;
import com.packagename.dbs.model.Manufacturer;
import com.packagename.dbs.model.Order;
import com.packagename.dbs.model.Store;
import com.packagename.dbs.model.products.Accessory;
import com.packagename.dbs.model.products.Book;
import com.packagename.dbs.model.products.BookCopy;
import com.packagename.dbs.model.products.Product;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;
    @Autowired
    AccessoryRepository accessoryRepository;
    @Autowired
    BookCopyRepository bookCopyRepository;
    @Autowired
    StoreRepository storeRepository;

    public List<Order> getAll(){
        List<Order> orders = (List<Order>) repository.findAll();
        return orders;
    }

    public List<Order> getAllFromStore(String store) {
        List<Order> orders = (List<Order>) repository.getAllFromStore(store);
        return orders;
    }

    public List<Order> getAllByState(String state) {
        return this.repository.getAllByState(state);
    }

    public List<Order> getAllByStateByStore(String state, String store){
        return this.repository.getAllByStateByStore(state, store);
    }

    public void changeState(Order order, String state) {
        order.setState(state);
        this.repository.saveAndFlush(order);

    }

    public int getOverAllN(List<List<Object>> ps) {
        int n = 0;
        for(List<Object> p:ps) {
            n += (int)p.get(1);
        }
        return n;
    }

    public void deliver(Order order){
        List<List<Object>> products = this.joinOrderItems(order);

        for(List<Object> pr: products) {
            Product prod = (Product) pr.get(0);
            prod.setIn_stock(prod.getIn_stock() + (int) pr.get(1));

            if(prod instanceof BookCopy) {
                this.bookCopyRepository.saveAndFlush((BookCopy) prod);
            } else {
                this.accessoryRepository.saveAndFlush((Accessory) prod);
            }

        }
    }


    public List<List<Object>> joinOrderItems(Order order){
        Map<Long, List<Object>> orders = new HashMap<>();

        for(Product p: order.getOrderItems()) {
            if(orders.containsKey(p.getId())) {
                int n = (int)orders.get(p.getId()).get(1) + 1;
                orders.get(p.getId()).remove(1);
                orders.get(p.getId()).add(n);
            } else {
                ArrayList<Object> tmp = new ArrayList<>();
                tmp.add(p);
                tmp.add(1);
                orders.put(p.getId(), tmp);
            }
        }
        return new ArrayList<List<Object>>(orders.values());

    }

    public boolean sendOrders(Store store, ArrayList<ArrayList<Object>> orderInfo){

        HashMap<String, Order> orders = new HashMap<>();

        for(ArrayList<Object> ar: orderInfo) {
            Product cp = (Product)ar.get(0);
            Manufacturer man = cp.getManufacturer();
            int n = (int) ar.get(2);
            //cp.setIn_stock(n);

            for(int i = 0; i < n; i++) {

                if(orders.containsKey(man.getName())) {
                    orders.get(man.getName()).getOrderItems().add(cp);
                } else {
                    List<Product> products = new ArrayList<>();
                    products.add(cp);

                    if(cp instanceof BookCopy) {
                        this.bookCopyRepository.saveAndFlush((BookCopy) cp);
                    } else {
                        this.accessoryRepository.saveAndFlush((Accessory) cp);
                    }

                    Order order = new Order("Sent", store, products);
                    order.setOrderDate(new Date());
                    orders.put(man.getName(), order);
                }
            }
        }
        this.storeRepository.saveAndFlush(store);

        Iterator it = orders.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            this.repository.saveAndFlush((Order)pair.getValue());
            it.remove();
        }

        return true;
    }
}

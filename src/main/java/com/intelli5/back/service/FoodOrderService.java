package com.intelli5.back.service;

import com.intelli5.back.domain.*;
import com.intelli5.back.domain.enumeration.PayStatus;
import com.intelli5.back.domain.enumeration.TicketStatus;
import com.intelli5.back.repository.FoodOrderRepository;
import com.intelli5.back.repository.MenuItemRepository;
import com.intelli5.back.repository.search.FoodOrderSearchRepository;
import com.intelli5.back.service.dto.ItemDTO;
import com.intelli5.back.service.dto.OrderDTO;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FoodOrder.
 */
@Service
@Transactional
public class FoodOrderService {

    private final Logger log = LoggerFactory.getLogger(FoodOrderService.class);

    @Inject
    private MenuItemRepository menuItemRepository;

    @Inject
    private FoodOrderRepository foodOrderRepository;

    @Inject
    private FoodOrderSearchRepository foodOrderSearchRepository;

    /**
     * Save a foodOrder.
     *
     * @param foodOrder the entity to save
     * @return the persisted entity
     */
    public FoodOrder save(FoodOrder foodOrder) {
        log.debug("Request to save FoodOrder : {}", foodOrder);
        FoodOrder result = foodOrderRepository.save(foodOrder);
        foodOrderSearchRepository.save(result);
        return result;
    }

    /**
     * create a foodOrder.
     *
     * @param orderDTO the entity to save
     * @return the persisted entity
     */
    public FoodOrder createOrder(OrderDTO orderDTO) {
        log.debug("Request to create FoodOrder : {}", orderDTO);
        FoodOrder foodOrder = new FoodOrder();
        BigDecimal price = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<OrderItem>();
        for (ItemDTO itemDTO: orderDTO.getItems()) {
            MenuItem menuItem = menuItemRepository.findOne(itemDTO.getId());
            price = price.add(menuItem.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setFoodOrder(foodOrder);
            orderItems.add(orderItem);
        }
        Payment payment = new Payment();
        payment.setTotalPrice(price);
        payment.setPaymentInfo(orderDTO.getPaymentInfo());
        payment.setStatus(PayStatus.PROCESS);

        FoodJoint foodJoint = new FoodJoint();
        foodJoint.setId(orderDTO.getFoodJointId());
        Ticket ticket = new Ticket();
        byte[] stream = QRCode.from("Hello World").stream().toByteArray();
        ticket.setNumber(100);
        ticket.setFoodJoint(foodJoint);
        ticket.setStatus(TicketStatus.WAIT);
        ticket.setFoodOrder(foodOrder);
        ticket.setQrCode(stream);
        ticket.setQrCodeContentType("image/png");

        foodOrder.setTotalPrice(price);
        foodOrder.setPayment(payment);
        foodOrder.setOrderItems(orderItems);
        foodOrder.setTicket(ticket);

        FoodOrder result = foodOrderRepository.save(foodOrder);
        foodOrderSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the foodOrders.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FoodOrder> findAll() {
        log.debug("Request to get all FoodOrders");
        List<FoodOrder> result = foodOrderRepository.findAll();

        return result;
    }


    /**
     *  get all the foodOrders where Ticket is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FoodOrder> findAllWhereTicketIsNull() {
        log.debug("Request to get all foodOrders where Ticket is null");
        return StreamSupport
            .stream(foodOrderRepository.findAll().spliterator(), false)
            .filter(foodOrder -> foodOrder.getTicket() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one foodOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FoodOrder findOne(Long id) {
        log.debug("Request to get FoodOrder : {}", id);
        FoodOrder foodOrder = foodOrderRepository.findOne(id);
        return foodOrder;
    }

    /**
     *  Delete the  foodOrder by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodOrder : {}", id);
        foodOrderRepository.delete(id);
        foodOrderSearchRepository.delete(id);
    }

    /**
     * Search for the foodOrder corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FoodOrder> search(String query) {
        log.debug("Request to search FoodOrders for query {}", query);
        return StreamSupport
            .stream(foodOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

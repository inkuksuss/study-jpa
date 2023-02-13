package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        try {
            initService.dbInit1();
            initService.dbInit2();;
        } catch (Exception | NotEnoughStockException e) {
            e.printStackTrace();
        }
    }



    @Service
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() throws NotEnoughStockException {
            Member member = createMember("userA", "a", "b", "c");
            em.persist(member);

            Book bookA = createBook("bookC", 20000, 100);
            em.persist(bookA);

            Book bookB = createBook("bookD", 30000, 200);
            em.persist(bookB);

            OrderItem orderItem1 = OrderItem.createOrderItem(bookA, 20000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(bookB, 30000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String bookC, int price, int quantity) {
            Book bookA = new Book();
            bookA.setName(bookC);
            bookA.setPrice(price);
            bookA.setStockQuantity(quantity);
            return bookA;
        }

        public void dbInit2() throws NotEnoughStockException {
            Member member = createMember("userB", "d", "c", "cd");
            em.persist(member);

            Book bookA = createBook("bookA", 10000, 300);
            em.persist(bookA);

            Book bookB = createBook("bookB", 20000, 100);
            em.persist(bookB);

            OrderItem orderItem1 = OrderItem.createOrderItem(bookA, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(bookB, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String userB, String d, String c, String cd) {
            Member member = new Member();
            member.setName(userB);
            member.setAddress(new Address(d, c, cd));
            return member;
        }
    }
}

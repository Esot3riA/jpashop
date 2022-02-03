package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne 형태
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    // N+1 문제가 발생함 (각 Order 가 Member, Delivery 엔터티를 끌고 옴)
    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // fetch join 을 사용하여 쿼리 1회에 조회, N+1 문제 해소
    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // DTO 에 해당하는 컬럼만 가져옴. V3보다 성능 최적화된 형태이나 findOrderDto 는 확장성이 낮음
    // 조회한 결과가 엔티티가 아닌 DTO 이므로 dirty check 가 불가능함
    // 사실 대부분의 경우 컬럼 몇 개 더 가져온다고 성능이 확 나빠지지 않음.. 대부분의 부하는 join 쪽에서 영향
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        // Repository 는 가급적 순수한 엔터티만 가져오도록 하고, SimpleQueryRepository 를 만들어서 DTO 전용 심플쿼리를 따로 관리
        return orderSimpleQueryRepository.findOrderDtos();
    }
}

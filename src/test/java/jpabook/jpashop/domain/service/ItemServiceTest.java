package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    @Rollback(value = false)
    public void 상품_등록() throws Exception {
        // given
        Album album = new Album();
        album.setArtist("YOASOBI");

        Book book = new Book();
        book.setName("book1");

        // when
        itemService.saveItem(album);
        itemService.saveItem(book);

        // then
        assertEquals(album, itemRepository.findOne(album.getId()));
        assertEquals(book, itemRepository.findOne(book.getId()));
    }
    
    @Test(expected = NotEnoughStockException.class)
    public void 재고_감소() throws Exception {
        // given
        Movie movie = new Movie();
        movie.setName("movie1");
        
        // when
        movie.removeStock(20);
        
        // then
        fail();
    }

}
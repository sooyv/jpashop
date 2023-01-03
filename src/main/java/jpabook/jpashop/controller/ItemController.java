package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
        public String newItem(Model model) {

        model.addAttribute("form", new BookForm());

        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
        public String createItem(BookForm form) {
        //Book
        Book bookItem = new Book();
        bookItem.setName(form.getName());
        bookItem.setPrice(form.getPrice());
        bookItem.setStockQuantity(form.getStockQuantity());
        bookItem.setAuthor(form.getAuthor());
        bookItem.setIsbn(form.getIsbn());
        itemService.saveItem(bookItem);

        System.out.println(form.getName());
        System.out.println(form.getPrice());
        System.out.println(form.getStockQuantity());

        return "redirect:/";
    }

    @GetMapping("/items")
    public String itemList(Model model) {
        List<Item> item = itemService.findItems();
        log.info(item.get(0).toString());
        model.addAttribute("items", item);

        return "/items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String updateItem(@PathVariable("id") Long id, Model model) {
        Item bookItem = itemService.findOne(id);
        model.addAttribute("bookItem", bookItem);

        return "/items/updateItem";
    }

    @PostMapping("/items/{id}/edit")
    public String updateItem(@PathVariable("id") Long id, BookForm bookForm) {
        // db에서 가지고 와서
        // db에서 findOne의 리턴 값을 확인해야함
        // 강제 형변환이 필요할 수도 있음
        Book bookItem = (Book)itemService.findOne(id);

        //가지고 온걸 수정을 하고
        bookItem.setName(bookForm.getName());
        bookItem.setPrice(bookForm.getPrice());
        bookItem.setStockQuantity(bookForm.getStockQuantity());
        bookItem.setAuthor(bookForm.getAuthor());
        bookItem.setIsbn(bookForm.getIsbn());

        // db로 다시 보내준다
        itemService.saveItem(bookItem);

//        return "/items/itemList";
        return "redirect:/items";
    }

    @GetMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable("id") Long id) {

        // 상품을 지우기 전에 상품과 관련된 것들을 지워줘야함.
        Item item = itemService.findOne(id);
        itemService.delete(item);

        return "redirect:/items";
    }

}
package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j // 로그를 찍을 수 있는 롬복 어노테이션이다.
public class HomeController {
    @GetMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
    }
}

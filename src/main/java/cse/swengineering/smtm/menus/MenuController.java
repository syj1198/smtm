package cse.swengineering.smtm.menus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MenuController {

    @Autowired
    private MenuRepository repository;

//    @GetMapping("/menus/week")
//    public List<User> users(){
//        return repository.findAll();
//    }
//
//    @GetMapping("/menus/{date}")
//    public Optional<User> employees(@PathVariable Long id){
//        return repository.findById(id);
//    }
//
//    @GetMapping("/menus/calendar")
//    public Optional<User> employees(@PathVariable Long id){
//        return repository.findById(id);
//    }
//
//    @PostMapping("/users")
//    public User process(@RequestParam String userId,
//                        @RequestParam String password,
//                        @RequestParam boolean isKorean){
//        return repository.save(new User(userId, password, isKorean));
//    }

}

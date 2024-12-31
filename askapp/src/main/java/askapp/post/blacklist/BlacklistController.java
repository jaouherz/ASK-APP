package askapp.post.blacklist;


import askapp.post.blacklist.models.Blacklist;
import askapp.post.blacklist.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/blacklist")
public class BlacklistController {

    @Autowired
    private BlackListService blackListService;

    @GetMapping("/addWord")
    public String addWord(Model model) {
        model.addAttribute("BlacklistForm", new Blacklist());
        return "new_blackword";
    }

    @PostMapping("/save")
    public String saveWord(@ModelAttribute("BlacklistForm") Blacklist blackList) throws Exception {
        blackListService.addWord(blackList);
        return "redirect:/api/v1/blacklist/all";
    }

    @GetMapping("/all")
    public String blackList(Model model) {
        model.addAttribute("blackList", blackListService.getAll());
        return "blacklist";
    }

    @GetMapping("/delete/{id}")
    public String deleteWord(@PathVariable("id") long id) {
        blackListService.delete(id);
        return "redirect:/api/v1/blacklist/all";
    }

    @GetMapping("/edit/{id}")
    public String editWord(@PathVariable("id") long id, Model model) {
        Blacklist blackList = blackListService.getById(id);
        model.addAttribute("BlacklistForm", blackList);
        return "new_blackword";
    }
}

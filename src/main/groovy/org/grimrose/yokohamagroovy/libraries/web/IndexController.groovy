package org.grimrose.yokohamagroovy.libraries.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/")
class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    String index(Model model) {
        model.addAttribute("messages", "Hello, Library!")
        "index"
    }

}

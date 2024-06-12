package com.example.mongo.controller;

import com.example.mongo.dto.ScoreDTO;
import com.example.mongo.service.ScoreMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreMongoService service;

    @GetMapping("/list")
    public String mongolist(Model model){
        List<ScoreDTO> list = service.findAll();
        model.addAttribute("mongolist",list);
        return "mongo/list";
    }
    @GetMapping("/insert")
    public String insertPage(){
        return "mongo/mongo_insert";
    }
    @PostMapping("/insert")
    public String insert(ScoreDTO document){
        System.out.println("컨트롤러"+document);
        service.insertDocument(document);
        return "redirect:/score/list";
    }
    @GetMapping("multi/insert")
    //동일한 형식의 입력데이터가 여러 개인 경우 파라미터를 동일한 이름으로 정의하고 형식에 맞게 DTO를 만들면 자동으로 List에 DTO가 담긴 형식으로 만들어진다.
    public String multiInsert(){
        List<ScoreDTO> docs = new ArrayList<>();
        ScoreDTO document = null;
        //임의로 10개의 데이터를 생성하는 코드이므로 나중에 쓰지 않아도 되는 코드
        for (int i = 11; i < 20; i++) {
            document = new ScoreDTO(null,"multi"+i,"multi"+i,"전산실","서울특별시",100,100);
            docs.add(document);
        }
        service.insertAllDocument(docs);
        return "redirect:/score/list";
    }
    @GetMapping("/paginglist")
    public String pagelist(@RequestParam("pageNo") String pageNo, Model model){
        List<ScoreDTO> pagelist = service.findAll(Integer.parseInt(pageNo));
        model.addAttribute("mongolist",pagelist);
        return "mongo/list";
    }
    @GetMapping("/search")
    public String searchPage(){
        return "/mongo/search";
    }
    @PostMapping("/search")
    public String search(@RequestParam("field") String field,@RequestParam("criteria") String criteria
                            ,@RequestParam("value") String value, Model model){
        List<ScoreDTO> searchlist = service.findCriteria(field+","+criteria,value);
        model.addAttribute("mongolist",searchlist);
        return "mongo/list";
    }
    @GetMapping("/read")
    public String read(@RequestParam("id") String id,@RequestParam("action") String action, Model model){
        ScoreDTO search= service.findById("id",id);
        String view ="";
        if(action.equals("detail")){
            view = "mongo/mongo_detail";
        }else if(action.equals("update")){
            view = "mongo/mongo_update";
        }
        model.addAttribute("search",search);
        return view;
    }
    @GetMapping("/read2")
    public String read2(@RequestParam("_id") String _id,@RequestParam("action") String action, Model model){
        ScoreDTO search= service.findById(_id);
        String view ="";
        if(action.equals("detail")){
            view = "mongo/mongo_detail";
        }else if(action.equals("update")){
            view = "mongo/mongo_update";
        }
        model.addAttribute("search",search);
        return view;
    }
    @PostMapping("/update")
    public String update(ScoreDTO document){
        service.update(document);
        return "redirect:/score/paginglist?pageNo=0";
    }

}

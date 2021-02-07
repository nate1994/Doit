package com.ssafy.doit.controller;

import com.ssafy.doit.model.Feed;
import com.ssafy.doit.model.Group;
import com.ssafy.doit.model.response.ResponseBasic;
import com.ssafy.doit.model.user.User;
import com.ssafy.doit.repository.*;
import com.ssafy.doit.service.AdminService;
import com.ssafy.doit.service.GroupUserService;
import com.ssafy.doit.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupUserService groupUserService;

    //관리자 - 회원 리스트
    @ApiOperation(value = "관리자 - 회원 리스트")
    @GetMapping("/searchAllUser")
    public Object searchAllUser(){ // 페이징 처리하기
        ResponseBasic result = new ResponseBasic();
        List<User> list = userRepository.findAll();

        if(list.size() == 0){
            result.status =false;
            result.data= "fail";
        }else{
            result.status = true;
            result.data = "success";
            result.object = list;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //관리자 - 회원 삭제
    @ApiOperation(value = "관리자 - 회원 탈퇴")
    @PutMapping("/beDeletedUser")
    public Object beDeletedUser(@RequestParam Long userPk) {
        ResponseBasic result = new ResponseBasic();
        try {
            Long adminPk = userService.currentUser();
            if(adminPk == 2) {
                Optional<User> userInfo = userRepository.findById(userPk);
                if (userInfo.isPresent()) {
                    groupUserService.deleteGroupByUser(userPk);
                }
            }else throw new Exception("관리자가 아닙니다.");
            result.status = true;
            result.data = "success";
        }
        catch (Exception e){
            e.printStackTrace();
            result.status = false;
            result.data = "fail";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //관리자 - 그룹 리스트
    @ApiOperation(value = "관리자 -그룹 리스트")
    @GetMapping("/searchAllGroup")
    public Object searchAllGroup(){ // 페이징 처리하기
        List<Group> list = groupRepository.findAll();
        ResponseBasic result = new ResponseBasic();
        if(list.size() == 0){
            result.status =false;
            result.data= "fail";
        }else{
            result.status = true;
            result.data = "success";
            result.object = list;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //그룹 삭제
    @ApiOperation(value = "관리자 - 그룹 삭제")
    @PutMapping("/beDeletedGroup")
    public Object beDeletedGroup(@RequestParam Long groupPk) {
        ResponseBasic result = new ResponseBasic();
        try {
            Long adminPk = userService.currentUser();
            if(adminPk == 2) {
              adminService.deleteAllByGroup(groupPk);
            }else throw new Exception("관리자가 아닙니다.");
            result.status = true;
            result.data = "success";
        }
        catch (Exception e){
            e.printStackTrace();
            result.status = false;
            result.data = "error";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //관리자 - 피드 삭제
    @ApiOperation(value = "관리자 - 피드 삭제")
    @PutMapping("/beDeletedFeed")
    public Object beDeletedFeed(@RequestParam Long feedPk) {
        ResponseBasic result = new ResponseBasic();
        try {
            Long adminPk = userService.currentUser();
            if(adminPk == 2) {
                adminService.deleteFeed(feedPk);
            }else throw new Exception("관리자가 아닙니다.");
            result.status = true;
            result.data = "success";
        }
        catch (Exception e){
            e.printStackTrace();
            result.status = false;
            result.data = "error";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

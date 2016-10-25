package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rasheed on 17/10/16.
 */
@RestController
public class MyRestController
{
    @Autowired
    private MyService myService;

    @RequestMapping(path = "/count", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public int count()
    {
        return myService.exampleMethod();
    }
}

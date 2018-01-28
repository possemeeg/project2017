package com.possemeeg.project2017.webdoor.controller;

import com.hazelcast.core.HazelcastInstance;
import com.possemeeg.project2017.webdoor.dynamic.DynamicResource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HelloController {
  private final ApplicationContext applicationContext;
  private final HazelcastInstance hazelcastInstance;

  public HelloController(@Autowired ApplicationContext applicationContext, @Autowired HazelcastInstance hazelcastInstance) {
    this.applicationContext = applicationContext;
    this.hazelcastInstance = hazelcastInstance;
  }

  @RequestMapping("/hello")
  public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    DynamicResource createOnDemand = applicationContext.getBean(DynamicResource.class);
    model.addAttribute("name", createOnDemand.toString());
    model.addAttribute("line", createOnDemand.line());

    return "hello";
  }
}

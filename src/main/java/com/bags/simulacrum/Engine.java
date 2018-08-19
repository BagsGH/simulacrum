package com.bags.simulacrum;


import org.springframework.stereotype.Component;

@Component
public class Engine  {

    public void start() {
        Weapon test = new Weapon();
        test.setMaxAmmo(500);
        System.out.print(test);
    }
}

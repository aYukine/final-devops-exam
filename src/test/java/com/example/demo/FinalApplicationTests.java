package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinalApplicationTests {

	@Test
	void contextLoads() {
	}
	
	void failingTest() {
        assertEquals(1, 2);
    }

}

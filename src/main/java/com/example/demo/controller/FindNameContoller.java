package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.KindOf;
import com.example.demo.service.AsyncService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FindNameContoller {

	private final AsyncService asyncService;

	@GetMapping("/usersAny/")
	@KindOf(id = "usersAny", name = "/usersAny")
	public List<String> findUsers() throws Exception {
		long start = System.currentTimeMillis();
		long heavyProcessTime = 3000L;
		long lightProcessTime = 1000L;
		long timeOutProcessTime = 4000L;

		log.warn("request started");
		CompletableFuture<String> heavyProcess = asyncService.findName("heavy", heavyProcessTime);
		CompletableFuture<String> lightProcess = asyncService.findName("light", lightProcessTime);
		CompletableFuture<String> timeOutProcess = asyncService.findName("timeOut", timeOutProcessTime);

		// heavyProcessが終わったら実行される処理
		heavyProcess.thenAcceptAsync(heavyProcessResult -> {
			log.warn("finished name=" + heavyProcessResult + ", sleep = " + heavyProcessTime);
		});

		// lightProcessが終わったら実行される処理
		lightProcess.thenAcceptAsync(lightProcessResult -> {
			log.warn("finished name=" + lightProcessResult + ", sleep = " + lightProcessTime);
		});

		// lightProcessが終わったら実行される処理
		timeOutProcess.thenAcceptAsync(timeOutProcessResult -> {
			log.warn("finished name=" + timeOutProcessResult + ", sleep = " + timeOutProcessTime);
		});

		//        try {
		//        timeOutProcess.get(1, TimeUnit.MILLISECONDS);
		//        }catch(Exception e) {
		//        	e.printStackTrace();
		//        }
		//
		//        // 指定した処理が終わったらこれ以降の処理が実行される
		//        CompletableFuture.allOf(heavyProcess, lightProcess).join();
		log.warn("end");

		// 返却値の作成
		List<String> names = new ArrayList<>();
		//        names.add(heavyProcess.get());
		//        names.add(lightProcess.get());
		try {
			names.add(heavyProcess.get(heavyProcessTime + 10, TimeUnit.MILLISECONDS));
		} catch (TimeoutException e) {
			log.warn("timeput: heavyProcess");
		}
		try {
			names.add(lightProcess.get(lightProcessTime + 1, TimeUnit.MILLISECONDS));
		} catch (TimeoutException e) {
			log.warn("timeput: lightProcess");
		}
		try {
			names.add(timeOutProcess.get(1, TimeUnit.MILLISECONDS));
		} catch (TimeoutException e) {
			log.warn("timeput: timeOutProcess");
		}

		//        Thread.sleep(10L);

		long end = System.currentTimeMillis();
		// 処理全体の時間を出力
		log.warn("request finished. time: " + ((end - start)) + "ms");

		return names;
	}

}

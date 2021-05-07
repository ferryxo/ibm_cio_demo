package com.cio.ibm.colorgroup;

import com.cio.ibm.colorgroup.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColorgroupApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ColorgroupApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(ColorgroupApplication.class, args);
		FileUtil.loadData();
	}

}

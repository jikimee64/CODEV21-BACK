package com.j2kb.codev21.domains.board.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MultipartFileService {
	
	@Autowired
    private HttpServletRequest request;

	@Value("${gcp.server.ip}")
	private String serverIp;

	@Value("${gcp.server.port}")
	private String serverPort;
	
	public String saveFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		//String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
		String realPathtoUploads = new File("").getAbsolutePath() + "/"; //tomcat경로가 아닌 절대경로
        if(!new File(realPathtoUploads).exists())
        {
            new File(realPathtoUploads).mkdir();
        }
        
        String orgName = multipartFile.getOriginalFilename();
        //String filePath = serverIp + ":" + serverPort + realPathtoUploads + orgName;
		String filePath = realPathtoUploads + orgName;
        File dest = new File(filePath);
        try {
        	multipartFile.transferTo(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return filePath;
	}
	
	public boolean delete(String fileDir) throws IOException {
		Path path = Paths.get(fileDir);
		FileSystemUtils.deleteRecursively(path);
		return true;
	}
}

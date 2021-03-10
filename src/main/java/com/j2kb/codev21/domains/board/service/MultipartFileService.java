package com.j2kb.codev21.domains.board.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultipartFileService {
	
	@Autowired
    private HttpServletRequest request;
	
	public String saveFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		final String uploadsDir = "/uploads/";
		String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
        if(!new File(realPathtoUploads).exists())
        {
            new File(realPathtoUploads).mkdir();
        }
        
        String orgName = multipartFile.getOriginalFilename();
        String filePath = realPathtoUploads + orgName;
        File dest = new File(filePath);
        try {
        	multipartFile.transferTo(dest);
		} catch (Exception e) {
			
		}
        
		return filePath;
	}
	
	public boolean delete(String fileDir) throws IOException {
		Path path = Paths.get(fileDir);
		FileSystemUtils.deleteRecursively(path);
		return true;
	}
}

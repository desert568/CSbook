package com.kinghis.yyoauth.servlet;

import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.util.RandomNumUtil;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet(name = "yzmServlet", urlPatterns = "/servlet/yzmServlet")
public class YzmServlet extends HttpServlet {

	private static final long serialVersionUID = 7517924339411521137L;
	
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setCharacterEncoding("UTF-8");
        RandomNumUtil rand = new RandomNumUtil();
        StringBuffer code = new StringBuffer();
        BufferedImage img = rand.genRandomCodeImage(code);
        HttpSession session = request.getSession();
        session.setAttribute(LoginConstants.YZM_CODE, code);
        try {
			ImageIO.write(img, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

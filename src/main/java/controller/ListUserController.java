package controller;

import http.HttpCookie;
import http.HttpRequest;
import http.HttpResponse;

import java.util.Collection;

import model.User;
import db.DataBase;

public class ListUserController extends AbstractController {
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		if (!isLogin(request.getCookie())) {
			response.sendRedirect("/user/login.html");
			return;
		}

		Collection<User> users = DataBase.findAll();
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='1'>");
		for (User user : users) {
			sb.append("<tr>");
			sb.append("<td>" + user.getUserId() + "</td>");
			sb.append("<td>" + user.getName() + "</td>");
			sb.append("<td>" + user.getEmail() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		response.forwardBody(sb.toString());		
	}

	private boolean isLogin(HttpCookie cookie) {
		String value = cookie.getCookie("logined");
		if (value == null) {
			return false;
		}
		return Boolean.parseBoolean(value);
	}
}

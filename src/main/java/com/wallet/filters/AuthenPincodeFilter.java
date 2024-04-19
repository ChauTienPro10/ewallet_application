//package com.wallet.filters;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.stream.Collectors;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletRequestWrapper;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.wallet.Entitis.Card;
//import com.wallet.Entitis.Member;
//import com.wallet.Repositories.CardRepository;
//import com.wallet.Repositories.MemberRepository;
//
////@WebFilter(urlPatterns = "/ltransaction/*")
//public class AuthenPincodeFilter implements Filter {
//	@Autowired
//	MemberRepository memberRepository;
//	@Autowired
//	CardRepository cardRepository;
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//		
//		try {
//
//			StringBuilder requestBodyBuilder = new StringBuilder();
//			try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpRequest.getInputStream()))) {
//				String line;
//				while ((line = reader.readLine()) != null) {
//					requestBodyBuilder.append(line);
//				}
//			} catch (IOException e) {
//				// Xử lý lỗi khi đọc dữ liệu yêu cầu
//				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request!");
//				return;
//			}
//
//			String requestBody = requestBodyBuilder.toString();
//
//			JsonParser parser = new JsonParser();
//			JsonElement jsonElement = parser.parse(requestBody);
//
//			if (jsonElement.isJsonObject()) {
//				JsonObject jsonObject = jsonElement.getAsJsonObject();
//				JsonElement pincodeElement = jsonObject.get("pincode");
//
//				if (pincodeElement != null && pincodeElement.isJsonPrimitive()) {
//					String pincode = pincodeElement.getAsString();
//
//					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//					String username = authentication.getName();
//					Member member = memberRepository.findByUsername(username);
//
//					if (member != null) {
//						Card card = cardRepository.findByMemberid(member.getMember_id());
//
//						if (card != null && card.getPin().equals(pincode)) {
//							System.out.println("continous");
//							
//							
//							chain.doFilter(request, response);
//							return;
//						}
//					}
//				}
//			}
//
//			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request!");
//		} catch (Exception e) {
//			e.printStackTrace();
//			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request!");
//
//		}
//
//	}
//
//}

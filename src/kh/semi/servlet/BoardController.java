package kh.semi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kh.semi.dao.BoardDAO;
import kh.semi.dao.MemberDAO;
import kh.semi.dao.PaymentDAO;
import kh.semi.dao.TitleImgDAO;
import kh.semi.dto.BoardDTO;
import kh.semi.dto.CommentDTO;
import kh.semi.dto.PaymentDTO;
import kh.semi.dto.TitleImgDTO;


@WebServlet("*.board")
public class BoardController extends HttpServlet {	
	private int fileIdNo = 1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String requestURI = request.getRequestURI(); 
		String contextPath = request.getContextPath();

		String cmd = requestURI.substring(contextPath.length());
		System.out.println(cmd);

		MemberDAO mdao = new MemberDAO();
		BoardDAO dao = new BoardDAO();
		PaymentDAO pdao = new PaymentDAO();
		TitleImgDAO tdao = new TitleImgDAO();

		try {
			if(cmd.contentEquals("/titleImagesMain.board")) {
				List<BoardDTO> list1 = dao.getDataForMain();
				int bNo1 = list1.get(0).getBoardNo();
				int bNo2 = list1.get(1).getBoardNo();
				int bNo3 = list1.get(2).getBoardNo();
				
				List<TitleImgDTO> list2 = tdao.getTitleImgMain(bNo1, bNo2, bNo3);
				String fName1 = list2.get(0).getFileName();
				String fPath1 = list2.get(0).getFilePath();
				String imgTag1 = fPath1 + fName1;
				
				String fName2 = list2.get(1).getFileName();
				String fPath2 = list2.get(1).getFilePath();
				String imgTag2 = fPath2 + fName2;
				
				String fName3 = list2.get(2).getFileName();
				String fPath3 = list2.get(2).getFilePath();
				String imgTag3 = fPath3 + fName3;
				
			}else if(cmd.contentEquals("/card1.board")) {
				List<BoardDTO> list = dao.getDataForMain();
							
				String title1 = list.get(0).getTitle();
				int goalAmount1 = list.get(0).getAmount();
				
				Timestamp dueDate1 = list.get(0).getDueDate();
				long dueTime1 = dueDate1.getTime();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				String dueDateStr1 = sdf1.format(dueTime1);
				
				int sumAmount1 = list.get(0).getSumAmount();
				double percentage1 = Math.floor((double)sumAmount1 / goalAmount1 * 100);
				// 마감 임박 1

				JsonObject obj1 = new JsonObject();
				obj1.addProperty("title1", title1);
				obj1.addProperty("dueDate1",dueDateStr1);
				obj1.addProperty("percentage1",percentage1);
				pw.print(obj1.toString());
			}else if(cmd.contentEquals("/card2.board")) {
				List<BoardDTO> list = dao.getDataForMain();
				
				String title2 = list.get(1).getTitle();
				int goalAmount2 = list.get(1).getAmount();
				Timestamp dueDate2 = list.get(1).getDueDate();
				long dueTime2 = dueDate2.getTime();
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				String dueDateStr2 = sdf2.format(dueTime2);
				int sumAmount2 = list.get(1).getSumAmount();
				double percentage2 = Math.floor((double)sumAmount2 / goalAmount2 * 100);
				 // 마감 임박2
				
				JsonObject obj2 = new JsonObject();
				obj2.addProperty("title2", title2);
				obj2.addProperty("dueDate2",dueDateStr2);
				obj2.addProperty("percentage2",percentage2);
				pw.print(obj2.toString());
			}else if(cmd.contentEquals("/card3.board")) {
				List<BoardDTO> list = dao.getDataForMain();
				
				String title3 = list.get(2).getTitle();
				int goalAmount3 = list.get(2).getAmount();
				Timestamp dueDate3 = list.get(2).getDueDate();
				long dueTime3 = dueDate3.getTime();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
				String dueDateStr3 = sdf3.format(dueTime3);
				int sumAmount3 = list.get(2).getSumAmount();
				double percentage3 = Math.floor((double)sumAmount3 / goalAmount3 * 100);
				 // 마감 임박 3
				
				JsonObject obj3 = new JsonObject();
				obj3.addProperty("title3", title3);
				obj3.addProperty("dueDate3",dueDateStr3);
				obj3.addProperty("percentage3",percentage3);
				pw.print(obj3.toString());
			}else if(cmd.contentEquals("/totalAmountDonors.board")) {
				
				int totalAmount = pdao.getTotalAmount();
				int countDonors = pdao.getNumberOfDonors();
				
				JsonObject obj = new JsonObject();
				obj.addProperty("totalAmount", totalAmount);
				obj.addProperty("countDonors", countDonors);
				pw.print(obj.toString());

			}else if(cmd.contentEquals("/write.board")) {
				request.getRequestDispatcher("/WEB-INF/boards/writer.jsp").forward(request, response);
			}else if(cmd.equals("/Read.board")) {
				int commentPage = Integer.parseInt(request.getParameter("commentPage"));
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				BoardDTO article = dao.selectOneArticle(boardNo);
				List<CommentDTO> comments = dao.selectCommentsByBoardNo(commentPage, boardNo);
				
				double amount = article.getAmount();
				double sumAmount = article.getSumAmount();
				double percentage = Math.floor((double)sumAmount / amount * 100);

//				TitleImgDTO titleImg = dao.getTitleImg(boardNo);
				//				response.setCharacterEncoding("UTF-8");
				//				request.setCharacterEncoding("UTF-8");
				//				request.setAttribute("titleImg", "files\\" + titleImg.getFileName());
				//				System.out.println("files\\" + titleImg.getFileName());

				DecimalFormat Commas = new DecimalFormat("#,###,###");

				request.setAttribute("comments", comments);
				request.setAttribute("percentage", percentage);
				request.setAttribute("boardNo", boardNo);
				request.setAttribute("amount", Commas.format(amount));
				request.setAttribute("sumAmount", Commas.format(sumAmount));
				request.setAttribute("result", article);

				request.getRequestDispatcher("read.jsp").forward(request, response);

			}else if(cmd.equals("/PaymentForm.board")){
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				String title = dao.selectOneArticle(boardNo).getTitle();

				String email = (String)request.getSession().getAttribute("loginEmail");
				List<String> result = mdao.selectByEmail(email);
				request.setAttribute("boardNo", boardNo);
				request.setAttribute("title", title);
				request.setAttribute("result", result);
				request.getRequestDispatcher("payment.jsp").forward(request, response);
			}else if(cmd.equals("/Payment.board")) {
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String phone = request.getParameter("phone");
				int amount = Integer.parseInt(request.getParameter("amount"));

				try {
					PaymentDTO dto = new PaymentDTO(boardNo, name, email, phone, amount, null);
					int result = pdao.insertPayment(dto);
					int result2 = dao.updateSumAccount(amount, boardNo);

					BoardDTO board = dao.selectOneArticle(boardNo);

					request.setAttribute("boardNo", boardNo);
					request.setAttribute("result", result);
					request.setAttribute("board", board);
					request.setAttribute("payment", dto);
					request.getRequestDispatcher("/WEB-INF/boards/payCompleted.jsp").forward(request, response);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(cmd.equals("/Recommend.board")) {
				String email = (String)request.getSession().getAttribute("loginEmail");
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				String title = request.getParameter("title");

				try {
					dao.updateRecommend(email, boardNo, title);
					int recommend = dao.selectOneArticle(boardNo).getRecommend();
					pw.print(recommend);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(cmd.equals("/RecommendCheck.board")) {
				String email = (String)request.getSession().getAttribute("loginEmail");
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				
				try {
					pw.print(dao.recommendCheck(email, boardNo));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(cmd.equals("/Comment.board")) {
				String email = (String)request.getSession().getAttribute("loginEmail");
				int boardNo = Integer.parseInt(request.getParameter("boardNo"));
				String comment = request.getParameter("comment");
				int currentPage = Integer.parseInt(request.getParameter("currentPage"));
				
				try {
					String name = mdao.selectByEmail(email).get(0);
					
					dao.insertComment(email, name, boardNo, comment);
					CommentDTO result = dao.selectCommentsByBoardNo(currentPage, boardNo).get(0);
					Gson gson = new Gson();
					pw.print(gson.toJson(result));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(cmd.equals("/DeleteComment.board")) {
				String email = (String)request.getSession().getAttribute("loginEmail");
				String writeDate = request.getParameter("writeDate");
				System.out.println(email + " : " + writeDate);
				
				int result = dao.deleteComment(email, writeDate);
				System.out.println(result);
				pw.print(result);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

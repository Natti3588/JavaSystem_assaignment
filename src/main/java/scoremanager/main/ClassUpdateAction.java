package scoremanager.main;

import bean.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassUpdateAction extends Action {
  
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    
	  // セッション情報をリクエストから取得
	    HttpSession session = req.getSession();
	    Teacher teacher = (Teacher) session.getAttribute("user");
//	    変更したいクラス番号を取得
	 String old =req.getParameter("num");
	 req.setAttribute("oldnum", old);
    // class_update.jspにフォワード
    req.getRequestDispatcher("class_update.jsp").forward(req, res);
  }

}

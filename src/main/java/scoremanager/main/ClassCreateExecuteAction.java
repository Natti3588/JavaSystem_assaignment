package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassCreateExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    // 予めクラス番号文字列を宣言
    String classNumstr = "";

    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    ClassNumDao classnumDao = new ClassNumDao();
	Map<String, String> errors = new HashMap<>(); // エラーメッセージ

    // パラメータから値を取得
    classNumstr = req.getParameter("num");

    // 取得したパラメータでclassNumインスタンスを生成
    ClassNum classNum = new ClassNum();
    classNum.setClass_num(classNumstr);
    classNum.setSchool(teacher.getSchool());
    if (classnumDao.get(classNumstr,teacher.getSchool()) != null) {
		errors.put("2", "クラス番号が重複しています");
		// リクエストにエラーメッセージをセット
		req.setAttribute("errors", errors);
	    req.getRequestDispatcher("class_create.jsp").forward(req, res);
	    return;}
    new ClassNumDao().save(classNum);

    req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
  }

}

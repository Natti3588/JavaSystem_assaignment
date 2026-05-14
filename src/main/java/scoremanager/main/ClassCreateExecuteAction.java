package scoremanager.main;

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

    // パラメータから値を取得
    classNumstr = req.getParameter("num");

    // 取得したパラメータでclassNumインスタンスを生成
    ClassNum classNum = new ClassNum();
    classNum.setClass_num(classNumstr);
    classNum.setSchool(teacher.getSchool());

    new ClassNumDao().save(classNum);

    req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
  }

}

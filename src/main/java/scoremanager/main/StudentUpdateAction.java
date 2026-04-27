package scoremanager.main;

import java.util.List;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    // セッション情報をリクエストから取得
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");

    // 変更対象の生徒IDを取得
    String student_no = req.getParameter("no");

    // studentDaoとclassNumDaoを宣言
    StudentDao studentDao = new StudentDao();
    ClassNumDao classNumDao = new ClassNumDao();

    // 学生情報を取得
    Student student = studentDao.get(student_no);

    // 文字列型のリストのclassNumに教師の学校にあるクラスを取得
    List<String> classNum = classNumDao.filter(teacher.getSchool());

    // リクエスト属性に値をセット
    req.setAttribute("ent_year", student.getEntYear());
    req.setAttribute("name", student.getName());
    req.setAttribute("no", student.getNo());
    req.setAttribute("class_num_set", classNum);

    // フォワードd
    req.getRequestDispatcher("student_update.jsp").forward(req, res);
  }

}
